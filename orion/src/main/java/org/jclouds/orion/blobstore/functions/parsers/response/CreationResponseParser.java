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
 * Checks if the return response to file creation is positive
 * 
 * @author Timur
 * 
 */
public class CreationResponseParser implements Function<HttpResponse, Boolean> {

   @Override
   public Boolean apply(HttpResponse response) {
      if ((response.getStatusCode() == 200) || (response.getStatusCode() == 201)) {
         return true;
      } else {
         return false;
      }
   }

}
