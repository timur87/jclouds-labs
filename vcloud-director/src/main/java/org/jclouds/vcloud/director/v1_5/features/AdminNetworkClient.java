/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.vcloud.director.v1_5.features;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.jclouds.concurrent.Timeout;
import org.jclouds.vcloud.director.v1_5.domain.Network;

/**
 * Provides synchronous access to admin {@link Network} objects.
 * 
 * @see AdminNetworkAsyncClient
 * @author danikov
 */
@Timeout(duration = 180, timeUnit = TimeUnit.SECONDS)
public interface AdminNetworkClient extends NetworkClient {
   
   /**
    * Gets admin representation of network. This operation could return admin 
    * representation of organization network or external network. vApp networks 
    * do not have admin representation.
    *
    * <pre>
    * GET /admin/network/{id}
    * </pre>
    *
    * @param catalogUri the reference for the catalog
    * @return a catalog
    */
   @Override
   Network getNetwork(URI networkRef);
   
   // PUT /admin/network/{id}
   
   // POST /admin/network/{id}/action/reset

}
