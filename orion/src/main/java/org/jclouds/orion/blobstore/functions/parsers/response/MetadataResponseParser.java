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

import java.io.IOException;

import org.jclouds.http.HttpResponse;
import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.MutableBlobProperties;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;

/**
 * Parse metadata response of a blob
 * 
 * @author Timur
 * 
 */
public class MetadataResponseParser implements Function<HttpResponse, MutableBlobProperties> {

   private final JSONUtils jsonConverter;

   @Inject
   public MetadataResponseParser(JSONUtils jsonConverter) {
      this.jsonConverter = jsonConverter;
   }

   @Override
   public MutableBlobProperties apply(HttpResponse response) {

      MutableBlobProperties properties = null;
      try {
         String theString = CharStreams.toString(CharStreams.newReaderSupplier(
               ByteStreams.newInputStreamSupplier(ByteStreams.toByteArray(response.getPayload().getInput())),
               Charsets.UTF_8));
         properties = this.jsonConverter.getStringAsObject(theString, MutableBlobProperties.class);
      } catch (IOException e) {
         System.out.println(response.getMessage());
         e.printStackTrace();
      }

      return properties;
   }
}
