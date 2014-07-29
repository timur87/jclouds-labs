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
package org.jclouds.greenqloud.compute.config;

import org.jclouds.ec2.compute.config.EC2ComputeServiceContextModule;
import org.jclouds.ec2.compute.strategy.ReviseParsedImage;
import org.jclouds.ec2.compute.suppliers.EC2HardwareSupplier;
import org.jclouds.greenqloud.compute.strategy.GreenQloudComputeReviseParsedImage;
import org.jclouds.greenqloud.compute.suppliers.GreenQloudEC2HardwareSupplier;

public class GreenQloudComputeComputeServiceContextModule extends EC2ComputeServiceContextModule {

   @Override
   protected void configure() {
      super.configure();
      bind(ReviseParsedImage.class).to(GreenQloudComputeReviseParsedImage.class);
      bind(EC2HardwareSupplier.class).to(GreenQloudEC2HardwareSupplier.class);
   }

}
