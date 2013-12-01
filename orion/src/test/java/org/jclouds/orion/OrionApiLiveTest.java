/*******************************************************************************
 * Copyright (c) 2013 University of Stuttgart.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and the Apache License 2.0 which both accompany this distribution,
 * and are available at http://www.eclipse.org/legal/epl-v10.html
 * and http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *    Timur Sungur - initial API and implementation
 *******************************************************************************/

package org.jclouds.orion;

import java.util.Calendar;

import org.jclouds.apis.BaseApiLiveTest;

public class OrionApiLiveTest extends BaseApiLiveTest<OrionApi> {

   void createContainerTest() {
      // Invokable<?, ?> method = Reflection2.method(OrionApi.class,
      // "listContainers", ListOptions[].class);
      this.api.createContainer("Container" + Calendar.getInstance().getTime(), "");
   }

   @Override
   public OrionProviderMetadata createProviderMetadata() {
      return new OrionProviderMetadata();
   }
}
