package org.jclouds.orion;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * Live tests, one first needs to specify endpoint.properties before running tests
 * @author timur
 *
 */
@Test(groups = "unit", testName = "OrionApiMetadataTest")
public class OrionBlobStoreLiveTests {
   // properties file name
   String propsFileName = "endpoint.properties";
   // property constants
   final static String HOST_ADDRESS = "hostaddress";
   final static String USERNAME = "username";
   final static String PASSWORD = "password";

   String blobName = "servicetemplates/http%3A%2F%2Fwww.example.org%2Fwinery%2FTEST%2Fjclouds1/test1/";
   private BlobStore blobStore;
   private final String container = "Container";
   String payload = "Payload Test String";
   private final String bibBlobName = "BigBlob.zip";

   @BeforeSuite
   protected void setUp() throws Exception {
      Properties props = new Properties();

      props.load(this.getClass().getClassLoader().getResourceAsStream(propsFileName));
      BlobStoreContext context = ContextBuilder.newBuilder("orionblob").endpoint(props.getProperty(HOST_ADDRESS))
            .credentials(props.getProperty(USERNAME), props.getProperty(PASSWORD)).build(BlobStoreContext.class);
      // create a container in the default location
      blobStore = context.getBlobStore();
   }

   @AfterTest
   protected void tearDown() throws Exception {
      blobStore.deleteContainer(container);
   }

   @Test
   protected void createContainer() throws Exception {
      blobStore.createContainerInLocation(null, container);
   }

   @Test
   protected void deleteContainer() throws Exception {

      blobStore.deleteContainer(container);
      blobStore.createContainerInLocation(null, container);
      Assert.assertTrue(blobStore.containerExists(container), "Container SHOULD exist");
      Assert.assertTrue(!blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");
   }

   @Test
   protected void containerExists() throws Exception {

      blobStore.createContainerInLocation(null, container);
      Assert.assertTrue(blobStore.containerExists(container), "Container SHOULD exist");
      Assert.assertTrue(!blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");
   }

   @Test
   protected void putBlob() throws Exception {

      blobStore.createContainerInLocation(null, container);
      Assert.assertTrue(blobStore.containerExists(container), "Container SHOULD exist");
      Assert.assertTrue(!blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      Blob blob = blobStore.blobBuilder(blobName).build();
      blob.setPayload(payload);
      blobStore.putBlob(container, blob);
   }

   @Test
   protected void removeBlob() throws Exception {

      blobStore.createContainerInLocation(null, container);
      Assert.assertTrue(blobStore.containerExists(container), "Container SHOULD exist");
      Assert.assertTrue(!blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      Blob blob = blobStore.blobBuilder(blobName).payload("").type(StorageType.FOLDER).build();
      blobStore.putBlob(container, blob);
      Assert.assertEquals(true, blobStore.blobExists(container, blobName));
      blobStore.removeBlob(container, blobName);
      Assert.assertEquals(false, blobStore.blobExists(container, blobName));
   }

   @Test
   protected void blobExists() throws Exception {
      blobStore.deleteContainer(container);
      blobStore.createContainerInLocation(null, container);

      Assert.assertEquals(blobStore.blobExists(container, blobName), false);
      Blob blob = blobStore.blobBuilder(blobName).build();
      blob.setPayload(payload);
      blobStore.putBlob(container, blob);
      Assert.assertEquals(blobStore.blobExists(container, blobName), true);
   }

   /**
    * This test requires an extra external file called BigBlob.zip after adding it this can be activated
    * 
    */
   //@Test
   protected void putBigBlob() throws Exception {

      blobStore.createContainerInLocation(null, container);
      Assert.assertTrue(blobStore.containerExists(container), "Container SHOULD exist");
      Assert.assertTrue(!blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      Blob blob = blobStore.blobBuilder(blobName).build();
      String pathName = this.getClass().getClassLoader().getResource(bibBlobName).getPath();
      File testFile = new File(pathName);
      InputSupplier<FileInputStream> iStream = Files.newInputStreamSupplier(testFile);
      blob.setPayload(iStream.getInput());
      blobStore.putBlob(container, blob);


      Blob returnedBlob = blobStore.getBlob(container, blobName);
      byte[] initialArray = Files.toByteArray(testFile);
      byte[] uploadedArray = ByteStreams.toByteArray(returnedBlob.getPayload().getInput());

      if(initialArray.length != uploadedArray.length){
         Assert.assertFalse(true,"Sizes must be the same");
      }

      for(int i = 0 ; i < initialArray.length ; i++){
         Assert.assertEquals(initialArray[i],uploadedArray[i], "index '"+ i + "' is different");
      }

   }

   @Test
   protected void getBlobMetadata() throws Exception {

      blobStore.createContainerInLocation(null, container);

      Blob blob = blobStore.blobBuilder(blobName).type(StorageType.FOLDER).build();
      blob.setPayload(payload);

      blobStore.putBlob(container, blob);
      blob = blobStore.blobBuilder(blobName).type(StorageType.FOLDER).build();
      blob.setPayload(payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      blobStore.putBlob(container, blob);

      BlobMetadata metadata = blobStore.blobMetadata(container, blobName);
      Assert.assertEquals(metadata.getUserMetadata().containsKey("test"), true, "user metadata is not there");

   }

   @Test
   protected void getBlob() throws Exception {

      blobStore.createContainerInLocation(null, container);

      Blob blob = blobStore.blobBuilder(blobName).build();
      blob.setPayload(payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      blobStore.putBlob(container, blob);
      Blob returnBlob = blobStore.getBlob(container, blobName);
      ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
      ByteStreams.copy(returnBlob.getPayload().getInput(), tempStream);
      Assert.assertEquals(payload, new String(tempStream.toByteArray()));
   }

   @Test
   protected void listContainers() throws Exception {

      blobStore.createContainerInLocation(null, container);

      Blob blob = blobStore.blobBuilder(blobName).build();
      blob.setPayload(payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      blobStore.putBlob(container, blob);

      PageSet<? extends StorageMetadata> resultSet = blobStore.list();
      for (StorageMetadata data : resultSet) {
         System.out.println(data.getName());
      }

   }

   @Test
   protected void listBlobs() throws Exception {

      blobStore.createContainerInLocation(null, container);

      Blob blob = blobStore.blobBuilder(blobName).build();
      blob.setPayload(payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      blobStore.putBlob(container, blob);

      PageSet<? extends StorageMetadata> resultSet = blobStore.list(container,
            ListContainerOptions.Builder.recursive());
      for (StorageMetadata data : resultSet) {
         System.out.println(data.getName());
      }

   }

}
