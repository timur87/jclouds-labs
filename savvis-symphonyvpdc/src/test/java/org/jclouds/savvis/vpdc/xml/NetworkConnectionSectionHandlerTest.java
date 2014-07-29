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
package org.jclouds.savvis.vpdc.xml;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;

import org.jclouds.http.functions.ParseSax;
import org.jclouds.http.functions.ParseSax.Factory;
import org.jclouds.http.functions.config.SaxParserModule;
import org.jclouds.savvis.vpdc.domain.NetworkConnectionSection;
import org.testng.annotations.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests behavior of {@code NetworkConnectionSectionHandler}
 */
@Test(groups = "unit")
public class NetworkConnectionSectionHandlerTest {

   public void test() {
      InputStream is = getClass().getResourceAsStream("/networkconnectionsection.xml");
      Injector injector = Guice.createInjector(new SaxParserModule());
      Factory factory = injector.getInstance(ParseSax.Factory.class);
      NetworkConnectionSection result = factory.create(injector.getInstance(NetworkConnectionSectionHandler.class))
               .parse(is);
      assertEquals(result.toString(), NetworkConnectionSection.builder().network("VM Tier01").info(
               "Read only description of a network connection to a deployed vApp").ipAddress("10.76.0.5").build()
               .toString());
   }
}
