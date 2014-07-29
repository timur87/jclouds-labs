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
package org.jclouds.joyent.cloudapi.v6_5.compute.functions;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Map;

import javax.inject.Inject;

import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.domain.Location;
import org.jclouds.joyent.cloudapi.v6_5.domain.Dataset;
import org.jclouds.joyent.cloudapi.v6_5.domain.datacenterscoped.DatasetInDatacenter;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

/**
 * A function for transforming a cloudApi-specific Image into a generic Image object.
 */
public class DatasetInDatacenterToImage implements Function<DatasetInDatacenter, Image> {
   private final Function<Dataset, OperatingSystem> imageToOs;
   private final Supplier<Map<String, Location>> locationIndex;

   @Inject
   public DatasetInDatacenterToImage(Function<Dataset, OperatingSystem> imageToOs,
         Supplier<Map<String, Location>> locationIndex) {
      this.imageToOs = checkNotNull(imageToOs, "imageToOs");
      this.locationIndex = checkNotNull(locationIndex, "locationIndex");
   }

   @Override
   public Image apply(DatasetInDatacenter datasetInDatacenter) {
      Location location = locationIndex.get().get(datasetInDatacenter.getDatacenter());
      checkState(location != null, "location %s not in locationIndex: %s", datasetInDatacenter.getDatacenter(),
            locationIndex.get());
      Dataset dataset = datasetInDatacenter.get();
      return new ImageBuilder()
            .id(datasetInDatacenter.slashEncode())
            // note that it is urn that is the expected value!
            .providerId(dataset.getUrn())
            .name(dataset.getName())
            .operatingSystem(imageToOs.apply(dataset))
            .description(dataset.getDescription())
            .version(dataset.getVersion())
            .location(location)
            .status(Image.Status.AVAILABLE).build();
   }
}
