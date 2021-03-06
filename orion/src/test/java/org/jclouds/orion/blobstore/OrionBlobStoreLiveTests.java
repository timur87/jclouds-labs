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
package org.jclouds.orion.blobstore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

/**
 * Live tests, one first needs to specify endpoint.properties before running
 * tests
 *
 *
 */
@Test(groups = "unit", testName = "OrionApiMetadataTest")
public class OrionBlobStoreLiveTests {
   // properties file name
   String propsFileName = "endpoint.properties";
   // property constants
   static final String HOST_ADDRESS = "hostaddress";
   static final String USERNAME = "username";
   static final String PASSWORD = "password";

   String blobName = "servicetemplates/http%3A%2F%2Fwww.example.org%2Fwinery%2FTEST%2Fjclouds1/test1/";
   private BlobStore blobStore;
   private final String container = "Container";
   String payload = "Payload Test String";
   private final String bibBlobName = "BigBlob";

   @BeforeSuite
   protected void setUp() throws Exception {
      final Properties props = new Properties();

      props.load(this.getClass().getClassLoader().getResourceAsStream(this.propsFileName));
      final BlobStoreContext context = ContextBuilder.newBuilder("orionblob").endpoint(props.getProperty(HOST_ADDRESS))
            .credentials(props.getProperty(USERNAME), props.getProperty(PASSWORD)).build(BlobStoreContext.class);
      // create a container in the default location
      this.blobStore = context.getBlobStore();
   }

   @BeforeMethod
   protected void createContainer() throws Exception {
      this.blobStore.createContainerInLocation(null, this.container);
   }

   @AfterMethod
   protected void deleteContainer() throws Exception {
      this.blobStore.deleteContainer(this.container);
      Assert.assertTrue(!this.blobStore.containerExists(this.container), "Container SHOULD NOT exist");
   }

   @Test
   protected void containerExists() throws Exception {

      Assert.assertTrue(this.blobStore.containerExists(this.container), "Container SHOULD exist");
      Assert.assertFalse(this.blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");
   }

   @Test
   protected void putBlob() throws Exception {

      Assert.assertTrue(this.blobStore.containerExists(this.container), "Container SHOULD exist");
      Assert.assertTrue(!this.blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      final Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      this.blobStore.putBlob(this.container, blob);
   }

   @Test
   protected void removeBlob() throws Exception {

      Assert.assertTrue(this.blobStore.containerExists(this.container), "Container SHOULD exist");
      Assert.assertTrue(!this.blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      final Blob blob = this.blobStore.blobBuilder(this.blobName).payload(new ByteArrayInputStream("".getBytes()))
            .type(StorageType.FOLDER).build();
      this.blobStore.putBlob(this.container, blob);
      Assert.assertEquals(true, this.blobStore.blobExists(this.container, this.blobName));
      this.blobStore.removeBlob(this.container, this.blobName);
      Assert.assertEquals(false, this.blobStore.blobExists(this.container, this.blobName));
   }

   @Test
   protected void blobExists() throws Exception {

      Assert.assertEquals(this.blobStore.blobExists(this.container, this.blobName), false);
      final Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      this.blobStore.putBlob(this.container, blob);
      Assert.assertEquals(this.blobStore.blobExists(this.container, this.blobName), true);
   }

   /**
    * This test requires an extra external file called BigBlob after adding it
    * this can be activated Purpose is to test a blob with a larger size
    *
    */
   @Test(enabled = false)
   // TODO requires addition of a file called BigBlob
   protected void putBigBlob() throws Exception {

      this.blobStore.createContainerInLocation(null, this.container);
      Assert.assertTrue(this.blobStore.containerExists(this.container), "Container SHOULD exist");
      Assert.assertTrue(!this.blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      final Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      final String pathName = this.getClass().getClassLoader().getResource(this.bibBlobName).getPath();
      final File testFile = new File(pathName);
      blob.setPayload(Files.asByteSource(testFile));
      this.blobStore.putBlob(this.container, blob);

      final Blob returnedBlob = this.blobStore.getBlob(this.container, this.blobName);
      final byte[] initialArray = Files.toByteArray(testFile);
      final byte[] uploadedArray = ByteStreams.toByteArray(returnedBlob.getPayload().openStream());

      if (initialArray.length != uploadedArray.length) {
         Assert.assertFalse(true, "Sizes must be the same");
      }

      for (int i = 0; i < initialArray.length; i++) {
         Assert.assertEquals(initialArray[i], uploadedArray[i], "index '" + i + "' is different");
      }

   }

   @Test
   protected void getBlobMetadata() throws Exception {

      final Blob blob = this.blobStore.blobBuilder(this.blobName).type(StorageType.BLOB).build();
      blob.setPayload(this.payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      this.blobStore.putBlob(this.container, blob);
      final BlobMetadata metadata = this.blobStore.blobMetadata(this.container, this.blobName);
      Assert.assertEquals(metadata.getUserMetadata().containsKey("test"), true, "user metadata is not there");

   }

   @Test
   protected void getBlob() throws Exception {

      final Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      this.blobStore.putBlob(this.container, blob);
      final Blob returnBlob = this.blobStore.getBlob(this.container, this.blobName);
      final ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
      ByteStreams.copy(returnBlob.getPayload().openStream(), tempStream);
      Assert.assertEquals(this.payload, new String(tempStream.toByteArray()));
   }

   @Test
   protected void listContainers() throws Exception {

      final Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      this.blobStore.putBlob(this.container, blob);

      final PageSet<? extends StorageMetadata> resultSet = this.blobStore.list();
      for (final StorageMetadata data : resultSet) {
         System.out.println(data.getName());
      }

   }

   @Test
   protected void listBlobs() throws Exception {

      final Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      this.blobStore.putBlob(this.container, blob);

      final PageSet<? extends StorageMetadata> resultSet = this.blobStore.list(this.container,
            ListContainerOptions.Builder.recursive());
      for (final StorageMetadata data : resultSet) {
         System.out.println(data.getName());
      }

   }

}
