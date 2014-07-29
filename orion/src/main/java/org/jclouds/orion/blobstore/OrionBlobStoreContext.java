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

package org.jclouds.orion.blobstore;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.inject.Inject;

import org.jclouds.Context;
import org.jclouds.blobstore.AsyncBlobStore;
import org.jclouds.blobstore.BlobRequestSigner;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.attr.ConsistencyModel;
import org.jclouds.internal.BaseView;
import org.jclouds.location.Provider;
import org.jclouds.rest.Utils;

import com.google.common.io.Closeables;
import com.google.common.reflect.TypeToken;

public class OrionBlobStoreContext extends BaseView implements BlobStoreContext {

   private final BlobStore blobStore;
   private final ConsistencyModel consistencyModel;
   private final Utils utils;
   private final BlobRequestSigner blobRequestSigner;

   @Inject
   public OrionBlobStoreContext(@Provider Context backend, @Provider TypeToken<? extends Context> backendType,
         Utils utils, ConsistencyModel consistencyModel, BlobStore blobStore, BlobRequestSigner blobRequestSigner) {

      super(backend, backendType);
      this.consistencyModel = checkNotNull(consistencyModel, "consistencyModel");
      this.blobStore = checkNotNull(blobStore, "blobStore");
      this.utils = checkNotNull(utils, "utils");
      this.blobRequestSigner = checkNotNull(blobRequestSigner, "blobRequestSigner");
   }

   @Override
   public ConsistencyModel getConsistencyModel() {
      return this.consistencyModel;
   }

   @Override
   public BlobStore getBlobStore() {
      return this.blobStore;
   }

   @Override
   @Deprecated
   public AsyncBlobStore getAsyncBlobStore() {
      return null;
   }

   @Override
   public Utils utils() {
      return this.utils;
   }

   @Override
   public BlobRequestSigner getSigner() {
      return this.blobRequestSigner;
   }

   @Override
   public void close() {
      try {
         Closeables.close(delegate(), true);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   public int hashCode() {
      return this.delegate().hashCode();
   }

   @Override
   public String toString() {
      return this.delegate().toString();
   }

   @Override
   public boolean equals(Object obj) {
      return this.delegate().equals(obj);
   }

}
