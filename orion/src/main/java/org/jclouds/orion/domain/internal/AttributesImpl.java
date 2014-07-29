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

import org.jclouds.orion.domain.Attributes;

import com.google.gson.annotations.SerializedName;

/**
 * @see Attributes
 *
 */
public class AttributesImpl implements Attributes {
   @SerializedName("ReadOnly")
   Boolean readOnly = false;
   @SerializedName("Exectuable")
   Boolean executable = false;
   @SerializedName("Hidden")
   Boolean hidden = false;
   @SerializedName("Archive")
   Boolean archive = false;
   @SerializedName("SymbolicLink")
   Boolean symbolicLink = false;

   /**
    * @return the readOnly
    */
   @Override
   public Boolean getReadOnly() {
      return this.readOnly;
   }

   /**
    * @param readOnly
    *           the readOnly to set
    */
   @Override
   public void setReadOnly(Boolean readOnly) {
      this.readOnly = readOnly;
   }

   /**
    * @return the executable
    */
   @Override
   public Boolean getExecutable() {
      return this.executable;
   }

   /**
    * @param executable
    *           the executable to set
    */
   @Override
   public void setExecutable(Boolean executable) {
      this.executable = executable;
   }

   /**
    * @return the hidden
    */
   @Override
   public Boolean getHidden() {
      return this.hidden;
   }

   /**
    * @param hidden
    *           the hidden to set
    */
   @Override
   public void setHidden(Boolean hidden) {
      this.hidden = hidden;
   }

   /**
    * @return the archive
    */
   @Override
   public Boolean getArchive() {
      return this.archive;
   }

   /**
    * @param archive
    *           the archive to set
    */
   @Override
   public void setArchive(Boolean archive) {
      this.archive = archive;
   }

   /**
    * @return the symbolicLink
    */
   @Override
   public Boolean getSymbolicLink() {
      return this.symbolicLink;
   }

   /**
    * @param symbolicLink
    *           the symbolicLink to set
    */
   @Override
   public void setSymbolicLink(Boolean symbolicLink) {
      this.symbolicLink = symbolicLink;
   }

}
