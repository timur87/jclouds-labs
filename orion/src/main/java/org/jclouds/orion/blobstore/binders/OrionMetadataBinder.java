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

package org.jclouds.orion.blobstore.binders;

import java.util.Calendar;
import java.util.Date;

import org.jclouds.http.HttpRequest;
import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.config.constans.OrionHttpFields;
import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.OrionBlob;
import org.jclouds.rest.Binder;

import com.google.inject.Inject;

/**
 * Add header Slug, lastModified time stamp and set payload as json
 * 
 * @author Timur
 * 
 */
public class OrionMetadataBinder implements Binder {

   private final JSONUtils jsonConverter;

   @Inject
   public OrionMetadataBinder(JSONUtils jsonConverter) {
      this.jsonConverter = jsonConverter;
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      OrionBlob blob = OrionBlob.class.cast(input);
      Date date = Calendar.getInstance().getTime();
      blob.getProperties().setLastModified(date);

      request = (R) request
            .toBuilder()
            .replaceHeader(OrionHttpFields.HEADER_SLUG,
                  OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(blob.getProperties().getName()))).build();

      request.setPayload(this.jsonConverter.getObjectAsString(blob.getProperties()));

      return request;
   }
}
