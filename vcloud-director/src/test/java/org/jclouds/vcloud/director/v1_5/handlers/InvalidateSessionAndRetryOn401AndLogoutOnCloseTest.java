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
package org.jclouds.vcloud.director.v1_5.handlers;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpResponse;
import org.jclouds.vcloud.director.v1_5.domain.SessionWithToken;
import org.jclouds.vcloud.director.v1_5.login.SessionApi;
import org.testng.annotations.Test;

import com.google.common.cache.LoadingCache;

/**
 * Tests behavior of {@code InvalidateSessionAndRetryOn401AndLogoutOnClose} handler
 */
@Test(groups = "unit", testName = "InvalidateSessionAndRetryOn401AndLogoutOnCloseTest")
public class InvalidateSessionAndRetryOn401AndLogoutOnCloseTest {
   @SuppressWarnings("unchecked")
   @Test
   public void test401ShouldInvalidateSessionAndRetry() {
      HttpCommand command = createMock(HttpCommand.class);
      SessionApi sessionApi = createMock(SessionApi.class);
      LoadingCache<Credentials, SessionWithToken> cache = createMock(LoadingCache.class);

      cache.invalidateAll();
      expectLastCall();
      expect(command.incrementFailureCount()).andReturn(1);
      expect(command.isReplayable()).andReturn(true);
      expect(command.getFailureCount()).andReturn(1).atLeastOnce();

      replay(cache, command);

      HttpResponse response = HttpResponse.builder().statusCode(401).build();

      InvalidateSessionAndRetryOn401AndLogoutOnClose retry = new InvalidateSessionAndRetryOn401AndLogoutOnClose(cache,
               sessionApi);

      assertTrue(retry.shouldRetryRequest(command, response));

      verify(cache, command);
   }

   @SuppressWarnings("unchecked")
   @Test
   public void test403ShouldNotInvalidateSessionOrRetry() {
      HttpCommand command = createMock(HttpCommand.class);
      SessionApi sessionApi = createMock(SessionApi.class);
      LoadingCache<Credentials, SessionWithToken> cache = createMock(LoadingCache.class);

      replay(cache, command);

      HttpResponse response = HttpResponse.builder().statusCode(403).build();

      InvalidateSessionAndRetryOn401AndLogoutOnClose retry = new InvalidateSessionAndRetryOn401AndLogoutOnClose(cache,
               sessionApi);

      assertFalse(retry.shouldRetryRequest(command, response));

      verify(cache, command);
   }

}
