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
package org.jclouds.blobstore.representations;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Map;

import static org.jclouds.representations.Representations.dateFormat;

public class StorageMetadata implements Serializable {

   private static final long serialVersionUID = -162484731217251198L;

   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {
      private String type;
      private String providerId;
      private String name;
      private String uri;
      private Map<String, String> userMetadata = ImmutableMap.of();
      private String eTag;
      private String creationDate;
      private String lastModifiedDate;

      public Builder type(final String type) {
         this.type = type;
         return this;
      }

      public Builder providerId(final String providerId) {
         this.providerId = providerId;
         return this;
      }

      public Builder name(final String name) {
         this.name = name;
         return this;
      }

      public Builder uri(final URI uri) {
         if (uri != null) {
            this.uri = uri.toString();
         }
         return this;
      }

      public Builder uri(final String uri) {
         this.uri = uri;
         return this;
      }

      public Builder userMetadata(final Map<String, String> userMetadata) {
         this.userMetadata = ImmutableMap.copyOf(userMetadata);
         return this;
      }

      public Builder eTag(final String eTag) {
         this.eTag = eTag;
         return this;
      }

      public Builder creationDate(final Date creationDate) {
         this.creationDate = dateFormat(creationDate);
         return this;
      }

      public Builder creationDate(final String creationDate) {
         this.creationDate = creationDate;
         return this;
      }

      public Builder lastModifiedDate(final Date lastModifiedDate) {
         this.lastModifiedDate = dateFormat(lastModifiedDate);
         return this;
      }

      public Builder lastModifiedDate(final String lastModifiedDate) {
         this.lastModifiedDate = lastModifiedDate;
         return this;
      }

      public StorageMetadata build() {
         return new StorageMetadata(type, providerId, name, uri, userMetadata, eTag, creationDate, lastModifiedDate);
      }

   }

   private final String type;
   private final String providerId;
   private final String name;
   private final String uri;
   private final Map<String, String> userMetadata;
   private final String eTag;
   private final String creationDate;
   private final String lastModifiedDate;

   public StorageMetadata(String type, String providerId, String name, String uri, Map<String, String> userMetadata,
                          String eTag, String creationDate, String lastModifiedDate) {
      this.type = type;
      this.providerId = providerId;
      this.name = name;
      this.uri = uri;
      this.userMetadata = userMetadata;
      this.eTag = eTag;
      this.creationDate = creationDate;
      this.lastModifiedDate = lastModifiedDate;
   }

   public String getType() {
      return type;
   }

   public String getProviderId() {
      return providerId;
   }

   public String getName() {
      return name;
   }

   public String getUri() {
      return uri;
   }

   public Map<String, String> getUserMetadata() {
      return userMetadata;
   }

   public String geteTag() {
      return eTag;
   }

   public String getCreationDate() {
      return creationDate;
   }

   public String getLastModifiedDate() {
      return lastModifiedDate;
   }

   public int hashCode() {
      return Objects.hashCode(uri);
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (!(obj instanceof StorageMetadata)) {
         return false;
      }
      StorageMetadata that = (StorageMetadata) obj;
      return Objects.equal(this.type, that.type) &&
            Objects.equal(this.providerId, that.providerId) &&
            Objects.equal(this.name, that.name) &&
            Objects.equal(this.uri, that.uri) &&
            Objects.equal(this.userMetadata, that.userMetadata) &&
            Objects.equal(this.eTag, that.eTag) &&
            Objects.equal(this.creationDate, that.creationDate) &&
            Objects.equal(this.lastModifiedDate, that.lastModifiedDate);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).add("type", type).add("providerId", providerId).add("name", name)
              .add("userMetadata", userMetadata).add("eTag", eTag).add("creationDate", creationDate)
              .add("lastModifiedDate", lastModifiedDate).toString();
   }
}
