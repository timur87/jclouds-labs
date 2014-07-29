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
package org.jclouds.savvis.vpdc.predicates;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Resource;
import javax.inject.Singleton;

import org.jclouds.logging.Logger;
import org.jclouds.savvis.vpdc.VPDCApi;
import org.jclouds.savvis.vpdc.domain.Task;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

/**
 * 
 * Tests to see if a task succeeds.
 */
@Singleton
public class TaskSuccess implements Predicate<String> {

   private final VPDCApi api;

   @Resource
   protected Logger logger = Logger.NULL;

   @Inject
   public TaskSuccess(VPDCApi api) {
      this.api = api;
   }

   public boolean apply(String taskId) {
      logger.trace("looking for status on task %s", checkNotNull(taskId, "taskId"));
      Task task = refresh(taskId);
      if (task == null)
         return false;
      logger.trace("%s: looking for task status %s: currently: %s", task.getId(), Task.Status.SUCCESS, task.getStatus());
      if (task.getError() != null)
         throw new IllegalStateException(String.format("task %s failed with exception %s", task.getId(), task
               .getError().toString()));
      return task.getStatus() == Task.Status.SUCCESS;
   }

   private Task refresh(String taskId) {
      return api.getBrowsingApi().getTask(taskId);
   }
}
