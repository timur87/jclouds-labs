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
package org.jclouds.orion.http.filters.create;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.orion.blobstore.functions.converters.JSONToOrionSpecificObject;
import org.jclouds.orion.blobstore.functions.converters.OrionSpecificObjectToJSON;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;

/**
 * Make ReadOnly attribute of file attributes true in the request
 * 
 * @author Timur
 * 
 */
public class CreateReadonlyFileFilter implements HttpRequestFilter {

   private final JSONToOrionSpecificObject json2OrionSpecificObj;
   private final OrionSpecificObjectToJSON orionSpecificObject2JSON;

   @Inject
   public CreateReadonlyFileFilter(OrionSpecificFileMetadata metadata, JSONToOrionSpecificObject json2OrionSpecificObj,
         OrionSpecificObjectToJSON orionSpecificObject2JSON) {
      this.json2OrionSpecificObj = Preconditions.checkNotNull(json2OrionSpecificObj, "json2OrionSpecificObjis null");
      this.orionSpecificObject2JSON = Preconditions.checkNotNull(orionSpecificObject2JSON,
            "orionSpecificObject2JSON is null");
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.http.HttpRequestFilter#filter(org.jclouds.http.HttpRequest )
    */
   @Override
   public HttpRequest filter(HttpRequest request) throws HttpException {
      OrionSpecificFileMetadata metadata;
      try {
         metadata = this.json2OrionSpecificObj.apply(CharStreams
               .toString(new InputStreamReader(request.getPayload()
                     .openStream(), Charsets.UTF_8)));
         metadata.getAttributes().setReadOnly(true);
         String updatedContent = this.orionSpecificObject2JSON.apply(metadata);
         request = request
               .toBuilder()
               .payload(
                     new ByteArrayInputStream(
                           updatedContent.getBytes())).build();
         //update content length
         request.getPayload().getContentMetadata().setContentLength((long) updatedContent.length());
      } catch (IOException e) {
         System.err.println(getClass().getCanonicalName()
               + ": Payload could not be converted to string");
         e.printStackTrace();
      }
      return request;
   }
}