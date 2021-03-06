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
 * File object attributes
 * 
 *
 */
public interface Attributes {

   /**
    * @return the readOnly
    */
   Boolean getReadOnly();

   /**
    * @param readOnly
    *           the readOnly to set
    */
   void setReadOnly(Boolean readOnly);

   /**
    * @return the executable
    */
   Boolean getExecutable();

   /**
    * @param executable
    *           the executable to set
    */
   void setExecutable(Boolean executable);

   /**
    * @return the hidden
    */
   Boolean getHidden();

   /**
    * @param hidden
    *           the hidden to set
    */
   void setHidden(Boolean hidden);

   /**
    * @return the archive
    */
   Boolean getArchive();

   /**
    * @param archive
    *           the archive to set
    */
   void setArchive(Boolean archive);

   /**
    * @return the symbolicLink
    */
   Boolean getSymbolicLink();

   /**
    * @param symbolicLink
    *           the symbolicLink to set
    */
   void setSymbolicLink(Boolean symbolicLink);

}
