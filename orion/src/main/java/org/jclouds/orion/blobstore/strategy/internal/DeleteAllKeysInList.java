/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.orion.blobstore.strategy.internal;

import static com.google.common.base.Throwables.propagate;
import static org.jclouds.blobstore.options.ListContainerOptions.Builder.recursive;
import static org.jclouds.concurrent.FutureIterables.awaitCompletion;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jclouds.Constants;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.internal.BlobRuntimeException;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.blobstore.reference.BlobStoreConstants;
import org.jclouds.blobstore.strategy.ClearContainerStrategy;
import org.jclouds.blobstore.strategy.ClearListStrategy;
import org.jclouds.http.handlers.BackoffLimitedRetryHandler;
import org.jclouds.logging.Logger;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.inject.Inject;

/**
 * Deletes all keys in the container
 * 
 */
@Singleton
public class DeleteAllKeysInList implements ClearListStrategy, ClearContainerStrategy {
   @Resource
   @Named(BlobStoreConstants.BLOBSTORE_LOGGER)
   protected Logger logger = Logger.NULL;

   protected final BackoffLimitedRetryHandler retryHandler;
   private final ListeningExecutorService executorService;

   protected final BlobStore blobStore;

   /** Maximum duration in milliseconds of a request. */
   protected long maxTime = Long.MAX_VALUE;

   /** Maximum times to retry an operation. */
   protected int maxErrors = 3;

   @Inject
   DeleteAllKeysInList(@Named(Constants.PROPERTY_USER_THREADS) ListeningExecutorService executorService,
         BlobStore blobStore, BackoffLimitedRetryHandler retryHandler) {
      this.executorService = executorService;
      this.blobStore = blobStore;
      this.retryHandler = retryHandler;
   }

   @Inject(optional = true)
   void setMaxTime(@Named(Constants.PROPERTY_REQUEST_TIMEOUT) long maxTime) {
      this.maxTime = maxTime;
   }

   @Inject(optional = true)
   void setMaxErrors(@Named(Constants.PROPERTY_MAX_RETRIES) int maxErrors) {
      this.maxErrors = maxErrors;
   }

   @Override
   public void execute(String containerName) {
      this.execute(containerName, recursive());
   }

   @Override
   public void execute(final String containerName, ListContainerOptions options) {
      String message = options.getDir() != null ? String.format("clearing path %s/%s", containerName, options.getDir())
            : String.format("clearing container %s", containerName);
      options = options.clone();
      if (options.isRecursive()) {
         message += " recursively";
      }
      this.logger.debug(message);
      Map<StorageMetadata, Exception> exceptions = Maps.newHashMap();
      for (int numErrors = 0; numErrors < this.maxErrors;) {
         // fetch partial directory listing
         PageSet<? extends StorageMetadata> listing = this.blobStore.list(containerName, options);

         // recurse on subdirectories
         if (options.isRecursive()) {
            for (StorageMetadata md : listing) {
               String fullPath = this.parentIsFolder(options, md) ? options.getDir() + "/" + md.getName() : md
                     .getName();
               switch (md.getType()) {
                  case BLOB:
                     break;
                  case FOLDER:
                  case RELATIVE_PATH:
                     if (options.isRecursive() && !fullPath.equals(options.getDir())) {
                        this.execute(containerName, options.clone().inDirectory(fullPath));
                     }
                     break;
                  case CONTAINER:
                     throw new IllegalArgumentException("Container type not supported");
               }
            }
         }

         // remove blobs and now-empty subdirectories
         Map<StorageMetadata, ListenableFuture<?>> responses = Maps.newHashMap();
         for (final StorageMetadata md : listing) {
            final String fullPath = this.parentIsFolder(options, md) ? options.getDir() + "/" + md.getName() : md
                  .getName();
            switch (md.getType()) {
               case BLOB:
                  responses.put(md, this.executorService.submit(new Runnable() {
                     @Override
                     public void run() {
                        DeleteAllKeysInList.this.blobStore.removeBlob(containerName, fullPath);
                     }
                  }));
                  break;
               case FOLDER:
                  if (options.isRecursive()) {
                     responses.put(md, this.executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                           DeleteAllKeysInList.this.blobStore.deleteDirectory(containerName, fullPath);
                        }
                     }));
                  }
                  break;
               case RELATIVE_PATH:
                  if (options.isRecursive()) {
                     responses.put(md, this.executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                           DeleteAllKeysInList.this.blobStore.deleteDirectory(containerName, md.getName());
                        }
                     }));
                  }
                  break;
               case CONTAINER:
                  throw new IllegalArgumentException("Container type not supported");
            }
         }

         try {
            exceptions = awaitCompletion(responses, this.executorService, this.maxTime, this.logger, message);
         } catch (TimeoutException te) {
            ++numErrors;
            if (numErrors == this.maxErrors) {
               throw propagate(te);
            }
            this.retryHandler.imposeBackoffExponentialDelay(numErrors, message);
            continue;
         } finally {
            for (ListenableFuture<?> future : responses.values()) {
               future.cancel(true);
            }
         }

         if (!exceptions.isEmpty()) {
            ++numErrors;
            if (numErrors == this.maxErrors) {
               break;
            }
            this.retryHandler.imposeBackoffExponentialDelay(numErrors, message);
            continue;
         }

         String marker = listing.getNextMarker();
         if (marker == null) {
            break;
         }
         this.logger.debug("%s with marker %s", message, marker);
         options = options.afterMarker(marker);

         // Reset numErrors if we execute a successful iteration. This ensures
         // that we only try an unsuccessful operation maxErrors times but
         // allow progress with directories containing many blobs in the face
         // of some failures.
         numErrors = 0;
      }
      if (!exceptions.isEmpty()) {
         throw new BlobRuntimeException(String.format("error %s: %s", message, exceptions));
      }
   }

   private boolean parentIsFolder(final ListContainerOptions options, final StorageMetadata md) {
      return (options.getDir() != null) && (md.getName().indexOf('/') == -1);
   }
}
