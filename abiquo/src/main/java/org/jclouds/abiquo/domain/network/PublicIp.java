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
package org.jclouds.abiquo.domain.network;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.abiquo.AbiquoApi;
import org.jclouds.abiquo.reference.ValidationErrors;
import org.jclouds.abiquo.reference.rest.ParentLinkName;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ParseXMLWithJAXB;
import org.jclouds.rest.ApiContext;

import com.abiquo.model.enumerator.NetworkType;
import com.abiquo.model.rest.RESTLink;
import com.abiquo.server.core.infrastructure.network.PublicIpDto;
import com.abiquo.server.core.infrastructure.network.VLANNetworkDto;
import com.google.inject.TypeLiteral;

/**
 * Adds generic high level functionality to {@link PublicIpDto}.
 */
public class PublicIp extends AbstractPublicIp<PublicIpDto, PublicNetwork> {
   /**
    * Constructor to be used only by the builder.
    */
   protected PublicIp(final ApiContext<AbiquoApi> context, final PublicIpDto target) {
      super(context, target);
   }

   @Override
   public PublicNetwork getNetwork() {
      RESTLink link = checkNotNull(target.searchLink(ParentLinkName.PUBLIC_NETWORK),
            ValidationErrors.MISSING_REQUIRED_LINK + ParentLinkName.PUBLIC_NETWORK);

      HttpResponse response = context.getApi().get(link);

      ParseXMLWithJAXB<VLANNetworkDto> parser = new ParseXMLWithJAXB<VLANNetworkDto>(context.utils().xml(),
            TypeLiteral.get(VLANNetworkDto.class));

      return wrap(context, PublicNetwork.class, parser.apply(response));
   }

   @Override
   public NetworkType getNetworkType() {
      return NetworkType.PUBLIC;
   }

   @Override
   public String toString() {
      return "PublicIp [networkType=" + getNetworkType() + ", available=" + isAvailable() + ", quarantine="
            + isQuarantine() + ", id=" + getId() + ", ip=" + getIp() + ", mac=" + getMac() + ", name=" + getName()
            + ", networkName=" + getNetworkName() + "]";
   }

}
