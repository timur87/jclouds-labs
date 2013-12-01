package org.jclouds.orion.blobstore;

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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * Live tests, one first needs to specify endpoint.properties before running
 * tests
 * 
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

      props.load(this.getClass().getClassLoader().getResourceAsStream(this.propsFileName));
      BlobStoreContext context = ContextBuilder.newBuilder("orionblob").endpoint(props.getProperty(HOST_ADDRESS))
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
      Assert.assertTrue(!this.blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");
   }

   @Test
   protected void putBlob() throws Exception {

      Assert.assertTrue(this.blobStore.containerExists(this.container), "Container SHOULD exist");
      Assert.assertTrue(!this.blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      this.blobStore.putBlob(this.container, blob);
   }

   @Test
   protected void removeBlob() throws Exception {

      Assert.assertTrue(this.blobStore.containerExists(this.container), "Container SHOULD exist");
      Assert.assertTrue(!this.blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      Blob blob = this.blobStore.blobBuilder(this.blobName).payload("").type(StorageType.FOLDER).build();
      this.blobStore.putBlob(this.container, blob);
      Assert.assertEquals(true, this.blobStore.blobExists(this.container, this.blobName));
      this.blobStore.removeBlob(this.container, this.blobName);
      Assert.assertEquals(false, this.blobStore.blobExists(this.container, this.blobName));
   }

   @Test
   protected void blobExists() throws Exception {

      Assert.assertEquals(this.blobStore.blobExists(this.container, this.blobName), false);
      Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      this.blobStore.putBlob(this.container, blob);
      Assert.assertEquals(this.blobStore.blobExists(this.container, this.blobName), true);
   }

   /**
    * This test requires an extra external file called BigBlob.zip after adding
    * it this can be activated Purpose is to test a blob with a larger size
    * 
    */
   @Test
   protected void putBigBlob() throws Exception {

      this.blobStore.createContainerInLocation(null, this.container);
      Assert.assertTrue(this.blobStore.containerExists(this.container), "Container SHOULD exist");
      Assert.assertTrue(!this.blobStore.containerExists(String.valueOf(Calendar.getInstance().getTimeInMillis())),
            "Container SHOULD NOT exist");

      Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      String pathName = this.getClass().getClassLoader().getResource(this.bibBlobName).getPath();
      File testFile = new File(pathName);
      InputSupplier<FileInputStream> iStream = Files.newInputStreamSupplier(testFile);
      blob.setPayload(iStream.getInput());
      this.blobStore.putBlob(this.container, blob);

      Blob returnedBlob = this.blobStore.getBlob(this.container, this.blobName);
      byte[] initialArray = Files.toByteArray(testFile);
      byte[] uploadedArray = ByteStreams.toByteArray(returnedBlob.getPayload().getInput());

      if (initialArray.length != uploadedArray.length) {
         Assert.assertFalse(true, "Sizes must be the same");
      }

      for (int i = 0; i < initialArray.length; i++) {
         Assert.assertEquals(initialArray[i], uploadedArray[i], "index '" + i + "' is different");
      }

   }

   @Test
   protected void getBlobMetadata() throws Exception {

      Blob blob = this.blobStore.blobBuilder(this.blobName).type(StorageType.BLOB).build();
      blob.setPayload(this.payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      this.blobStore.putBlob(this.container, blob);
      BlobMetadata metadata = this.blobStore.blobMetadata(this.container, this.blobName);
      Assert.assertEquals(metadata.getUserMetadata().containsKey("test"), true, "user metadata is not there");

   }

   @Test
   protected void getBlob() throws Exception {

      Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      this.blobStore.putBlob(this.container, blob);
      Blob returnBlob = this.blobStore.getBlob(this.container, this.blobName);
      ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
      ByteStreams.copy(returnBlob.getPayload().getInput(), tempStream);
      Assert.assertEquals(this.payload, new String(tempStream.toByteArray()));
   }

   @Test
   protected void listContainers() throws Exception {

      Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      this.blobStore.putBlob(this.container, blob);

      PageSet<? extends StorageMetadata> resultSet = this.blobStore.list();
      for (StorageMetadata data : resultSet) {
         System.out.println(data.getName());
      }

   }

   @Test
   protected void listBlobs() throws Exception {

      Blob blob = this.blobStore.blobBuilder(this.blobName).build();
      blob.setPayload(this.payload);
      blob.getMetadata().getUserMetadata().put("test", "test");
      this.blobStore.putBlob(this.container, blob);

      PageSet<? extends StorageMetadata> resultSet = this.blobStore.list(this.container,
            ListContainerOptions.Builder.recursive());
      for (StorageMetadata data : resultSet) {
         System.out.println(data.getName());
      }

   }

}
