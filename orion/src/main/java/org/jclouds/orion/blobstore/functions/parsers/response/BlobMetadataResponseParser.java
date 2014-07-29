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
package org.jclouds.orion.blobstore.functions.parsers.response;

/**
 * Parses response to a blob metadata request and after parsing {@link MutableBlobProperties}
 * a new request is done to fetch the actual blob
 * *
 */

import java.io.IOException;
import java.io.InputStream;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.http.HttpResponse;
import org.jclouds.orion.OrionApi;
import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.blobstore.functions.converters.OrionBlobToBlob;
import org.jclouds.orion.domain.BlobType;
import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.MutableBlobProperties;
import org.jclouds.orion.domain.OrionBlob;
import org.jclouds.orion.domain.OrionBlob.Factory;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import com.google.inject.Inject;

public class BlobMetadataResponseParser implements Function<HttpResponse, Blob> {

   private final OrionApi api;
   private final Factory orionBlobProvider;
   private final OrionBlobToBlob orionBlob2Blob;
   private final JSONUtils jsonConverter;
   private final OrionUtils utils;

   @Inject
   public BlobMetadataResponseParser(JSONUtils jsonConverter, OrionApi api, OrionUtils utils,
         OrionBlob.Factory orionBlobProvider, OrionBlobToBlob orionBlob2Blob) {
      this.jsonConverter = Preconditions.checkNotNull(jsonConverter, "mapper is null");
      this.api = Preconditions.checkNotNull(api, "api is null");
      this.utils = utils;
      this.orionBlobProvider = Preconditions.checkNotNull(orionBlobProvider, "orionBlobProvider is null");
      this.orionBlob2Blob = Preconditions.checkNotNull(orionBlob2Blob, "orionBlob2Blob is null");

   }

   @Override
   public Blob apply(final HttpResponse response) {
      MutableBlobProperties properties = null;
      try {
         ByteSource source = new ByteSource() {

            @Override
            public InputStream openStream() throws IOException {
               return response.getPayload().openStream();
            }
         };
         properties = this.jsonConverter.getStringAsObject(source.asCharSource(Charsets.UTF_8).read(), MutableBlobProperties.class);
         OrionBlob orionBlob = this.orionBlobProvider.create(properties);
         if (properties.getType() == BlobType.FILE_BLOB) {
            HttpResponse payloadRes = this.api.getBlobContents(this.getUserWorkspace(), properties.getContainer(),
                  properties.getParentPath(), properties.getName());
            orionBlob.setPayload(payloadRes.getPayload());
         }
         return this.orionBlob2Blob.apply(orionBlob);

      } catch (IOException e) {
         System.out.println(response.getMessage());
         e.printStackTrace();
      }

      return null;
   }

   /**
    * @return
    */
   private String getUserWorkspace() {
      return getUtils().getUserWorkspace();
   }

   public OrionUtils getUtils() {
      return this.utils;
   }
}