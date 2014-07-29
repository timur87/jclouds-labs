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
package org.jclouds.orion.http.filters;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpRequestFilter;
import org.jclouds.http.HttpResponse;
import org.jclouds.location.Provider;
import org.jclouds.orion.OrionApi;
import org.jclouds.orion.config.constans.OrionConstantValues;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.net.HttpHeaders;

/**
 * This class is used to make a form-based authentication. It contains a cache.
 * The authentication keys for users are kept in a cache table
 * 
 * @author Timur
 * 
 */
public class FormAuthentication implements HttpRequestFilter {

   private class SessionKeyRequester implements Callable<Collection<String>> {

      @Override
      public Collection<String> call() throws Exception {
         if (!OrionConstantValues.DEBUG_MODE) {
            return FormAuthentication.this
                  .getApi()
                  .formLogin(FormAuthentication.this.getCreds().identity, FormAuthentication.this.getCreds().credential)
                  .getHeaders().get(HttpHeaders.SET_COOKIE);
         } else {
            HttpResponse res = FormAuthentication.this.getApi().formLogin(FormAuthentication.this.getCreds().identity,
                  FormAuthentication.this.getCreds().credential);
            return res.getHeaders().get(HttpHeaders.SET_COOKIE);
         }
      }

   }

   private static Cache<String, Collection<String>> getKeycache() {
      return FormAuthentication.keyCache;
   }

   private final Credentials creds;

   private final OrionApi api;

   // This class holds a cache for keys
   // username:sesionsId pairs are used
   final static private Cache<String, Collection<String>> keyCache = CacheBuilder.newBuilder().maximumSize(1000)
         .build();

   public static boolean hasKey(String identity) {

      return FormAuthentication.getKeycache().asMap().containsKey(identity);
   }

   public static void removeKey(String identity) {
      FormAuthentication.getKeycache().invalidate(identity);
   }

   @Inject
   public FormAuthentication(@Provider Supplier<Credentials> creds, OrionApi api) {
      Preconditions.checkNotNull(creds, "creds");
      this.creds = creds.get();
      this.api = Preconditions.checkNotNull(api, "creds");
   }

   @Override
   public HttpRequest filter(HttpRequest request) throws HttpException {
      Collection<String> cachedKey = null;
      try {
         cachedKey = FormAuthentication.getKeycache().get(this.getCreds().identity, new SessionKeyRequester());
         request = request.toBuilder()
               .replaceHeader(HttpHeaders.COOKIE, cachedKey.toArray(new String[cachedKey.size()])).build();
      } catch (ExecutionException e) {
         e.printStackTrace();
         throw new HttpException(e.getMessage());
      } catch (Exception e) {
         e.printStackTrace();
      }

      return request;
   }

   private OrionApi getApi() {
      return this.api;
   }

   private Credentials getCreds() {
      return this.creds;
   }
}
