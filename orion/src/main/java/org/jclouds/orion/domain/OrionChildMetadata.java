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

/**
 * List operation on Orion returns an object with an array of children objects.
 * A child node in this array represented by this object.
 *
 *
 */

public interface OrionChildMetadata {

   /**
    * @return the childrenLocation
    */
   String getChildrenLocation();

   /**
    * @param childrenLocation
    *           the childrenLocation to set
    */
   void setChildrenLocation(String childrenLocation);

   /**
    * @return the directory
    */
   boolean isDirectory();

   /**
    * @param directory
    *           the directory to set
    */
   void setDirectory(boolean directory);

   /**
    * @return the id
    */
   String getId();

   /**
    * @param id
    *           the id to set
    */
   void setId(String id);

   /**
    * @return the importLocation
    */
   String getImportLocation();

   /**
    * @param importLocation
    *           the importLocation to set
    */
   void setImportLocation(String importLocation);

   /**
    * @return the location
    */
   String getLocation();

   /**
    * @param location
    *           the location to set
    */
   void setLocation(String location);

   /**
    * @return the name
    */
   String getName();

   /**
    * @param name
    *           the name to set
    */
   void setName(String name);

   /**
    * @return the length
    */
   Long getLength();

   /**
    * @param length
    *           the length to set
    */
   void setLength(Long length);

   /**
    * @return the localTimeStamp
    */
   Long getLocalTimeStamp();

   /**
    * @param localTimeStamp
    *           the localTimeStamp to set
    */
   void setLocalTimeStamp(Long localTimeStamp);

}
