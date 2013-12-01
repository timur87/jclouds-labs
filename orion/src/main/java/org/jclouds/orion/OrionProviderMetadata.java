/*******************************************************************************
 * Copyright (c) 2013 University of Stuttgart.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and the Apache License 2.0 which both accompany this distribution,
 * and are available at http://www.eclipse.org/legal/epl-v10.html
 * and http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *    Timur Sungur - initial API and implementation
 *******************************************************************************/

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
      // TODO Auto-generated constructor stub
   }
}
