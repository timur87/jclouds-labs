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
package org.jclouds.abiquo.domain.cloud;

import static com.abiquo.model.enumerator.VolumeState.ATTACHED;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import org.jclouds.abiquo.AbiquoApi;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.domain.infrastructure.Tier;
import org.jclouds.abiquo.domain.task.VirtualMachineTask;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.rest.ApiContext;

import com.abiquo.model.enumerator.VolumeState;
import com.abiquo.model.rest.RESTLink;
import com.abiquo.model.transport.AcceptedRequestDto;
import com.abiquo.server.core.cloud.VirtualDatacenterDto;
import com.abiquo.server.core.cloud.VirtualMachineWithNodeExtendedDto;
import com.abiquo.server.core.infrastructure.storage.TierDto;
import com.abiquo.server.core.infrastructure.storage.VolumeManagementDto;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 * Adds high level functionality to {@link VolumeManagementDto}.
 * 
 * @see API: <a
 *      href="http://community.abiquo.com/display/ABI20/Volume+Resource">
 *      http://community.abiquo.com/display/ABI20/Volume+Resource</a>
 */
public class Volume extends DomainWrapper<VolumeManagementDto> {
   /** The default state for volumes. */
   public static final VolumeState DEFAULT_STATE = VolumeState.DETACHED;

   /** The virtual datacenter where the volume belongs. */
   private VirtualDatacenter virtualDatacenter;

   /** The tier where the volume belongs. */
   private Tier tier;

   /**
    * Constructor to be used only by the builder.
    */
   protected Volume(final ApiContext<AbiquoApi> context, final VolumeManagementDto target) {
      super(context, target);
   }

   // Domain operations

   public void delete() {
      context.getApi().getCloudApi().deleteVolume(target);
      target = null;
   }

   public void save() {
      target = context.getApi().getCloudApi().createVolume(virtualDatacenter.unwrap(), target);
   }

   public VirtualMachineTask update() {
      AcceptedRequestDto<String> taskRef = context.getApi().getCloudApi().updateVolume(target);
      return taskRef == null ? null : getTask(taskRef).asVirtualMachineTask();
   }

   // Parent access

   /**
    * @see API: <a href=
    *      "http://community.abiquo.com/display/ABI20/Virtual+Datacenter+Resource#VirtualDatacenterResource-RetrieveaVirtualDatacenter"
    *      > http://community.abiquo.com/display/ABI20/Virtual+Datacenter+
    *      Resource# VirtualDatacenterResource-RetrieveaVirtualDatacenter</a>
    */
   public VirtualDatacenter getVirtualDatacenter() {
      Integer virtualDatacenterId = target.getIdFromLink(ParentLinkName.VIRTUAL_DATACENTER);
      VirtualDatacenterDto dto = context.getApi().getCloudApi().getVirtualDatacenter(virtualDatacenterId);
      virtualDatacenter = wrap(context, VirtualDatacenter.class, dto);
      return virtualDatacenter;
   }

   /**
    * Retrieve the virtual machine this volume is attached to.
    * 
    * @return The virtual machine this volume is attached to, or null if it is
    * not attached.
    */
   public VirtualMachine getVirtualMachine() {
      checkState(ATTACHED == VolumeState.valueOf(target.getState()), "Volume is not attached to a VM");
      RESTLink vmLink = checkNotNull(target.searchLink(ParentLinkName.VIRTUAL_MACHINE),
            ValidationErrors.MISSING_REQUIRED_LINK + " " + ParentLinkName.VIRTUAL_MACHINE);
      vmLink.setType(VirtualMachineWithNodeExtendedDto.BASE_MEDIA_TYPE);
      HttpResponse response = context.getApi().get(vmLink);

      ParseXMLWithJAXB<VirtualMachineWithNodeExtendedDto> parser = context.utils().injector()
            .getInstance(Key.get(new TypeLiteral<ParseXMLWithJAXB<VirtualMachineWithNodeExtendedDto>>(){}));
      return wrap(context, VirtualMachine.class, parser.apply(response));
   }

   public Tier getTier() {
      Integer tierId = target.getIdFromLink(ParentLinkName.TIER);
      TierDto dto = context.getApi().getCloudApi().getStorageTier(virtualDatacenter.unwrap(), tierId);
      tier = wrap(context, Tier.class, dto);
      return tier;
   }

   // Actions

   /**
    * Move the volume to the given virtual datacenter.
    * 
    * @param newVirtualDatacenter
    *           The destination virtual datacenter.
    */
   public void moveTo(final VirtualDatacenter newVirtualDatacenter) {
      target = context.getApi().getCloudApi().moveVolume(unwrap(), newVirtualDatacenter.unwrap());
   }

   // Builder

   public static Builder builder(final ApiContext<AbiquoApi> context, final VirtualDatacenter virtualDatacenter,
         final Tier tier) {
      return new Builder(context, virtualDatacenter, tier);
   }

   public static class Builder {
      private ApiContext<AbiquoApi> context;

      private String name;

      private String description;

      private Long sizeInMb;

      private VirtualDatacenter virtualDatacenter;

      private Tier tier;

      public Builder(final ApiContext<AbiquoApi> context, final VirtualDatacenter virtualDatacenter, final Tier tier) {
         super();
         checkNotNull(virtualDatacenter, ValidationErrors.NULL_RESOURCE + VirtualDatacenter.class);
         checkNotNull(tier, ValidationErrors.NULL_RESOURCE + Tier.class);
         this.context = context;
         this.virtualDatacenter = virtualDatacenter;
         this.tier = tier;
      }

      public Builder name(final String name) {
         this.name = name;
         return this;
      }

      public Builder description(final String description) {
         this.description = description;
         return this;
      }

      public Builder sizeInMb(final long sizeInMb) {
         this.sizeInMb = sizeInMb;
         return this;
      }

      public Volume build() {
         VolumeManagementDto dto = new VolumeManagementDto();
         dto.setName(name);
         dto.setDescription(description);
         dto.setSizeInMB(sizeInMb);
         dto.setState(DEFAULT_STATE.name());

         RESTLink link = tier.unwrap().searchLink("self");
         checkNotNull(link, ValidationErrors.MISSING_REQUIRED_LINK);
         dto.addLink(new RESTLink("tier", link.getHref()));

         Volume volume = new Volume(context, dto);
         volume.virtualDatacenter = virtualDatacenter;
         volume.tier = tier;

         return volume;
      }
   }

   // Delegate methods

   public Integer getId() {
      return target.getId();
   }

   public String getState() {
      return target.getState();
   }

   public String getName() {
      return target.getName();
   }

   public void setName(final String name) {
      target.setName(name);
   }

   public long getSizeInMB() {
      return target.getSizeInMB();
   }

   public void setSizeInMB(final long sizeInMB) {
      target.setSizeInMB(sizeInMB);
   }

   public String getDescription() {
      return target.getDescription();
   }

   public void setDescription(final String description) {
      target.setDescription(description);
   }

   @Override
   public String toString() {
      return "Volume [id=" + getId() + ", state=" + getState() + ", name=" + getName() + ", sizeInMB=" + getSizeInMB()
            + ", description=" + getDescription() + "]";
   }

}
