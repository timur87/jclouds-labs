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

package org.jclouds.orion.blobstore.functions.converters;

import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * A function to de-serialize {@link OrionSpecificFileMetadata} from a JSON
 * String
 * 
 * @author Timur
 * 
 */
public class JSONToOrionSpecificObject implements Function<String, OrionSpecificFileMetadata> {

   private final JSONUtils jsonConverter;

   @Inject
   public JSONToOrionSpecificObject(JSONUtils mapper) {
      this.jsonConverter = Preconditions.checkNotNull(mapper, "mapper is null");
   }

   @Override
   public OrionSpecificFileMetadata apply(String jsonStr) {
      return this.jsonConverter.getStringAsObject(jsonStr, OrionSpecificFileMetadata.class);
   }
}
