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
package org.jclouds.jenkins.v1.config;

import java.util.Map;

import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.annotation.ClientError;
import org.jclouds.http.annotation.Redirection;
import org.jclouds.http.annotation.ServerError;
import org.jclouds.jenkins.v1.JenkinsAsyncApi;
import org.jclouds.jenkins.v1.JenkinsApi;
import org.jclouds.jenkins.v1.features.ComputerAsyncApi;
import org.jclouds.jenkins.v1.features.ComputerApi;
import org.jclouds.jenkins.v1.features.JobAsyncApi;
import org.jclouds.jenkins.v1.features.JobApi;
import org.jclouds.jenkins.v1.handlers.JenkinsErrorHandler;
import org.jclouds.rest.ConfiguresRestClient;
import org.jclouds.rest.config.RestClientModule;

import com.google.common.collect.ImmutableMap;

/**
 * Configures the Jenkins connection.
 */
@ConfiguresRestClient
public class JenkinsRestClientModule extends RestClientModule<JenkinsApi, JenkinsAsyncApi> {

   public static final Map<Class<?>, Class<?>> DELEGATE_MAP = ImmutableMap.<Class<?>, Class<?>> builder()
         .put(ComputerApi.class, ComputerAsyncApi.class)
         .put(JobApi.class, JobAsyncApi.class)
         .build();

   public JenkinsRestClientModule() {
      super(DELEGATE_MAP);
   }
   
   @Override
   protected void bindErrorHandlers() {
      bind(HttpErrorHandler.class).annotatedWith(Redirection.class).to(JenkinsErrorHandler.class);
      bind(HttpErrorHandler.class).annotatedWith(ClientError.class).to(JenkinsErrorHandler.class);
      bind(HttpErrorHandler.class).annotatedWith(ServerError.class).to(JenkinsErrorHandler.class);
   }
}
