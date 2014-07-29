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
package org.jclouds.vcloud.director.v1_5.login;

import static org.testng.Assert.assertEquals;

import java.net.URI;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.rest.AnonymousRestApiMetadata;
import org.jclouds.rest.internal.BaseRestApiExpectTest;
import org.jclouds.vcloud.director.v1_5.VCloudDirectorMediaType;
import org.jclouds.vcloud.director.v1_5.domain.Link;
import org.jclouds.vcloud.director.v1_5.domain.Session;
import org.jclouds.vcloud.director.v1_5.domain.SessionWithToken;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMultimap;

/**
 * 
 * Allows us to test a api via its side effects.
 */
@Test(groups = "unit", testName = "SessionApiExpectTest")
public class SessionApiExpectTest extends BaseRestApiExpectTest<SessionApi> {
   public static final String user = "adrian@jclouds.org";
   public static final String org = "JClouds";
   public static final Session SESSION = Session.builder().user(user).org(org).href(
            URI.create("https://vcloudbeta.bluelock.com/api/session/")).addLink(
            Link.builder().rel("down").type("application/vnd.vmware.vcloud.orgList+xml").href(
                     URI.create("https://vcloudbeta.bluelock.com/api/org/")).build()).addLink(
            Link.builder().rel("down").type("application/vnd.vmware.admin.vcloud+xml").href(
                     URI.create("https://vcloudbeta.bluelock.com/api/admin/")).build()).addLink(
            Link.builder().rel("down").type("application/vnd.vmware.vcloud.query.queryList+xml").href(
                     URI.create("https://vcloudbeta.bluelock.com/api/query")).build()).addLink(
            Link.builder().rel("entityResolver").type("application/vnd.vmware.vcloud.entity+xml").href(
                     URI.create("https://vcloudbeta.bluelock.com/api/entity/")).build()).build();

   public void testWhenResponseIs2xxLoginReturnsValidSession() {
      URI loginUrl = URI.create("https://vcloudbeta.bluelock.com/api/sessions");

      String token = "mIaR3/6Lna8DWImd7/JPR5rK8FcUHabt+G/UCJV5pJQ=";

      SessionApi api = requestSendsResponse(

      HttpRequest.builder().method("POST").endpoint(loginUrl).headers(
               ImmutableMultimap.<String, String> builder().put("Accept", "*/*").put("Authorization",
                        "Basic YWRyaWFuQGpjbG91ZHMub3JnQEpDbG91ZHM6cGFzc3dvcmQ=").build()).build(),

      HttpResponse.builder().statusCode(200).headers(
               ImmutableMultimap.<String, String> builder().put("x-vcloud-authorization", token).put("Set-Cookie",
                        String.format("vcloud-token=%s; Secure; Path=/", token)).build())
               .payload(
                        payloadFromResourceWithContentType("/session.xml", VCloudDirectorMediaType.SESSION + ";version=1.5")).build()

      );

      assertEquals(api.loginUserInOrgWithPassword(loginUrl, user, org, "password"), SessionWithToken.builder()
               .session(SESSION)

               .token(token).build());

   }

   public void testWhenResponseIs2xxGetSessionReturnsValidSession() {
      URI sessionUrl = URI.create("https://vcloudbeta.bluelock.com/api/session");

      String token = "mIaR3/6Lna8DWImd7/JPR5rK8FcUHabt+G/UCJV5pJQ=";

      SessionApi api = requestSendsResponse(

      HttpRequest.builder().method("GET").endpoint(sessionUrl).headers(
               ImmutableMultimap.<String, String> builder().put("x-vcloud-authorization", token).put("Accept", "*/*")
                        .build()).build(),

      HttpResponse.builder().statusCode(200)
               .payload(
                        payloadFromResourceWithContentType("/session.xml", VCloudDirectorMediaType.SESSION + ";version=1.5")).build()

      );

      assertEquals(api.getSessionWithToken(sessionUrl, token), SESSION);

   }

   public void testLogoutWhenResponseIs2xx() {
      URI sessionUrl = URI.create("https://vcloudbeta.bluelock.com/api/session");

      String token = "mIaR3/6Lna8DWImd7/JPR5rK8FcUHabt+G/UCJV5pJQ=";

      SessionApi api = requestSendsResponse(

      HttpRequest.builder().method("DELETE").endpoint(sessionUrl).headers(
               ImmutableMultimap.<String, String> builder().put("x-vcloud-authorization", token).put("Accept", "*/*")
                        .build()).build(),

      HttpResponse.builder().statusCode(204).build()

      );

      api.logoutSessionWithToken(sessionUrl, token);

   }
   
   @Override
   protected ApiMetadata createApiMetadata() {
      return AnonymousRestApiMetadata.forClientMappedToAsyncClient(SessionApi.class, SessionAsyncApi.class)
            .toBuilder().defaultEndpoint("https://vcloudbeta.bluelock.com/api").build();
   }
}
