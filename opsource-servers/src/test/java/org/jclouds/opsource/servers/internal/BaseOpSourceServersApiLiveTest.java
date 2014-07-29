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
package org.jclouds.opsource.servers.internal;

import org.jclouds.apis.BaseContextLiveTest;
import org.jclouds.opsource.servers.OpSourceServersApiMetadata;
import org.jclouds.opsource.servers.OpSourceServersAsyncApi;
import org.jclouds.opsource.servers.OpSourceServersApi;
import org.jclouds.rest.RestContext;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.google.common.reflect.TypeToken;

/**
 * Tests behavior of {@link OpSourceServersApi} and acts as parent for other api live tests.
 */
@Test(groups = "live")
public abstract class BaseOpSourceServersApiLiveTest extends
         BaseContextLiveTest<RestContext<OpSourceServersApi, OpSourceServersAsyncApi>> {

   protected BaseOpSourceServersApiLiveTest() {
      provider = "opsource-servers";
   }

   protected RestContext<OpSourceServersApi, OpSourceServersAsyncApi> restContext;

   @BeforeGroups(groups = { "integration", "live" })
   @Override
   public void setupContext() {
      super.setupContext();
      restContext = context;
   }
   
   @Override
   protected TypeToken<RestContext<OpSourceServersApi, OpSourceServersAsyncApi>> contextType() {
      return OpSourceServersApiMetadata.CONTEXT_TOKEN;
   }

}
