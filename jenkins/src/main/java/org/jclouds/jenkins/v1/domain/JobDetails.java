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
package org.jclouds.jenkins.v1.domain;

import com.google.common.base.Objects.ToStringHelper;

public class JobDetails extends Job {

   public static Builder<?> builder() {
      return new ConcreteBuilder();
   }

   @Override
   public Builder<?> toBuilder() {
      return builder().fromJobDetails(this);
   }

   public static class Builder<B extends Builder<B>> extends Job.Builder<B> {

      @Override
      public JobDetails build() {
         return new JobDetails(this);
      }

      public B fromJobDetails(JobDetails in) {
         return fromJob(in);
      }
   }

   private static class ConcreteBuilder extends Builder<ConcreteBuilder> {
   }

   protected JobDetails(Builder<?> builder) {
      super(builder);
   }

   @Override
   public ToStringHelper string() {
      return super.string(); // .add("field", field);
   }

}
