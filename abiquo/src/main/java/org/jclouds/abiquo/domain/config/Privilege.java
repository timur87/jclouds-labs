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
package org.jclouds.abiquo.domain.config;

import org.jclouds.abiquo.AbiquoApi;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.rest.ApiContext;

import com.abiquo.server.core.enterprise.PrivilegeDto;

/**
 * Adds high level functionality to {@link PrivilegeDto}.
 */
public class Privilege extends DomainWrapper<PrivilegeDto> {
   /**
    * Constructor to be used only by the builder. This resource cannot be
    * created.
    */
   private Privilege(final ApiContext<AbiquoApi> context, final PrivilegeDto target) {
      super(context, target);
   }

   // Delegate methods

   public Integer getId() {
      return target.getId();
   }

   public String getName() {
      return target.getName();
   }

   @Override
   public String toString() {
      return "Privilege [id=" + getId() + ", name=" + getName() + "]";
   }

}
