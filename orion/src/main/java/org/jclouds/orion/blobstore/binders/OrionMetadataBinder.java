/*******************************************************************************
 * Copyright (c) 2013 University of Stuttgart.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and the Apache License 2.0 which both accompany this distribution,
 * and are available at http://www.eclipse.org/legal/epl-v10.html
 * and http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *    Timur Sungur - initial API and implementation
 *******************************************************************************/

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
