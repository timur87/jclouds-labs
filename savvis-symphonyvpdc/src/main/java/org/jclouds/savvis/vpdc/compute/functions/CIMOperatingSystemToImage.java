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
package org.jclouds.savvis.vpdc.compute.functions;

import javax.inject.Singleton;

import org.jclouds.compute.domain.CIMOperatingSystem;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.Image.Status;

import com.google.common.base.Function;

@Singleton
public class CIMOperatingSystemToImage implements Function<CIMOperatingSystem, Image> {

   @Override
   public Image apply(CIMOperatingSystem from) {
      ImageBuilder builder = new ImageBuilder();
      builder.ids(from.getOsType().getCode() + "");
      builder.name(from.getName());
      builder.description(from.getDescription());
      builder.operatingSystem(from);
      builder.status(Status.AVAILABLE);
      return builder.build();
   }

}
