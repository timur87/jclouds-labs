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
package org.jclouds.savvis.vpdc.compute.config;

import org.jclouds.compute.ComputeServiceAdapter;
import org.jclouds.compute.config.ComputeServiceAdapterContextModule;
import org.jclouds.compute.domain.CIMOperatingSystem;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.domain.Location;
import org.jclouds.savvis.vpdc.compute.functions.CIMOperatingSystemToImage;
import org.jclouds.savvis.vpdc.compute.functions.NetworkToLocation;
import org.jclouds.savvis.vpdc.compute.functions.VMSpecToHardware;
import org.jclouds.savvis.vpdc.compute.functions.VMToNodeMetadata;
import org.jclouds.savvis.vpdc.compute.strategy.VPDCComputeServiceAdapter;
import org.jclouds.savvis.vpdc.domain.Network;
import org.jclouds.savvis.vpdc.domain.VM;
import org.jclouds.savvis.vpdc.domain.VMSpec;

import com.google.common.base.Function;
import com.google.inject.TypeLiteral;

public class VPDCComputeServiceContextModule extends
         ComputeServiceAdapterContextModule<VM, VMSpec, CIMOperatingSystem, Network> {

   @Override
   protected void configure() {
      super.configure();
      bind(new TypeLiteral<ComputeServiceAdapter<VM, VMSpec, CIMOperatingSystem, Network>>() {
      }).to(VPDCComputeServiceAdapter.class);
      bind(new TypeLiteral<Function<VM, NodeMetadata>>() {
      }).to(VMToNodeMetadata.class);
      bind(new TypeLiteral<Function<CIMOperatingSystem, org.jclouds.compute.domain.Image>>() {
      }).to(CIMOperatingSystemToImage.class);
      bind(new TypeLiteral<Function<VMSpec, org.jclouds.compute.domain.Hardware>>() {
      }).to(VMSpecToHardware.class);
      bind(new TypeLiteral<Function<Network, Location>>() {
      }).to(NetworkToLocation.class);
      // to have the compute service adapter override default locations
      install(new LocationsFromComputeServiceAdapterModule<VM, VMSpec, CIMOperatingSystem, Network>(){});
   }
}
