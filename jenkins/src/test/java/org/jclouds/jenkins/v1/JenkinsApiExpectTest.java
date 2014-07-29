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
package org.jclouds.jenkins.v1;

import static org.testng.Assert.assertEquals;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.jenkins.v1.internal.BaseJenkinsApiExpectTest;
import org.jclouds.jenkins.v1.parse.ParseNodeTest;
import org.testng.annotations.Test;

@Test(groups = "unit", testName = "JenkinsApiExpectTest")
public class JenkinsApiExpectTest extends BaseJenkinsApiExpectTest {

   public void testGetMasterWhenResponseIs2xx() {
      HttpRequest getMaster = HttpRequest
            .builder()
            .method("GET")
            .endpoint("http://localhost:8080/api/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Basic aWRlbnRpdHk6Y3JlZGVudGlhbA==").build();

      HttpResponse getMasterResponse = HttpResponse.builder().statusCode(200)
            .payload(payloadFromResource("/master.json")).build();

      JenkinsApi apiWhenMasterExists = requestSendsResponse(getMaster, getMasterResponse);

      assertEquals(apiWhenMasterExists.getMaster().toString(),
            new ParseNodeTest().expected().toString());
   }
}
