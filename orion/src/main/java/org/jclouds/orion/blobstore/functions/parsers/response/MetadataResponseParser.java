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

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.jclouds.http.HttpResponse;
import org.jclouds.logging.Logger;
import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.MutableBlobProperties;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.io.ByteSource;
import com.google.inject.Inject;

/**
 * Parse metadata response of a blob
 *
 *
 */
public class MetadataResponseParser implements Function<HttpResponse, MutableBlobProperties> {
   @Resource
   Logger logger = Logger.CONSOLE;
   private final JSONUtils jsonConverter;

   @Inject
   public MetadataResponseParser(JSONUtils jsonConverter) {
      this.jsonConverter = jsonConverter;
   }

   @Override
   public MutableBlobProperties apply(final HttpResponse response) {

      MutableBlobProperties properties = null;
      try {
         final ByteSource source = new ByteSource() {

            @Override
            public InputStream openStream() throws IOException {
               return response.getPayload().openStream();
            }
         };
         properties = this.jsonConverter.getStringAsObject(source.asCharSource(Charsets.UTF_8).read(),
               MutableBlobProperties.class);
      } catch (final IOException e) {
         this.logger.error(response.getMessage(), response);
      }

      return properties;
   }
}
