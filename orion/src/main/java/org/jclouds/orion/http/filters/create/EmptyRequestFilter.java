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

import javax.ws.rs.core.MediaType;

import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.orion.blobstore.functions.converters.OrionSpecificObjectToJSON;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * This class should be present before providing remaining filters
 * 
 * @author timur
 * 
 */
public class EmptyRequestFilter implements HttpRequestFilter {

   private final OrionSpecificFileMetadata metadata;
   private final OrionSpecificObjectToJSON orionSpecificObject2JSON;

   @Inject
   public EmptyRequestFilter(OrionSpecificFileMetadata metadata, OrionSpecificObjectToJSON orionSpecificObject2JSON) {
      this.metadata = Preconditions.checkNotNull(metadata, "metadata is null");
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
   public HttpRequest filter(HttpRequest req) throws HttpException {

      req = req.toBuilder().payload(new ByteArrayInputStream(this.orionSpecificObject2JSON.apply(this.metadata).getBytes())).build();
      req.getPayload().getContentMetadata().setContentType(MediaType.APPLICATION_JSON);
      return req;
   }

}