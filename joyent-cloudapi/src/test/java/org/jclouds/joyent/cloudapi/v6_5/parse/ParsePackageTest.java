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
package org.jclouds.joyent.cloudapi.v6_5.parse;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.jclouds.json.BaseItemParserTest;
import org.jclouds.json.config.GsonModule;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

@Test(groups = "unit", testName = "ParsePackageTest")
public class ParsePackageTest extends BaseItemParserTest<org.jclouds.joyent.cloudapi.v6_5.domain.Package> {

   @Override
   public String resource() {
      return "/package.json";
   }

   @Override
   @Consumes(MediaType.APPLICATION_JSON)
   public org.jclouds.joyent.cloudapi.v6_5.domain.Package expected() {
      return org.jclouds.joyent.cloudapi.v6_5.domain.Package.builder()
                .name("Small 1GB")
                .memorySizeMb(1024)
                .diskSizeGb(30720)
                .swapSizeMb(2048)
                .isDefault(true).build();
   }

   protected Injector injector() {
      return Guice.createInjector(new GsonModule() {

         @Override
         protected void configure() {
            bind(DateAdapter.class).to(Iso8601DateAdapter.class);
            super.configure();
         }

      });
   }
}
