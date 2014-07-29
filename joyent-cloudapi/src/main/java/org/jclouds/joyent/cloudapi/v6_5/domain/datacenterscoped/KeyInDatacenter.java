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
package org.jclouds.joyent.cloudapi.v6_5.domain.datacenterscoped;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.joyent.cloudapi.v6_5.domain.Key;

public class KeyInDatacenter extends DatacenterAndName {
   protected final Key key;

   public KeyInDatacenter(Key key, String datacenterId) {
      super(datacenterId, checkNotNull(key, "key").getName());
      this.key = key;
   }

   public Key get() {
      return key;
   }

   // superclass hashCode/equals are good enough, and help us use DatacenterAndId and PackageInDatacenter
   // interchangeably as Map keys

   @Override
   public String toString() {
      return "[key=" + key + ", datacenterId=" + datacenterId + "]";
   }

}
