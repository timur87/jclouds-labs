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
package org.jclouds.joyent.cloudapi.v6_5.features;

import static org.testng.Assert.assertEquals;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.joyent.cloudapi.v6_5.JoyentCloudApi;
import org.jclouds.joyent.cloudapi.v6_5.internal.BaseJoyentCloudApiExpectTest;
import org.jclouds.joyent.cloudapi.v6_5.options.CreateMachineOptions;
import org.jclouds.joyent.cloudapi.v6_5.parse.ParseCreatedMachineTest;
import org.jclouds.joyent.cloudapi.v6_5.parse.ParseMachineListTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

@Test(groups = "unit", testName = "MachineApiExpectTest")
public class MachineApiExpectTest extends BaseJoyentCloudApiExpectTest {
   public HttpRequest list = HttpRequest.builder().method("GET")
            .endpoint("https://us-sw-1.api.joyentcloud.com/my/machines")
            .addHeader("X-Api-Version", "~6.5")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Basic aWRlbnRpdHk6Y3JlZGVudGlhbA==").build();

   public HttpResponse listResponse = HttpResponse.builder().statusCode(200).payload(
            payloadFromResource("/machine_list.json")).build();

   public void testListMachinesWhenResponseIs2xx() {

      JoyentCloudApi apiWhenMachinesExists = requestsSendResponses(getDatacenters, getDatacentersResponse, list, listResponse);

      assertEquals(apiWhenMachinesExists.getMachineApiForDatacenter("us-sw-1").list(), new ParseMachineListTest().expected());
   }

   public void testListMachinesWhenResponseIs404() {
      HttpResponse listResponse = HttpResponse.builder().statusCode(404).build();

      JoyentCloudApi listWhenNone = requestsSendResponses(getDatacenters, getDatacentersResponse, list, listResponse);

      assertEquals(listWhenNone.getMachineApiForDatacenter("us-sw-1").list(), ImmutableSet.of());
   }

   public void testCreateMachineWhenResponseIs202() throws Exception {
      HttpRequest createWithDataset = HttpRequest
               .builder()
               .method("POST")
               .endpoint("https://us-sw-1.api.joyentcloud.com/my/machines?dataset=sdc%3Asdc%3Acentos-5.7%3A1.2.1&name=sample-e92&package=Small%201GB")
               .addHeader("X-Api-Version", "~6.5")
               .addHeader("Accept", "application/json")
               .addHeader("Authorization", "Basic aWRlbnRpdHk6Y3JlZGVudGlhbA==").build();

      HttpResponse createWithDatasetResponse = HttpResponse.builder().statusCode(202).message("HTTP/1.1 202 Accepted")
               .payload(payloadFromResourceWithContentType("/new_machine.json", "application/json; charset=UTF-8"))
               .build();

      JoyentCloudApi apiWithNewMachine = requestsSendResponses(getDatacenters, getDatacentersResponse, createWithDataset, createWithDatasetResponse);

      assertEquals(
            apiWithNewMachine
                  .getMachineApiForDatacenter("us-sw-1")
                  .createWithDataset("sdc:sdc:centos-5.7:1.2.1",
                        CreateMachineOptions.Builder.name("sample-e92").packageName("Small 1GB")).toString(),
            new ParseCreatedMachineTest().expected().toString());
   }
}
