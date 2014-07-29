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
   public abstract String getName();

   /**
    * @param name
    *           the name to set
    */
   public abstract void setName(String name);

   /**
    * @return the directory
    */
   public abstract Boolean getDirectory();

   /**
    * @param directory
    *           the directory to set
    */
   public abstract void setDirectory(Boolean directory);

   /**
    * @return the eTag
    */
   public abstract String geteTag();

   /**
    * @param eTag
    *           the eTag to set
    */
   public abstract void seteTag(String eTag);

   /**
    * @return the localTimeStamp
    */
   public abstract Long getLocalTimeStamp();

   /**
    * @param localTimeStamp
    *           the localTimeStamp to set
    */
   public abstract void setLocalTimeStamp(Long localTimeStamp);

   /**
    * @return the location
    */
   public abstract String getLocation();

   /**
    * @param location
    *           the location to set
    */
   public abstract void setLocation(String location);

   /**
    * @return the childrenLocation
    */
   public abstract String getChildrenLocation();

   /**
    * @param childrenLocation
    *           the childrenLocation to set
    */
   public abstract void setChildrenLocation(String childrenLocation);

   /**
    * @return the attributes
    */
   public abstract Attributes getAttributes();

   /**
    * @param attributes
    *           the attributes to set
    */
   public abstract void setAttributes(Attributes attributes);

   /**
    * @return the charSet
    */
   public abstract String getCharSet();

   /**
    * @param charSet
    *           the charSet to set
    */
   public abstract void setCharSet(String charSet);

   /**
    * @return the contentType
    */
   public abstract String getContentType();

   /**
    * @param contentType
    *           the contentType to set
    */
   public abstract void setContentType(String contentType);

   /**
    * @return the contentLegth
    */
   public abstract Long getContentLegth();

   /**
    * @param contentLegth
    *           the contentLegth to set
    */
   public abstract void setContentLegth(Long contentLegth);

}