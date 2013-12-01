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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Check if the received object is folder
 * 
 * @author Timur
 * 
 */
public class FolderMetadataResponseParser implements Function<HttpResponse, Boolean> {

   @Override
   public Boolean apply(HttpResponse res) {
      if (res.getStatusLine().startsWith("4")) {
         return false;
      } else if (res.getStatusLine().startsWith("2")) {
         JsonObject responseObject = (JsonObject) (new JsonParser()).parse(res.getMessage());
         if (responseObject.get("Directory").getAsBoolean()) {
            return true;
         }
      }
      return false;
   }

}
