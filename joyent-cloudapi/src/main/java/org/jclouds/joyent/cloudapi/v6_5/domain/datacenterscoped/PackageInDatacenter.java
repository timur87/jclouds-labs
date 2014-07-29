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

public class PackageInDatacenter extends DatacenterAndName {
   protected final org.jclouds.joyent.cloudapi.v6_5.domain.Package pkg;

   public PackageInDatacenter(org.jclouds.joyent.cloudapi.v6_5.domain.Package pkg, String datacenterId) {
      super(datacenterId, checkNotNull(pkg, "pkg").getName());
      this.pkg = pkg;
   }

   public org.jclouds.joyent.cloudapi.v6_5.domain.Package get() {
      return pkg;
   }

   // superclass hashCode/equals are good enough, and help us use DatacenterAndId and PackageInDatacenter
   // interchangeably as Map keys

   @Override
   public String toString() {
      return "[pkg=" + pkg + ", datacenterId=" + datacenterId + "]";
   }

}
