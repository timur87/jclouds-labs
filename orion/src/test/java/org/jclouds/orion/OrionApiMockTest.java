package org.jclouds.orion;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static org.jclouds.Constants.PROPERTY_MAX_RETRIES;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.orion.config.constans.OrionConstantValues;
import org.jclouds.orion.config.constans.OrionHttpFields;
import org.jclouds.orion.domain.OrionBlob;
import org.jclouds.orion.domain.internal.MutableBlobPropertiesImpl;
import org.jclouds.orion.domain.internal.OrionBlobImpl;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.inject.Module;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

@Test
public class OrionApiMockTest {

   private final Set<Module> modules = ImmutableSet.<Module> of(new ExecutorServiceModule(sameThreadExecutor(),
         sameThreadExecutor()));

   public OrionApi api(String uri, String provider, Properties overrides) {
      overrides.setProperty(PROPERTY_MAX_RETRIES, "1");

      return ContextBuilder.newBuilder(provider).credentials(this.USER_NAME, this.password).endpoint(uri)
            .overrides(overrides).modules(this.modules).buildApi(new TypeToken<OrionApi>(OrionApi.class) {
            });
   }

   public OrionApi api(String uri, String provider) {
      return this.api(uri, provider, new Properties());
   }

   public static MockWebServer mockOpenStackServer() throws IOException {
      MockWebServer server = new MockWebServer();
      // URL url = server.getUrl("");
      // server.setDispatcher(getURLReplacingQueueDispatcher(url));
      return server;
   }

   private OrionApi orionApi = null;
   private MockWebServer mockServer = null;
   private static final String MOCKSERVER_PATH = "/";

   private static final String USER_NAME = "userName";
   private static final String password = "password";

   private static final String CONTAINER = "Container";

   /* Mock answers */

   private static final String POST = "POST ";
   private static final String PUT = "PUT ";
   private static final String DELETE = "DELETE ";
   private static final String GET = "GET ";
   private static final String HTTP_VERSION = " HTTP/1.1";
   private static final String PARENT_PATH = "PARENT/";
   private static final String BLOB_NAME = "BLOB";

   private static MockResponse getFormAuthenticationResponse() {
      return new MockResponse()
            .setStatus("HTTP/1.1 200 OK")
            .setHeader(HttpHeaders.SET_COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/")
            .setHeader(HttpHeaders.EXPIRES, "Thu, 01 Jan 1970 00:00:00 GMT")
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=ISO-8859-1")
            .setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked")
            .setHeader(HttpHeaders.SERVER, "Jetty(8.1.10.v20130312)")
            .setBody(
                  "{\"Name\":\"timur\",\"uid\":\"A\",\"Location\":\"/users/A\",\"login\":\"timur\",\"lastlogintimestamp\":1385499643546}");
   }

   private static MockResponse getMockOKResponse() {
      return new MockResponse().setStatus("HTTP/1.1 200 OK");
   }

   private void checkSlugHeader(RecordedRequest req, String value) {

      for (String header : req.getHeaders()) {
         System.out.println(header);
         if (header.equals(OrionHttpFields.HEADER_SLUG + ": " + value)) {
            return;
         }
      }
      Assert.assertTrue(false, "no slug header");
   }

   @AfterTest
   public void testTeardown() throws IOException {
      this.mockServer.shutdown();
   }

   @BeforeTest
   public void formLogin() throws InterruptedException, IOException {
      this.mockServer = new MockWebServer();
      this.mockServer.play();
      this.mockServer.enqueue(getFormAuthenticationResponse());
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi = this.api(this.mockServer.getUrl("/").toString(), "orionblob");
      this.orionApi.containerExists(USER_NAME, CONTAINER);
      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), POST + MOCKSERVER_PATH
            + OrionConstantValues.ORION_AUTH_PATH + HTTP_VERSION);
      this.mockServer.takeRequest();
   }

   @Test
   public void containerExists() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.containerExists(OrionApiMockTest.USER_NAME, CONTAINER);
      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), GET + MOCKSERVER_PATH
            + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "?" + OrionHttpFields.QUERY_PARTS
            + "=" + OrionConstantValues.ORION_FILE_METADATA + HTTP_VERSION);
   }

   @Test
   public void createContainer() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      // this.mockServer.enqueue(getMockOKResponse());

      this.orionApi.createContainer(USER_NAME, CONTAINER);

      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), POST + MOCKSERVER_PATH + OrionConstantValues.ORION_WORKSPACE_PATH
            + USER_NAME + "/" + HTTP_VERSION);
      this.checkSlugHeader(req, CONTAINER);
   }

   @Test
   public void blobExists() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.blobExists(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME);
      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), GET + MOCKSERVER_PATH
            + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH + BLOB_NAME + "?"
            + OrionHttpFields.QUERY_PARTS + "=" + OrionConstantValues.ORION_FILE_METADATA + HTTP_VERSION);

   }

   @Test
   public void listContainers() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      JsonObject object = new JsonObject();

      this.mockServer.enqueue(getMockOKResponse().setBody(new Gson().toJson(object)));
      try {
         this.orionApi.listContainers(USER_NAME);
      } catch (IllegalStateException e) {

      }
      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), GET + MOCKSERVER_PATH
            + OrionConstantValues.ORION_WORKSPACE_PATH + USER_NAME + "/" + "?" + OrionHttpFields.QUERY_DEPTH + "="
            + "1" + HTTP_VERSION);

   }

   @Test
   public void listContainerContents() throws InterruptedException {

      this.mockServer.enqueue(getMockOKResponse());
      JsonObject object = new JsonObject();

      this.mockServer.enqueue(getMockOKResponse().setBody(new Gson().toJson(object)));
      try {
         this.orionApi.listContainerContents(USER_NAME, CONTAINER);
      } catch (IllegalStateException e) {

      }

      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), GET + MOCKSERVER_PATH
            + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "?" + OrionHttpFields.QUERY_DEPTH
            + "=" + "1" + HTTP_VERSION);

   }

   @Test
   public void deleteContainerMetadata() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.deleteContainerMetadata(USER_NAME, CONTAINER);
      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), DELETE + MOCKSERVER_PATH + OrionConstantValues.ORION_WORKSPACE_PATH
            + USER_NAME + "/" + OrionConstantValues.ORION_FILE_PATH + CONTAINER + HTTP_VERSION);
   }

   @Test
   public void deleteContainer() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());

      this.orionApi.deleteContainer(USER_NAME, CONTAINER);

      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), DELETE + MOCKSERVER_PATH + OrionConstantValues.ORION_FILE_PATH
            + USER_NAME + "/" + CONTAINER + HTTP_VERSION);

   }

   @Test
   public void removeBlob() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());

      this.orionApi.removeBlob(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME);
      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), DELETE + MOCKSERVER_PATH + OrionConstantValues.ORION_FILE_PATH
            + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH + HTTP_VERSION);

   }

   @Test
   public void createBlob() throws InterruptedException {

      OrionBlob blob = new OrionBlobImpl(new MutableBlobPropertiesImpl());
      blob.getProperties().setName(BLOB_NAME);

      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.createBlob(USER_NAME, CONTAINER, PARENT_PATH, blob);
      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), POST + MOCKSERVER_PATH + OrionConstantValues.ORION_IMPORT_PATH
            + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH + HTTP_VERSION);
      this.checkSlugHeader(req, BLOB_NAME);

   }

   @Test
   public void createFolder() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.createFolder(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME);
      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), POST + MOCKSERVER_PATH + OrionConstantValues.ORION_FILE_PATH
            + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH + HTTP_VERSION);
      this.checkSlugHeader(req, BLOB_NAME);

   }

   @Test
   public void getBlobContents() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.getBlobContents(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME);
      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), GET + MOCKSERVER_PATH
            + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH + BLOB_NAME
            + HTTP_VERSION);
   }

   @Test
   public void createMetadataFolder() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.createMetadataFolder(USER_NAME, CONTAINER, PARENT_PATH);
      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), POST + MOCKSERVER_PATH + OrionConstantValues.ORION_FILE_PATH
            + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH + HTTP_VERSION);

   }

   @Test
   public void createMetadata() throws InterruptedException {
      OrionBlob blob = new OrionBlobImpl(new MutableBlobPropertiesImpl());
      blob.getProperties().setName(BLOB_NAME);

      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.createMetadata(USER_NAME, CONTAINER, PARENT_PATH, blob);
      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), POST + MOCKSERVER_PATH + OrionConstantValues.ORION_IMPORT_PATH
            + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH + OrionConstantValues.ORION_METADATA_PATH + HTTP_VERSION);
      this.checkSlugHeader(req,
            OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(blob.getProperties().getName())));
   }

   @Test
   public void metadaFolderExits() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.metadaFolderExits(USER_NAME, CONTAINER, PARENT_PATH);
      RecordedRequest req = this.mockServer.takeRequest();
      Assert.assertEquals(req.getRequestLine(), GET + MOCKSERVER_PATH + OrionConstantValues.ORION_FILE_PATH + USER_NAME
            + "/" + CONTAINER + "/" + PARENT_PATH + OrionConstantValues.ORION_METADATA_FILE_NAME + HTTP_VERSION);
   }

   @Test
   public void putMetadata() throws InterruptedException {
      OrionBlob blob = new OrionBlobImpl(new MutableBlobPropertiesImpl());
      blob.getProperties().setName(BLOB_NAME);

      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.putMetadata(USER_NAME, CONTAINER, PARENT_PATH, blob);
      Assert.assertEquals(
            this.mockServer.takeRequest().getRequestLine(),
            PUT + MOCKSERVER_PATH + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "/"
                  + PARENT_PATH + OrionConstantValues.ORION_METADATA_PATH
                  + OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(blob.getProperties().getName()))
                  + HTTP_VERSION);
   }

   @Test
   public void getMetadata() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.getMetadata(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME);
      Assert.assertEquals(
            this.mockServer.takeRequest().getRequestLine(),
            GET + MOCKSERVER_PATH + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "/"
                  + PARENT_PATH + OrionConstantValues.ORION_METADATA_PATH
                  + OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(BLOB_NAME)) + HTTP_VERSION);
   }

   @Test
   public void deleteGivenPath() throws InterruptedException {
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.deleteGivenPath(PARENT_PATH);
      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), DELETE + MOCKSERVER_PATH + PARENT_PATH
            + HTTP_VERSION);
   }

   @Test
   public void list() throws InterruptedException {
      JsonObject object = new JsonObject();
      object.addProperty("name", BLOB_NAME);
      object.addProperty("container", CONTAINER);
      object.addProperty("parentPath", PARENT_PATH);
      this.mockServer.enqueue(getMockOKResponse().setBody(new Gson().toJson(object)));
      this.orionApi.list(USER_NAME, CONTAINER, new ListContainerOptions());
      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), GET + MOCKSERVER_PATH
            + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "?" + OrionHttpFields.QUERY_DEPTH
            + "=" + "1" + HTTP_VERSION);
   }

   public void getBlob() throws InterruptedException {
      JsonObject object = new JsonObject();
      object.addProperty("name", BLOB_NAME);
      object.addProperty("container", CONTAINER);
      object.addProperty("parentPath", PARENT_PATH);
      this.mockServer.enqueue(getMockOKResponse().setBody(new Gson().toJson(object)));
      this.mockServer.enqueue(getMockOKResponse());
      this.orionApi.getBlob(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME);

      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), GET + MOCKSERVER_PATH
            + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH
            + OrionConstantValues.ORION_METADATA_PATH + OrionUtils.getMetadataName(BLOB_NAME) + HTTP_VERSION);

      Assert.assertEquals(this.mockServer.takeRequest().getRequestLine(), GET + MOCKSERVER_PATH
            + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER + "/" + PARENT_PATH + BLOB_NAME
            + HTTP_VERSION);
   }
}
