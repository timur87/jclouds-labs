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
 * Orion file properties representation
 * 
 * */
public interface OrionSpecificFileMetadata {

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
    * @return the directory
    */
   Boolean getDirectory();

   /**
    * @param directory
    *           the directory to set
    */
   void setDirectory(Boolean directory);

   /**
    * @return the eTag
    */
   String geteTag();

   /**
    * @param eTag
    *           the eTag to set
    */
   void seteTag(String eTag);

   /**
    * @return the localTimeStamp
    */
   Long getLocalTimeStamp();

   /**
    * @param localTimeStamp
    *           the localTimeStamp to set
    */
   void setLocalTimeStamp(Long localTimeStamp);

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
    * @return the childrenLocation
    */
   String getChildrenLocation();

   /**
    * @param childrenLocation
    *           the childrenLocation to set
    */
   void setChildrenLocation(String childrenLocation);

   /**
    * @return the attributes
    */
   Attributes getAttributes();

   /**
    * @param attributes
    *           the attributes to set
    */
   void setAttributes(Attributes attributes);

   /**
    * @return the charSet
    */
   String getCharSet();

   /**
    * @param charSet
    *           the charSet to set
    */
   void setCharSet(String charSet);

   /**
    * @return the contentType
    */
   String getContentType();

   /**
    * @param contentType
    *           the contentType to set
    */
   void setContentType(String contentType);

   /**
    * @return the contentLegth
    */
   Long getContentLegth();

   /**
    * @param contentLegth
    *           the contentLegth to set
    */
   void setContentLegth(Long contentLegth);

}