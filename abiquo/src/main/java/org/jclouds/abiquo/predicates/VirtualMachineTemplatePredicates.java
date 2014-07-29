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
package org.jclouds.abiquo.predicates;

import org.jclouds.abiquo.domain.cloud.VirtualMachineTemplate;

import com.google.common.base.Predicate;

/**
 * Container for {@link VirtualMachineTemplate} filters.
 */
public class VirtualMachineTemplatePredicates {

   public static Predicate<VirtualMachineTemplate> isShared() {
      return new Predicate<VirtualMachineTemplate>() {
         @Override
         public boolean apply(final VirtualMachineTemplate input) {
            return input.unwrap().isShared();
         }
      };
   }

   public static Predicate<VirtualMachineTemplate> isInstance() {
      return new Predicate<VirtualMachineTemplate>() {
         @Override
         public boolean apply(final VirtualMachineTemplate input) {
            return input.unwrap().searchLink("master") != null;
         }
      };
   }
}
