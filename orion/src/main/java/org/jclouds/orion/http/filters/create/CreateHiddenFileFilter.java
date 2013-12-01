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

package org.jclouds.orion.http.filters.create;

import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.orion.blobstore.functions.converters.JSONToOrionSpecificObject;
import org.jclouds.orion.blobstore.functions.converters.OrionSpecificObjectToJSON;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * Make Hidden attribute of file attributes true in the request
 * 
 * @author Timur
 * 
 */
public class CreateHiddenFileFilter implements HttpRequestFilter {

   private final JSONToOrionSpecificObject json2OrionSpecificObj;
   private final OrionSpecificObjectToJSON orionSpecificObject2JSON;

   @Inject
   public CreateHiddenFileFilter(OrionSpecificFileMetadata metadata, JSONToOrionSpecificObject json2OrionSpecificObj,
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
      metadata = this.json2OrionSpecificObj.apply((String) request.getPayload().getRawContent());
      metadata.getAttributes().setHidden(true);
      request = request.toBuilder().payload(this.orionSpecificObject2JSON.apply(metadata)).build();
      return request;
   }

}