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
package org.jclouds.orion.config.constans;

/**
 * Http related fields such as headers, query fields, form fields
 *
 *
 */
public class OrionHttpFields {

   public static final String ORION_VERSION_FIELD = "Orion-Version";
   public static final String ORION_XFER_OPTIONS = "X-Xfer-Options";
   public static final String HEADER_SLUG = "Slug";

   // Form Parameters

   public static final String USERNAME = "login";
   public static final String PASSWORD = "password";

   // Ignore form authentication header
   // api methods wtih this header key will not be subject to authentication
   // check
   public static final String IGNORE_AUTHENTICATION = "ignoreAuthentication";

   // Orion Specific Fields

   public static final String ORION_NAME = "Name";
   public static final String ORION_DIRECTORY = "Directory";
   public static final String ORION_ATTRIBUTES = "Attributes";
   public static final String ORION_CONTENT_TYPE = "ContentType";

   public static final String QUERY_DEPTH = "depth";
   public static final String QUERY_PARTS = "parts";

   public static final String ORION_ECLIPSE_WEB_FIELD = "EclipseWeb-Version";

}
