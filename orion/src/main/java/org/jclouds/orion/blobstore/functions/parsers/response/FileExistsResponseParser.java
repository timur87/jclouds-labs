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

package org.jclouds.orion.blobstore.functions.parsers.response;

import org.jclouds.http.HttpResponse;

import com.google.common.base.Function;

/**
 * Check if the response is positive to file request
 * 
 *
 */
public class FileExistsResponseParser implements Function<HttpResponse, Boolean> {

   @Override
   public Boolean apply(HttpResponse response) {
      if (String.valueOf(response.getStatusCode()).startsWith("2")) {
         return true;
      }
      return false;
   }

}
