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

package org.jclouds.orion.domain;

import java.util.List;

import org.jclouds.orion.domain.internal.JSONUtilsImpl;

import com.google.inject.ImplementedBy;

/**
 * JSON processing utilities for Orion domain objects
 * 
 * @author timur
 * 
 */
@ImplementedBy(JSONUtilsImpl.class)
public interface JSONUtils {
   /**
    * Return passed object in json string
    * 
    * @param obj
    * @return
    */
   <T> String getObjectAsString(T obj);

   /**
    * Create the object out of json string
    * 
    * @param str
    * @param t
    * @return
    */
   <T> T getStringAsObject(String str, Class<T> t);

   /**
    * Fetch the list of container from a response json string and return them as
    * OrionChildMetada
    * 
    * @param string
    *           Response message from Orion to file list query
    * @return
    */
   List<OrionChildMetadata> fetchContainerObjects(String string);

   /**
    * Return list of files from a response string
    * 
    * @param string
    *           Response message from Orion to file list query
    * @return
    */
   List<OrionChildMetadata> fetchFileObjects(String string);

}
