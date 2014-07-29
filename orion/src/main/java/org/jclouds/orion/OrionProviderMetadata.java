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

package org.jclouds.orion;

import java.util.Properties;

import org.jclouds.orion.config.constans.OrionConstantValues;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.providers.internal.BaseProviderMetadata;

public class OrionProviderMetadata extends BaseProviderMetadata {

   static class Builder extends BaseProviderMetadata.Builder {

      Builder() {
         this.id(OrionConstantValues.ORION_ID).name("Orion Service").defaultProperties(new Properties())
         .linkedService("bloborion").apiMetadata(new OrionApiMetadata()).endpoint(OrionConstantValues.END_POINT)
         .iso3166Code("");
      }

      @Override
      public ProviderMetadata build() {
         return new OrionProviderMetadata(this);
      }

      @Override
      public Builder fromProviderMetadata(ProviderMetadata in) {
         super.fromProviderMetadata(in);
         return this;
      }
   }

   public OrionProviderMetadata() {
      super(new Builder());
   }

   public OrionProviderMetadata(Builder builder) {
      super(builder);
   }
}
