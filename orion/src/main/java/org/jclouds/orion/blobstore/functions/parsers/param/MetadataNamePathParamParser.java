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

import com.google.common.base.Function;

/**
 * Encodes blob name, used in case if the name will be used in a path
 * 
 * @author timur
 * 
 */
public class MetadataNamePathParamParser implements Function<Object, String> {

   @Override
   public String apply(Object blobName) {

      return OrionUtils.getMetadataName(OrionUtils.getNamePath(((String) blobName)));
   }
}
