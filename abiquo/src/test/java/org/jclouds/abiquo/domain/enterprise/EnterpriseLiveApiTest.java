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

import static com.google.common.collect.Iterables.get;
import static com.google.common.collect.Iterables.isEmpty;
import static com.google.common.collect.Iterables.size;
import static org.jclouds.abiquo.reference.AbiquoTestConstants.PREFIX;
import static org.jclouds.abiquo.util.Assert.assertHasError;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import javax.ws.rs.core.Response.Status;

import org.jclouds.abiquo.domain.cloud.VirtualAppliance;
import org.jclouds.abiquo.domain.cloud.VirtualMachine;
import org.jclouds.abiquo.domain.enterprise.Enterprise.Builder;
import org.jclouds.abiquo.domain.exception.AbiquoException;
import org.jclouds.abiquo.domain.infrastructure.Datacenter;
import org.jclouds.abiquo.internal.BaseAbiquoApiLiveApiTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.abiquo.server.core.enterprise.DatacentersLimitsDto;
import com.abiquo.server.core.enterprise.EnterpriseDto;

/**
 * Live integration tests for the {@link Enterprise} domain class.
 */
@Test(groups = "api", testName = "EnterpriseLiveApiTest")
public class EnterpriseLiveApiTest extends BaseAbiquoApiLiveApiTest {
   private Enterprise enterprise;

   private Limits limits;

   @BeforeClass
   public void setupEnterprise() {
      enterprise = Enterprise.Builder.fromEnterprise(env.enterprise).build();
      enterprise.setName(PREFIX + "-enterprise-test");
      enterprise.save();

      limits = enterprise.allowDatacenter(env.datacenter);
      assertNotNull(limits);

      DatacentersLimitsDto limitsDto = env.enterpriseApi.getLimits(enterprise.unwrap(), env.datacenter.unwrap());
      assertNotNull(limitsDto);
      assertEquals(limitsDto.getCollection().size(), 1);
   }

   @AfterClass
   public void tearDownEnterprise() {
      enterprise.prohibitDatacenter(env.datacenter);

      try {
         // If a datacenter is not allowed, the limits for it can not be
         // retrieved
         env.enterpriseApi.getLimits(enterprise.unwrap(), env.datacenter.unwrap());
      } catch (AbiquoException ex) {
         assertHasError(ex, Status.CONFLICT, "ENTERPRISE-10");
      }

      Iterable<Datacenter> allowed = enterprise.listAllowedDatacenters();
      assertNotNull(allowed);
      assertTrue(isEmpty(allowed));

      enterprise.delete();
   }

   public void testUpdate() {
      enterprise.setName("Updated Enterprise");
      enterprise.update();

      // Recover the updated enterprise
      EnterpriseDto updated = env.enterpriseApi.getEnterprise(enterprise.getId());

      assertEquals(updated.getName(), "Updated Enterprise");
   }

   public void testCreateRepeated() {
      Enterprise repeated = Builder.fromEnterprise(enterprise).build();

      try {
         repeated.save();
         fail("Should not be able to create enterprises with the same name");
      } catch (AbiquoException ex) {
         assertHasError(ex, Status.CONFLICT, "ENTERPRISE-4");
      }
   }

   public void testAllowTwiceWorks() {
      // Allow the datacenter again and check that the configuration has not
      // changed
      Limits limits = enterprise.allowDatacenter(env.datacenter);
      assertNotNull(limits);

      DatacentersLimitsDto limitsDto = env.enterpriseApi.getLimits(enterprise.unwrap(), env.datacenter.unwrap());
      assertNotNull(limitsDto);
      assertEquals(limitsDto.getCollection().size(), 1);
   }

   public void testListLimits() {
      Iterable<Limits> allLimits = enterprise.listLimits();
      assertNotNull(allLimits);
      assertEquals(size(allLimits), 1);
   }

   public void testUpdateInvalidLimits() {
      // CPU soft remains to 0 => conflict because hard is smaller
      limits.setCpuCountHardLimit(2);

      try {
         limits.update();
      } catch (AbiquoException ex) {
         assertHasError(ex, Status.BAD_REQUEST, "CONSTR-LIMITRANGE");
      }
   }

   public void testUpdateLimits() {
      limits.setCpuCountLimits(4, 5);
      limits.update();

      DatacentersLimitsDto limitsDto = env.enterpriseApi.getLimits(enterprise.unwrap(), env.datacenter.unwrap());
      assertNotNull(limitsDto);
      assertEquals(limitsDto.getCollection().size(), 1);
      assertEquals(limitsDto.getCollection().get(0).getCpuCountHardLimit(), 5);
      assertEquals(limitsDto.getCollection().get(0).getCpuCountSoftLimit(), 4);
   }

   public void testListAllowedDatacenters() {
      Iterable<Datacenter> allowed = enterprise.listAllowedDatacenters();

      assertNotNull(allowed);
      assertFalse(isEmpty(allowed));
      assertEquals(get(allowed, 0).getId(), env.datacenter.getId());
   }

   public void testListVirtualMachines() {
      Iterable<VirtualMachine> machines = env.defaultEnterprise.listVirtualMachines();
      assertTrue(size(machines) > 0);
   }

   public void testListVirtualAppliances() {
      Iterable<VirtualAppliance> vapps = env.defaultEnterprise.listVirtualAppliances();
      assertTrue(size(vapps) > 0);
   }
}
