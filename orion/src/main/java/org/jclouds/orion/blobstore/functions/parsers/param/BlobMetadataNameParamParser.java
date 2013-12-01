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

package org.jclouds.orion.blobstore.functions.parsers.param;

import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.domain.OrionBlob;

import com.google.common.base.Function;

/**
 * Fetch the file name from OrionBlob, convert it to metadata and name encode it
 * once.
 * 
 * @author timur
 * 
 */
public class BlobMetadataNameParamParser implements Function<Object, String> {

   @Override
   public String apply(Object blob) {
      return OrionUtils.getNamePath(OrionUtils.getMetadataName(OrionBlob.class.cast(blob).getProperties().getName()));
   }

}
