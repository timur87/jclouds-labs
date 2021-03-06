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

import org.jclouds.orion.domain.OrionError;

import com.google.gson.annotations.SerializedName;

/**
 * {@see OrionError}
 * 
 *
 */
public class OrionErrorImpl implements OrionError {

   @SerializedName("HttpCode")
   String httpCode;
   @SerializedName("Code")
   String code;
   @SerializedName("Severity")
   String severity;
   @SerializedName("Message")
   String message;
   @SerializedName("DetailedMessage")
   String detailedMessage;
   @SerializedName("Cause")
   String cause;
   @SerializedName("SeeAlso")
   String seeAlso;

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.OrionError#getHttpCode()
    */
   @Override
   public String getHttpCode() {
      return this.httpCode;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.orion.domain.internal.OrionError#setHttpCode(java.lang.String
    * )
    */
   @Override
   public void setHttpCode(String httpCode) {
      this.httpCode = httpCode;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.OrionError#getCode()
    */
   @Override
   public String getCode() {
      return this.code;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.orion.domain.internal.OrionError#setCode(java.lang.String)
    */
   @Override
   public void setCode(String code) {
      this.code = code;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.OrionError#getSeverity()
    */
   @Override
   public String getSeverity() {
      return this.severity;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.orion.domain.internal.OrionError#setSeverity(java.lang.String
    * )
    */
   @Override
   public void setSeverity(String severity) {
      this.severity = severity;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.OrionError#getMessage()
    */
   @Override
   public String getMessage() {
      return this.message;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.orion.domain.internal.OrionError#setMessage(java.lang.String)
    */
   @Override
   public void setMessage(String message) {
      this.message = message;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.OrionError#getDetailedMessage()
    */
   @Override
   public String getDetailedMessage() {
      return this.detailedMessage;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.OrionError#setDetailedMessage(java.
    * lang.String)
    */
   @Override
   public void setDetailedMessage(String detailedMessage) {
      this.detailedMessage = detailedMessage;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.OrionError#getCause()
    */
   @Override
   public String getCause() {
      return this.cause;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.orion.domain.internal.OrionError#setCause(java.lang.String)
    */
   @Override
   public void setCause(String cause) {
      this.cause = cause;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.orion.domain.internal.OrionError#getSeeAlso()
    */
   @Override
   public String getSeeAlso() {
      return this.seeAlso;
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * org.jclouds.orion.domain.internal.OrionError#setSeeAlso(java.lang.String)
    */
   @Override
   public void setSeeAlso(String seeAlso) {
      this.seeAlso = seeAlso;
   }

}
