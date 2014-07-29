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

import java.util.Set;
import org.jclouds.joyent.cloudapi.v6_5.domain.Dataset;

/**
 * Provides synchronous access to Datasets.
 * <p/>
 * 
 * @see DatasetAsyncApi
 * @see <a href="http://apidocs.joyent.com/sdcapidoc/cloudapi/index.html#datasets">api doc</a>
 */
public interface DatasetApi {

   /**
    * Provides a list of datasets available in this datacenter.
    * 
    * @return
    */
   Set<Dataset> list();

   /**
    * Gets an individual dataset by id.
    * 
    * @param id
    *           the id of the dataset
    * @return
    */
   Dataset get(String id);
}
