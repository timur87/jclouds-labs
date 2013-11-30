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
package org.jclouds.orion.domain.internal;

import org.jclouds.orion.domain.OrionChildMetadata;

import com.google.gson.annotations.SerializedName;

/**
 * @see OrionChildMetadata
 * @author timur
 * 
 */

public class OrionChildMetadataImpl implements OrionChildMetadata {
   @SerializedName("ChildrenLocation")
   private String childrenLocation;
   @SerializedName("Directory")
   boolean directory;
   @SerializedName("Id")
   private String id;
   @SerializedName("ImportLocation")
   private String importLocation;
   @SerializedName("Location")
   private String location;
   @SerializedName("Name")
   private String name;
   @SerializedName("LocalTimeStamp")
   private Long localTimeStamp;
   @SerializedName("Length")
   private Long length;

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#getChildrenLocation()
    */
   @Override
   public String getChildrenLocation() {
      return this.childrenLocation;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#setChildrenLocation(java.lang
    * .String)
    */
   @Override
   public void setChildrenLocation(String childrenLocation) {
      this.childrenLocation = childrenLocation;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#getDirectory()
    */
   @Override
   public boolean isDirectory() {
      return this.directory;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.orion.domain.internal.Child#setDirectory(java.lang.String)
    */
   @Override
   public void setDirectory(boolean directory) {
      this.directory = directory;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#getId()
    */
   @Override
   public String getId() {
      return this.id;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#setId(java.lang.String)
    */
   @Override
   public void setId(String id) {
      this.id = id;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#getImportLocation()
    */
   @Override
   public String getImportLocation() {
      return this.importLocation;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.orion.domain.internal.Child#setImportLocation(java.lang.String
    * )
    */
   @Override
   public void setImportLocation(String importLocation) {
      this.importLocation = importLocation;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#getLocation()
    */
   @Override
   public String getLocation() {
      return this.location;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#setLocation(java.lang.String)
    */
   @Override
   public void setLocation(String location) {
      this.location = location;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#getName()
    */
   @Override
   public String getName() {
      return this.name;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.Child#setName(java.lang.String)
    */
   @Override
   public void setName(String name) {
      this.name = name;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.OrionChildMetadata#getLength()
    */
   @Override
   public Long getLength() {
      return this.length;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.OrionChildMetadata#setLength(int)
    */
   @Override
   public void setLength(Long length) {
      this.length = length;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.OrionChildMetadata#getLocalTimeStamp()
    */
   @Override
   public Long getLocalTimeStamp() {
      return this.localTimeStamp;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.OrionChildMetadata#setLocalTimeStamp(int)
    */
   @Override
   public void setLocalTimeStamp(Long localTimeStamp) {
      this.localTimeStamp = localTimeStamp;

   }

}
