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
package org.jclouds.smartos;

import java.net.URI;

import org.jclouds.apis.internal.BaseApiMetadata;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.smartos.compute.config.SmartOSComputeServiceContextModule;
import org.jclouds.smartos.compute.config.SmartOSParserModule;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Implementation of {@link ApiMetadata} for SmartOS
 */
public class SmartOSApiMetadata extends BaseApiMetadata {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public SmartOSApiMetadata() {
      super(new Builder());
   }

   protected SmartOSApiMetadata(Builder builder) {
      super(builder);
   }

   public static class Builder extends BaseApiMetadata.Builder<Builder> {

      protected Builder() {
         id("smartos-ssh")
         .name("SmartOS SSH API")
         .identityName("Username")
         .defaultIdentity("root")
         .defaultCredential("smartos")
         .defaultEndpoint("http://localhost")
         .documentation(URI.create("http://http://wiki.smartos.org/display/DOC/How+to+create+a+Virtual+Machine+in+SmartOS"))
         .view(ComputeServiceContext.class)
         .defaultModules(ImmutableSet.<Class<? extends Module>>builder()
                                     .add(SmartOSComputeServiceContextModule.class)
                                     .add(SmartOSParserModule.class).build());
      }

      @Override
      public SmartOSApiMetadata build() {
         return new SmartOSApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }
   }
}
