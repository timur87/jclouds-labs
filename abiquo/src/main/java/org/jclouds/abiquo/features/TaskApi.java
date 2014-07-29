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
package org.jclouds.abiquo.features;

import java.io.Closeable;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;

import org.jclouds.abiquo.AbiquoFallbacks.NullOn303;
import org.jclouds.abiquo.binders.BindLinkToPath;
import org.jclouds.abiquo.binders.BindToPath;
import org.jclouds.abiquo.http.filters.AbiquoAuthentication;
import org.jclouds.abiquo.http.filters.AppendApiVersionToMediaType;
import org.jclouds.abiquo.rest.annotations.EndpointLink;
import org.jclouds.rest.annotations.BinderParam;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.JAXBResponseParser;
import org.jclouds.rest.annotations.RequestFilters;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.SingleResourceTransportDto;
import com.abiquo.server.core.task.TaskDto;
import com.abiquo.server.core.task.TasksDto;

/**
 * Provides synchronous access to Abiquo Task API.
 * 
 * @see API: <a href="http://community.abiquo.com/display/ABI20/API+Reference">
 *      http://community.abiquo.com/display/ABI20/API+Reference</a>
 */
@RequestFilters({ AbiquoAuthentication.class, AppendApiVersionToMediaType.class })
public interface TaskApi extends Closeable {
   /*********************** Task ***********************/

   /**
    * Get a task from its link.
    * 
    * @param link
    *           The link of the task.
    * @return The task.
    */
   @Named("task:get")
   @GET
   @Consumes(TaskDto.BASE_MEDIA_TYPE)
   @JAXBResponseParser
   @Fallback(NullOn303.class)
   TaskDto getTask(@BinderParam(BindLinkToPath.class) RESTLink link);

   /**
    * Get the list of tasks of the given object.
    * 
    * @param dto
    *           The object.
    * @return The list of tasks for the given object.
    */
   @Named("task:list")
   @GET
   @Consumes(TasksDto.BASE_MEDIA_TYPE)
   @JAXBResponseParser
   <T extends SingleResourceTransportDto> TasksDto listTasks(@EndpointLink("tasks") @BinderParam(BindToPath.class) T dto);
}
