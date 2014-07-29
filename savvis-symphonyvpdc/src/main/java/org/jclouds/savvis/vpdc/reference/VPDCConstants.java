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
package org.jclouds.savvis.vpdc.reference;

/**
 * Configuration properties and constants used in VCloud connections.
 */
public final class VPDCConstants {

   /**
    * which email must be present for VDCs to be considered yours
    */
   public static final String PROPERTY_VPDC_VDC_EMAIL = "savvis-symphonyvpdc.vdc-email";

   public static final String PROPERTY_VPDC_TIMEOUT_TASK_COMPLETED = "jclouds.vpdc.timeout.task-complete";

   private VPDCConstants() {
      throw new AssertionError("intentionally unimplemented");
   }
}
