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

import java.util.List;

import org.jclouds.blobstore.strategy.ClearContainerStrategy;
import org.jclouds.orion.OrionApi;
import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.config.constans.OrionConstantValues;
import org.jclouds.orion.domain.OrionChildMetadata;

import com.google.inject.Inject;

public class ClearFilesInContainer implements ClearContainerStrategy {

   private final OrionApi api;
   private final OrionUtils utils;

   @Inject
   public ClearFilesInContainer(OrionUtils utils, OrionApi api) {
      this.api = api;
      this.utils = utils;
   }

   @Override
   public void execute(String containerName) {
      List<OrionChildMetadata> children = this.api.listContainerContents(this.getUserWorkspace(), containerName);
      for (OrionChildMetadata childFile : children) {
         String childPath = childFile.getLocation();
         while (childPath.startsWith(OrionConstantValues.PATH_DELIMITER)) {
            childPath = childPath.replaceFirst(OrionConstantValues.PATH_DELIMITER, "");
         }
         this.api.deleteGivenPath(childPath);
      }
   }

   private String getUserWorkspace() {
      return getUtils().getUserWorkspace();
   }

   public OrionUtils getUtils() {
      return this.utils;
   }

}
