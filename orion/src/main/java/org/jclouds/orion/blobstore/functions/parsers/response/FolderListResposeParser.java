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
import java.util.List;

import org.jclouds.http.HttpResponse;
import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.OrionChildMetadata;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteSource;
import com.google.inject.Inject;

/**
 * Convert received HttpResponse to {@link OrionChildMetadata} for further
 * processing
 * 
 *
 */
public class FolderListResposeParser implements Function<HttpResponse, List<OrionChildMetadata>> {

   private final JSONUtils jsonUtils;

   @Inject
   public FolderListResposeParser(JSONUtils mapper) {
      this.jsonUtils = Preconditions.checkNotNull(mapper, "mapper is null");

   }

   /*
    * (non-Javadoc)
    * 
    * @see com.google.common.base.Function#apply(java.lang.Object)
    */
   @Override
   public List<OrionChildMetadata> apply(final HttpResponse res) {
      ByteSource source = new ByteSource() {

         @Override
         public InputStream openStream() throws IOException {
            return res.getPayload().openStream();
         }
      };
      try {
         return this.jsonUtils.fetchFileObjects(source.asCharSource(Charsets.UTF_8).read());
      } catch (IOException e) {
         e.printStackTrace();
      }
      return null;


   }
}
