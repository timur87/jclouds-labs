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

import org.jclouds.http.HttpRequest;
import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.config.constans.OrionHttpFields;
import org.jclouds.rest.Binder;

/**
 * Add Slug header to the request from String object
 * 
 * @author timur
 * 
 */
public class SlugHeaderFromString implements Binder {

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object name) {
      HttpRequest req = request.toBuilder()
            .replaceHeader(OrionHttpFields.HEADER_SLUG, OrionUtils.convertNameToSlug((String) name)).build();
      return (R) req;
   }

}
