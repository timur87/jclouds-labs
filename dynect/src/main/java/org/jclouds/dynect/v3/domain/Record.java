/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, String 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.dynect.v3.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;
import java.util.Map;

import javax.inject.Named;

import com.google.common.base.Objects.ToStringHelper;
import com.google.common.primitives.UnsignedInteger;

/**
 * @author Adrian Cole
 */
public class Record<D extends Map<String, Object>> extends RecordId {

   private final UnsignedInteger ttl;
   @Named("rdata")
   private final D rdata;

   @ConstructorProperties({ "zone", "fqdn", "record_type", "record_id", "ttl", "rdata" })
   protected Record(String zone, String fqdn, String type, long id, UnsignedInteger ttl, D rdata) {
      super(zone, fqdn, type, id);
      this.ttl = checkNotNull(ttl, "ttl of %s", id);
      this.rdata = checkNotNull(rdata, "rdata of %s", id);
   }

   /**
    * The current ttl of the record or zero if default for the zone
    */
   public UnsignedInteger getTTL() {
      return ttl;
   }

   /**
    * RData defining the record; corresponds to binary master format. Only
    * simple data types such as String or Integer are values.
    */
   public D getRData() {
      return rdata;
   }

   @Override
   protected ToStringHelper string() {
      return super.string().add("ttl", ttl).add("rdata", rdata);
   }

   public static <D extends Map<String, Object>> Builder<D, ?> builder() {
      return new ConcreteBuilder<D>();
   }

   public Builder<D, ?> toBuilder() {
      return new ConcreteBuilder<D>().from(this);
   }

   public abstract static class Builder<D extends Map<String, Object>, B extends Builder<D, B>> extends RecordId.Builder<B> {

      protected UnsignedInteger ttl;
      protected D rdata;

      /**
       * @see Record#getTTL()
       */
      public B ttl(UnsignedInteger ttl) {
         this.ttl = ttl;
         return self();
      }

      /**
       * @see Record#getTTL()
       */
      public B ttl(int ttl) {
         return ttl(UnsignedInteger.fromIntBits(ttl));
      }

      /**
       * @see Record#getRData()
       */
      public B rdata(D rdata) {
         this.rdata = rdata;
         return self();
      }

      public Record<D> build() {
         return new Record<D>(zone, fqdn, type, id, ttl, rdata);
      }

      @Override
      public B from(RecordId in) {
         if (in instanceof Record) {
            @SuppressWarnings("unchecked")
            Record<D> record = Record.class.cast(in);
            ttl(record.ttl).rdata(record.rdata);
         }
         return super.from(in);
      }
   }

   private static class ConcreteBuilder<D extends Map<String, Object>> extends Builder<D, ConcreteBuilder<D>> {
      protected ConcreteBuilder<D> self() {
         return this;
      }
   }
}