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
package org.jclouds.orion;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.jclouds.domain.Credentials;
import org.jclouds.location.Provider;
import org.jclouds.orion.config.constans.OrionConstantValues;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.inject.Inject;

/**
 * Utilities for Orion blob store
 *
 *
 */
public class OrionUtils {

   private final Supplier<Credentials> credsSupplier;

   @Inject
   public OrionUtils(@Provider Supplier<Credentials> creds) {
      this.credsSupplier = creds;
   }

   /**
    * Removes the last element which is the name of the blob for instance
    * /path1/path2/blobname/ -> path1/path2/ The first slash is removed since
    * the paths are relative
    *
    * @param blobName
    * @return
    */
   public static String getParentPath(String blobName) {
      Preconditions.checkNotNull(blobName, "blobname is null");
      String fetchedParent = "";
      final String[] blobPaths = blobName.split(OrionConstantValues.PATH_DELIMITER);
      for (int index = 0; index < (blobPaths.length - 1); index++) {
         if (!blobPaths[index].isEmpty()) {
            fetchedParent = fetchedParent + blobPaths[index] + OrionConstantValues.PATH_DELIMITER;
         }
      }
      return fetchedParent;
   }

   /**
    * Convert blobName to an hashed unique ID. This this unique id used to store
    * metadata information. SHA-256 hashing is used to generate unique id.
    *
    * @param blobName
    * @return
    */
   public static String getMetadataName(String blobName) {
      MessageDigest messageDigest;
      try {
         messageDigest = MessageDigest.getInstance("SHA-256");
         messageDigest.update(blobName.getBytes(OrionConstantValues.ENCODING));
         final byte[] digest = messageDigest.digest();
         final BigInteger bigInteger = new BigInteger(1, digest);
         return bigInteger.toString(16);
      } catch (final NoSuchAlgorithmException e) {
         e.printStackTrace();
         return String.valueOf(blobName.hashCode());
      } catch (final UnsupportedEncodingException e) {
         e.printStackTrace();
         return String.valueOf(blobName.hashCode());
      }

   }

   /**
    * Gets the name of passed name by extracting the parent paths
    *
    * @param originalName
    * @return
    */
   public static String getName(String originalName) {
      final String parentPath = OrionUtils.getParentPath(originalName);
      return originalName.replaceFirst(parentPath, "").replaceAll(OrionConstantValues.PATH_DELIMITER, "");
   }

   /**
    * Gets the name of passed name by extracting the parent paths and encode it
    * once since it will be used as path
    *
    * @param originalName
    * @return
    */
   public static String getNamePath(String originalName) {
      final String parentPath = OrionUtils.getParentPath(originalName);
      return encodeName(originalName.replaceFirst(parentPath, "").replaceAll(OrionConstantValues.PATH_DELIMITER, ""));
   }

   /**
    *
    *
    * @param parentPath
    * @return
    */
   public static String getParentRequestLocation(String parentPath) {
      Preconditions.checkNotNull(parentPath, "blobname is null");
      String requestParent = "";

      for (final String path : parentPath.split(OrionConstantValues.PATH_DELIMITER)) {
         if (!path.isEmpty()) {
            requestParent = requestParent + OrionUtils.getRequestLocation(path) + OrionConstantValues.PATH_DELIMITER;
         }
      }
      return requestParent;
   }

   /**
    * Locations are encoded one more time
    *
    * @param createdName
    *           Gets orion based name ,i.e., all parent paths have been removed
    * @return
    */
   public static String getRequestLocation(String createdName) {
      return OrionUtils.encodeName(createdName);
   }

   /**
    *
    * Check if the given path is a container path. If it is a container path
    * there should exist {file}/{userWorkspace}/{containerName} so the length of
    * the path should be 3
    *
    * @param path
    * @return
    */
   public static boolean isContainerFromPath(String path) {
      final String[] paths = path.split(OrionConstantValues.PATH_DELIMITER);
      int nonEmptyLength = 0;
      for (final String pathPart : paths) {
         if (!pathPart.equals("")) {
            nonEmptyLength++;
         }
      }
      if (nonEmptyLength == 3) {
         return true;
      } else {
         return false;
      }
   }

   /**
    * Create a name for the user from the path by removing/file then
    * userWorkspace and finally the container name This is achieved by removing
    * first 3 strings.
    *
    * @param path
    * @return
    */
   public static String createOriginalNameFromLocation(String path) {
      // decode once because names are decoded one time to create the location
      // names
      path = OrionUtils.decodeName(path);
      final String[] paths = path.split(OrionConstantValues.PATH_DELIMITER);
      int index = 0;
      String originalName = "";
      for (final String pathPart : paths) {

         if (index > 3) {
            originalName = originalName + pathPart + OrionConstantValues.PATH_DELIMITER;
         }
         index++;
      }
      // remove the last slash
      // last field is the name of the file
      originalName = originalName.substring(0, originalName.length() - 1);
      return originalName;
   }

   /**
    * Create container name which removes the first 2 elements and then returns
    * the file name
    *
    * @param path
    * @return
    */
   public static String createContainerName(String path) {
      // decode once because names are decoded one time to create the location
      // names
      path = OrionUtils.decodeName(path);
      final String[] paths = path.split(OrionConstantValues.PATH_DELIMITER);
      int index = 0;
      String originalName = "";
      for (final String pathPart : paths) {

         if (index > 2) {
            originalName = originalName + pathPart + OrionConstantValues.PATH_DELIMITER;
            break;
         }
         index++;
      }
      // remove the last slash
      // last field is the name of the file
      originalName = originalName.substring(0, originalName.length() - 1);
      return originalName;
   }

   /**
    *
    * @param createdName
    * @return
    */
   private static String decodeName(String createdName) {

      try {
         return URLDecoder.decode(createdName, "UTF-8");
      } catch (final UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      return createdName;
   }

   /**
    *
    * @param createdName
    * @return
    */
   private static String encodeName(String createdName) {

      try {
         return URLEncoder.encode(URLEncoder.encode(createdName, "UTF-8"), "UTF-8");
      } catch (final UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      return createdName;
   }

   /**
    * @param path
    * @return
    */
   public static String convert2RelativePath(String path) {
      while (!path.startsWith(OrionConstantValues.PATH_DELIMITER)) {
         path = path.replaceFirst(OrionConstantValues.PATH_DELIMITER, "");
      }
      return path;

   }

   /**
    * @param name
    * @return Slug One time encoding is applied by this method to provide
    */
   public static String convertNameToSlug(String name) {
      return encodeName(name);
   }

   private String getIdentity() {
      return this.credsSupplier.get().identity;
   }

   public String getUserWorkspace() {
      return getIdentity() + OrionConstantValues.ORION_USER_CONTENT_ENDING;
   }

}
