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
package org.jclouds.orion.blobstore.functions.converters;

import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * A function to de-serialize {@link OrionSpecificFileMetadata} from a JSON
 * String
 * 
 * @author Timur
 * 
 */
public class JSONToOrionSpecificObject implements Function<String, OrionSpecificFileMetadata> {

   private final JSONUtils jsonConverter;

   @Inject
   public JSONToOrionSpecificObject(JSONUtils mapper) {
      this.jsonConverter = Preconditions.checkNotNull(mapper, "mapper is null");
   }

   @Override
   public OrionSpecificFileMetadata apply(String jsonStr) {
      return this.jsonConverter.getStringAsObject(jsonStr, OrionSpecificFileMetadata.class);
   }
}
