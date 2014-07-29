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
package org.jclouds.cloudsigma2.domain;

import com.google.common.collect.ImmutableList;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Tag extends Item {

   public static class Builder extends Item.Builder {
      private Map<String, String> meta;
      private Owner owner;
      private List<TagResource> resources;

      public Builder meta(Map<String, String> meta) {
         this.meta = meta;
         return this;
      }

      public Builder owner(Owner owner) {
         this.owner = owner;
         return this;
      }

      public Builder resources(List<TagResource> resources) {
         this.resources = ImmutableList.copyOf(resources);
         return this;
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public Builder uuid(String uuid) {
         return Builder.class.cast(super.uuid(uuid));
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public Builder name(String name) {
         return Builder.class.cast(super.name(name));
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public Builder resourceUri(URI resourceUri) {
         return Builder.class.cast(super.resourceUri(resourceUri));
      }

      public Tag build() {
         return new Tag(uuid, name, resourceUri, meta, owner, resources);
      }
   }

   private final Map<String, String> meta;
   private final Owner owner;
   private final List<TagResource> resources;

   @ConstructorProperties({
         "uuid", "name", "resource_uri", "meta", "owner", "resources"
   })
   public Tag(String uuid, String name, URI resourceUri, Map<String, String> meta, Owner owner,
              List<TagResource> resources) {
      super(uuid, name, resourceUri);
      this.meta = meta;
      this.owner = owner;
      this.resources = resources == null ? new ArrayList<TagResource>() : resources;
   }

   /**
    * @return tag meta data
    */
   public Map<String, String> getMeta() {
      return meta;
   }

   /**
    * @return tag owner
    */
   public Owner getOwner() {
      return owner;
   }

   /**
    * @return tag's resource list
    */
   public List<TagResource> getResources() {
      return resources;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Tag)) return false;
      if (!super.equals(o)) return false;

      Tag tag = (Tag) o;

      if (meta != null ? !meta.equals(tag.meta) : tag.meta != null) return false;
      if (owner != null ? !owner.equals(tag.owner) : tag.owner != null) return false;
      if (resources != null ? !resources.equals(tag.resources) : tag.resources != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + (meta != null ? meta.hashCode() : 0);
      result = 31 * result + (owner != null ? owner.hashCode() : 0);
      result = 31 * result + (resources != null ? resources.hashCode() : 0);
      return result;
   }

   @Override
   public String toString() {
      return "[uuid=" + uuid + ", name=" + name + ", resourceUri=" + resourceUri
            + ", meta=" + meta + ", owner=" + owner + ", resources=" + resources + "]";
   }
}
