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
package org.jclouds.docker;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;
import org.jclouds.Constants;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.config.ComputeServiceProperties;
import org.jclouds.docker.compute.config.DockerComputeServiceContextModule;
import org.jclouds.docker.config.DockerHttpApiModule;
import org.jclouds.docker.config.DockerParserModule;
import org.jclouds.rest.internal.BaseHttpApiMetadata;

import java.net.URI;
import java.util.Properties;

import static org.jclouds.compute.config.ComputeServiceProperties.TEMPLATE;
import static org.jclouds.reflect.Reflection2.typeToken;

/**
 * Implementation of {@link BaseHttpApiMetadata} for the Docker API
 */
public class DockerApiMetadata extends BaseHttpApiMetadata<DockerApi> {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public DockerApiMetadata() {
      this(new Builder());
   }

   protected DockerApiMetadata(Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      Properties properties = BaseHttpApiMetadata.defaultProperties();
      properties.setProperty(Constants.PROPERTY_MAX_RETRIES, "15");
      properties.setProperty("jclouds.ssh.retry-auth", "true");
      properties.setProperty(Constants.PROPERTY_CONNECTION_TIMEOUT, "1200000"); // 15 minutes
      properties.setProperty(ComputeServiceProperties.IMAGE_LOGIN_USER, "root:password");
      properties.setProperty(TEMPLATE, "osFamily=UBUNTU,os64Bit=true,osVersionMatches=1[012].[01][04]");
      return properties;
   }

   public static class Builder extends BaseHttpApiMetadata.Builder<DockerApi, Builder> {

      protected Builder() {
         super(DockerApi.class);
         id("docker")
                 .name("Docker API")
                 .identityName("user")
                 .credentialName("password")
                 .documentation(URI.create("https://docs.docker.com/reference/api/docker_remote_api/"))
                 .version("1.12")
                 .defaultEndpoint("http://127.0.0.1:2375")
                 .defaultProperties(DockerApiMetadata.defaultProperties())
                 .view(typeToken(ComputeServiceContext.class))
                 .defaultModules(ImmutableSet.<Class<? extends Module>>of(
                         DockerHttpApiModule.class,
                         DockerParserModule.class,
                         DockerComputeServiceContextModule.class));
      }

      @Override
      public DockerApiMetadata build() {
         return new DockerApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }

      @Override
      public Builder fromApiMetadata(ApiMetadata in) {
         return this;
      }
   }
}
