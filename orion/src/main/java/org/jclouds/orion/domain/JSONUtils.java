/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
