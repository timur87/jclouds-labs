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
package org.jclouds.abiquo.domain.enterprise;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.jclouds.abiquo.AbiquoApi;
import org.jclouds.abiquo.domain.DomainWrapper;
import org.jclouds.abiquo.domain.config.Privilege;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.rest.ApiContext;

import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.enterprise.PrivilegesDto;
import com.abiquo.server.core.enterprise.RoleDto;

/**
 * Adds high level functionality to {@link RoleDto}.
 * 
 * @see API: <a href="http://community.abiquo.com/display/ABI20/Roles+Resource">
 *      http://community.abiquo.com/display/ABI20/Roles+Resource</a>
 */
public class Role extends DomainWrapper<RoleDto> {
   /** Default active value of the user */
   private static final boolean DEFAULT_BLOCKED = false;

   /**
    * Constructor to be used only by the builder.
    */
   protected Role(final ApiContext<AbiquoApi> context, final RoleDto target) {
      super(context, target);
   }

   // Domain operations

   /**
    * @see API: <a href=
    *      "http://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource-DeleteanexistingRole"
    *      >
    *      http://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource
    *      -DeleteanexistingRole </a>
    */
   public void delete() {
      context.getApi().getAdminApi().deleteRole(target);
      target = null;
   }

   /**
    * @see API: <a href=
    *      "http://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource-CreateanewRole"
    *      > http
    *      ://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource
    *      -CreateanewRole</a>
    */
   public void save() {
      target = context.getApi().getAdminApi().createRole(target);
   }

   /**
    * @see API: <a href=
    *      "http://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource-UpdateanexistingRole"
    *      >
    *      http://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource
    *      -UpdateanexistingRole </a>
    */
   public void update() {
      target = context.getApi().getAdminApi().updateRole(target);
   }

   public void setEnterprise(final Enterprise enterprise) {
      checkNotNull(enterprise, ValidationErrors.NULL_RESOURCE + Enterprise.class);
      checkNotNull(enterprise.getId(), ValidationErrors.MISSING_REQUIRED_FIELD + " id in " + Enterprise.class);

      RESTLink link = enterprise.unwrap().searchLink("edit");

      checkNotNull(link, ValidationErrors.MISSING_REQUIRED_LINK);

      target.addLink(new RESTLink("enterprise", link.getHref()));
   }

   public void setPrivileges(final List<Privilege> privileges) {
      for (Privilege privilege : privileges) {
         addPrivilege(privilege);
      }
   }

   private void addPrivilege(final Privilege privilege) {
      checkNotNull(privilege, ValidationErrors.NULL_RESOURCE + Privilege.class);
      checkNotNull(privilege.getId(), ValidationErrors.MISSING_REQUIRED_FIELD + " id in " + Privilege.class);

      RESTLink link = privilege.unwrap().searchLink("self");

      // rel would be "privilege" if the object is coming from a privilege list.
      if (link == null) {
         link = privilege.unwrap().searchLink("privilege");
      }

      checkNotNull(link, ValidationErrors.MISSING_REQUIRED_LINK);

      target.addLink(new RESTLink("privilege" + privilege.getId(), link.getHref()));
   }

   // Children access

   /**
    * @see API: <a href=
    *      "http://community.abiquo.com/display/ABI20/Roles+Resource#RolesResource-RetrievealistofprivilegesfromaRole"
    *      > http://community.abiquo.com/display/ABI20/Roles+Resource#
    *      RolesResource- RetrievealistofprivilegesfromaRole</a>
    */
   public Iterable<Privilege> listPrivileges() {
      PrivilegesDto dto = context.getApi().getAdminApi().listPrivileges(target);
      return wrap(context, Privilege.class, dto.getCollection());
   }

   // Builder

   public static Builder builder(final ApiContext<AbiquoApi> context) {
      return new Builder(context);
   }

   public static class Builder {
      private ApiContext<AbiquoApi> context;

      private String name;

      private boolean blocked = DEFAULT_BLOCKED;

      public Builder(final ApiContext<AbiquoApi> context) {
         super();
         this.context = context;
      }

      public Builder name(final String name) {
         this.name = name;
         return this;
      }

      public Builder blocked(final boolean blocked) {
         this.blocked = blocked;
         return this;
      }

      public Role build() {
         RoleDto dto = new RoleDto();
         dto.setName(name);
         dto.setBlocked(blocked);
         Role role = new Role(context, dto);

         return role;
      }

      public static Builder fromRole(final Role in) {
         return Role.builder(in.context).blocked(in.isBlocked()).name(in.getName());
      }
   }

   // Delegate methods

   public Integer getId() {
      return target.getId();
   }

   public String getName() {
      return target.getName();
   }

   public boolean isBlocked() {
      return target.isBlocked();
   }

   public void setBlocked(final boolean blocked) {
      target.setBlocked(blocked);
   }

   public void setName(final String name) {
      target.setName(name);
   }

   @Override
   public String toString() {
      return "Role [id=" + getId() + ", name=" + getName() + ", blocked=" + isBlocked() + "]";
   }

}
