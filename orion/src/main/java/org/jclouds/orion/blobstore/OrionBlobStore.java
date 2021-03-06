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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.internal.BaseBlobStore;
import org.jclouds.blobstore.options.CreateContainerOptions;
import org.jclouds.blobstore.options.GetOptions;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.blobstore.util.BlobUtils;
import org.jclouds.collect.Memoized;
import org.jclouds.domain.Location;
import org.jclouds.io.MutableContentMetadata;
import org.jclouds.orion.OrionApi;
import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.blobstore.functions.converters.BlobPropertiesToBlobMetadata;
import org.jclouds.orion.blobstore.functions.converters.BlobToOrionBlob;
import org.jclouds.orion.config.constans.OrionConstantValues;
import org.jclouds.orion.domain.BlobType;
import org.jclouds.orion.domain.OrionBlob;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;

/**
 * BlobStore API for Orion based back-ends
 *
 *
 */
public class OrionBlobStore extends BaseBlobStore {

   private final OrionApi api;
   private final BlobToOrionBlob blob2OrionBlob;
   private final BlobPropertiesToBlobMetadata blobProps2BlobMetadata;
   private final BlobUtils blobUtils;
   private final OrionUtils utils;

   @Inject
   protected OrionBlobStore(BlobStoreContext context, OrionUtils utils, BlobUtils blobUtils, OrionApi api,
         Supplier<Location> defaultLocation, @Memoized Supplier<Set<? extends Location>> locations,
         BlobToOrionBlob blob2OrionBlob, BlobPropertiesToBlobMetadata blobProps2BlobMetadata,
         OrionBlob.Factory orionBlobProvider) {
      super(context, blobUtils, defaultLocation, locations);
      this.blobUtils = blobUtils;
      this.api = Preconditions.checkNotNull(api, "api is null");
      this.utils = Preconditions.checkNotNull(utils, "utils is null");
      this.blob2OrionBlob = Preconditions.checkNotNull(blob2OrionBlob, "blob2OrionBlob is null");
      this.blobProps2BlobMetadata = Preconditions
            .checkNotNull(blobProps2BlobMetadata, "blobProps2BlobMetadata is null");

   }

   @Override
   public boolean blobExists(String container, String blobName) {
      return this.api.blobExists(this.getUserWorkspace(), container, OrionUtils.getParentPath(blobName),
            OrionUtils.getName(blobName));
   }

   @Override
   public BlobMetadata blobMetadata(String container, String blobName) {
      final String parentPath = OrionUtils.getParentPath(blobName);
      // Blob names must NOT start with a "/" since they are relative paths
      // they will be automatically removed in case it starts with that
      // Get the blob name
      // Convert the blob name to it's metadata file name and fetch it
      return this.blobProps2BlobMetadata.apply(this.api.getMetadata(this.getUserWorkspace(), container, parentPath,
            blobName));
   }

   @Override
   public boolean containerExists(String container) {
      return this.api.containerExists(this.getUserWorkspace(), container);
   }

   @Override
   public void deleteContainer(String container) {
      this.api.deleteContainerViaWorkspaceApi(this.getUserWorkspace(), container);
   }

   @Override
   public boolean createContainerInLocation(Location location, String container) {
      return this.api.createContainer(this.getUserWorkspace(), container);
   }

   @Override
   public boolean createContainerInLocation(Location arg0, String arg1, CreateContainerOptions arg2) {
      return this.createContainerInLocation(arg0, arg1);
   }

   @Override
   protected boolean deleteAndVerifyContainerGone(String container) {
      this.api.deleteContainerViaWorkspaceApi(this.getUserWorkspace(), container);
      return !containerExists(container);
   }

   @Override
   public Blob getBlob(String container, String blob, GetOptions arg2) {
      return this.api.getBlob(this.getUserWorkspace(), container, OrionUtils.getParentPath(blob),
            OrionUtils.getName(blob));
   }

   private String getUserWorkspace() {
      return getUtils().getUserWorkspace();
   }

   @Override
   public PageSet<? extends StorageMetadata> list() {
      return this.api.listContainers(this.getUserWorkspace());
   }

   @Override
   public PageSet<? extends StorageMetadata> list(String container, ListContainerOptions options) {
      return this.api.list(this.getUserWorkspace(), container, options);
   }

   @Override
   public String putBlob(String container, Blob blob) {
      final OrionBlob orionBlob = this.blob2OrionBlob.apply(blob);
      final MutableContentMetadata tempMD = orionBlob.getProperties().getContentMetadata();
      // Copy temporarily the inputstream otherwise JVM closes the stream
      final ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();
      try {
         ByteStreams.copy(blob.getPayload().openStream(), tempOutputStream);
         orionBlob.setPayload(tempOutputStream.toByteArray());
         orionBlob.getProperties().setContentMetadata(tempMD);
      } catch (final IOException e1) {
         e1.printStackTrace();
      }
      final ArrayList<String> pathList = Lists.newArrayList(orionBlob.getProperties().getParentPath()
            .split(OrionConstantValues.PATH_DELIMITER));
      this.createParentPaths(container, pathList);
      this.insertBlob(container, orionBlob);
      return null;
   }

   @Override
   public String putBlob(String container, Blob blob, PutOptions arg2) {
      return this.putBlob(container, blob);
   }

   @Override
   public void removeBlob(String container, String blobName) {
      this.api.removeBlob(this.getUserWorkspace(), container, OrionUtils.getParentPath(blobName),
            OrionUtils.getName(blobName));
   }

   /**
    * insert blob operations 1. create a blob file 2. put blob content 3. create
    * metadata
    *
    * @param container
    * @param orionBlob
    * @return
    */
   private void insertBlob(String container, OrionBlob orionBlob) {
      orionBlob.getProperties().setContainer(container);

      if (orionBlob.getProperties().getType() == BlobType.FILE_BLOB) {
         this.api.createBlob(this.getUserWorkspace(), container, orionBlob.getProperties().getParentPath(), orionBlob);
      } else if (orionBlob.getProperties().getType() == BlobType.FOLDER_BLOB) {
         this.api.createFolder(this.getUserWorkspace(), container, orionBlob.getProperties().getParentPath(), orionBlob
               .getProperties().getName());
      } else {
         System.err.println("blob could not be created. type is not supported! ");
      }

      if (!this.createMetadata(container, orionBlob)) {
         System.err.println("metadata could not be created blob will be removed");
         this.api.removeBlob(this.getUserWorkspace(), container, orionBlob.getProperties().getParentPath(), orionBlob
               .getProperties().getName());
      }

   }

   private boolean createMetadata(String container, OrionBlob blob) {

      if (OrionConstantValues.DEBUG_MODE) {
         final boolean res1 = this.api.createMetadataFolder(this.getUserWorkspace(), container, blob.getProperties()
               .getParentPath());
         final boolean res2 = this.api.createMetadata(this.getUserWorkspace(), container, blob.getProperties()
               .getParentPath(), blob);
         return res1 && res2;
      }
      return this.api.createMetadataFolder(this.getUserWorkspace(), container, blob.getProperties().getParentPath()) &&
      // Create metadata file
            this.api.createMetadata(this.getUserWorkspace(), container, blob.getProperties().getParentPath(), blob);
   }

   /**
    * Create the non existing paths starting from index 0
    *
    * @param containerName
    * @param pathArray
    */
   private void createParentPaths(String containerName, List<String> pathArray) {
      String parentPath = "";
      for (final String path : pathArray) {
         if (!this.api.blobExists(this.getUserWorkspace(), containerName, parentPath, path)) {
            this.insertBlob(
                  containerName,
                  this.blob2OrionBlob.apply(this.blobUtils.blobBuilder()
                        .payload(new ByteArrayInputStream("".getBytes())).name(parentPath + path)
                        .type(StorageType.FOLDER).build()));

         }
         parentPath = parentPath + path + OrionConstantValues.PATH_DELIMITER;
      }

   }

   public OrionUtils getUtils() {
      return this.utils;
   }

}
