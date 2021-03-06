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
package org.jclouds.joyent.cloudapi.v6_5;

import java.util.Set;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.joyent.cloudapi.v6_5.features.DatacenterAsyncApi;
import org.jclouds.joyent.cloudapi.v6_5.features.DatasetAsyncApi;
import org.jclouds.joyent.cloudapi.v6_5.features.KeyAsyncApi;
import org.jclouds.joyent.cloudapi.v6_5.features.MachineAsyncApi;
import org.jclouds.joyent.cloudapi.v6_5.features.PackageAsyncApi;
import org.jclouds.location.Zone;
import org.jclouds.location.functions.ZoneToEndpoint;
import org.jclouds.rest.annotations.Delegate;
import org.jclouds.rest.annotations.EndpointParam;

import com.google.inject.Provides;

/**
 * Provides asynchronous access to JoyentCloud via their REST API.
 * <p/>
 * 
 * @see JoyentCloudApi
 * @see <a href="http://cloudApi.joyent.org/cloudApiapi.html">api doc</a>
 */
public interface JoyentCloudAsyncApi {
   
   /**
    * 
    * @return the datacenter codes configured
    */
   @Provides
   @Zone
   Set<String> getConfiguredDatacenters();
   
   /**
    * Provides asynchronous access to Datacenter features.
    */
   @Delegate
   DatacenterAsyncApi getDatacenterApi();

   /**
    * Provides asynchronous access to Key features.
    */
   @Delegate
   KeyAsyncApi getKeyApi();

   /**
    * Provides asynchronous access to Machine features.
    */
   @Delegate
   MachineAsyncApi getMachineApiForDatacenter(
         @EndpointParam(parser = ZoneToEndpoint.class) @Nullable String datacenter);

   /**
    * Provides asynchronous access to Dataset features.
    */
   @Delegate
   DatasetAsyncApi getDatasetApiForDatacenter(
         @EndpointParam(parser = ZoneToEndpoint.class) @Nullable String datacenter);

   /**
    * Provides asynchronous access to Package features.
    */
   @Delegate
   PackageAsyncApi getPackageApiForDatacenter(
         @EndpointParam(parser = ZoneToEndpoint.class) @Nullable String datacenter);
}
