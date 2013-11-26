package org.jclouds.orion;

import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static org.jclouds.Constants.PROPERTY_MAX_RETRIES;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.jclouds.ContextBuilder;
import org.jclouds.concurrent.config.ExecutorServiceModule;
import org.jclouds.orion.config.constans.OrionConstantValues;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.net.HttpHeaders;
import com.google.common.reflect.TypeToken;
import com.google.inject.Module;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.squareup.okhttp.mockwebserver.QueueDispatcher;
import com.squareup.okhttp.mockwebserver.RecordedRequest;

@Test
public class OrionApiMockTest{



   private final Set<Module> modules = ImmutableSet.<Module> of(
         new ExecutorServiceModule(sameThreadExecutor(), sameThreadExecutor()));

   public OrionApi api(String uri, String provider, Properties overrides) {
      overrides.setProperty(PROPERTY_MAX_RETRIES, "1");

      return ContextBuilder.newBuilder(provider)
            .credentials(userName, password)
            .endpoint(uri)
            .overrides(overrides)
            .modules(modules)
            .buildApi(new TypeToken<OrionApi>(OrionApi.class) {});
   }

   public OrionApi api(String uri, String provider) {
      return api(uri, provider, new Properties());
   }

   public static MockWebServer mockOpenStackServer() throws IOException {
      MockWebServer server = new MockWebServer();
      //   URL url = server.getUrl("");
      //      server.setDispatcher(getURLReplacingQueueDispatcher(url));
      return server;
   }

   /**
    * there's no built-in way to defer evaluation of a response header, hence
    * this method, which allows us to send back links to the mock server.
    */
   public static QueueDispatcher getURLReplacingQueueDispatcher(final URL url) {
      final QueueDispatcher dispatcher = new QueueDispatcher() {
         protected final BlockingQueue<MockResponse> responseQueue = new LinkedBlockingQueue<MockResponse>();

         @Override
         public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            MockResponse response = responseQueue.take();
            if (response.getBody() != null) {
               String newBody = new String(response.getBody()).replace(":\"URL", ":\"" + url.toString());
               response = response.setBody(newBody);
            }
            return response;
         }

         @Override
         public void enqueueResponse(MockResponse response) {
            responseQueue.add(response);
         }
      };

      return dispatcher;
   }

   private OrionApi orionApi = null;
   private MockWebServer mockServer= null;
   private final String MOCKSERVER_PATH = "/";

   private final String userName = "userName";
   private final String password= "password";

   private final String container = "Container";

   /*Mock answers*/

   private final String POST = "POST ";
   private final String PUT = "PUT ";
   private final String DELETE = "DELETE ";
   private final String GET = "GET ";
   private final String HTTP_VERSION = " HTTP/1.1";


   private static MockResponse getFormAuthenticationResponse(){
      return new MockResponse().setStatus("200 OK").setHeader(HttpHeaders.SET_COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").setHeader(HttpHeaders.EXPIRES, "Thu, 01 Jan 1970 00:00:00 GMT").setHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=ISO-8859-1").setHeader(HttpHeaders.TRANSFER_ENCODING, "chunked").setHeader(HttpHeaders.SERVER, "Jetty(8.1.10.v20130312)").setBody("{\"Name\":\"timur\",\"uid\":\"A\",\"Location\":\"/users/A\",\"login\":\"timur\",\"lastlogintimestamp\":1385499643546}");
   }

   private static MockResponse getMockOKResponse(){
      return new MockResponse().setStatus("200 OK");
   }

   @BeforeSuite
   public void testSetup() throws IOException, InterruptedException{
      mockServer = new MockWebServer();
      mockServer.enqueue(getFormAuthenticationResponse());
      mockServer.play();
      orionApi = api(mockServer.getUrl("/").toString(), "orionblob");
      orionApi.formLogin(userName, password);
      //Check login field
      Assert.assertEquals(mockServer.getRequestCount(), 1);
      Assert.assertEquals(mockServer.takeRequest().getRequestLine(), POST + MOCKSERVER_PATH + OrionConstantValues.ORION_AUTH_PATH + HTTP_VERSION);
   }

   @AfterSuite
   public void testTeardown() throws IOException{
      mockServer.shutdown();
   }
   @Test
   public void containerExistsTest() throws InterruptedException {
      mockServer.enqueue(getMockOKResponse());
      orionApi.containerExists(userName, container);
      Assert.assertEquals(mockServer.getRequestCount(), 1);
      Assert.assertEquals(mockServer.takeRequest().getRequestLine(), POST + MOCKSERVER_PATH + OrionConstantValues.ORION_WORKSPACE_PATH + userName + "" + container + HTTP_VERSION);

   }

   public void createContainerAsAProjectTest() {

   }

   public void listContainers() {

   }

   public void listContainerContents() {

   }

   public void deleteContainerMetadata() {

   }

   public void deleteContainer() {

   }

   public void formLogin() {

   }

   public void blobExists() {

   }

   public void removeBlob() {

   }

   public void createBlob() {

   }

   public void createFolder() {

   }

   public void getBlob() {
   }

   public void getBlobContents() {
   }

   public void createMetadataFolder() {
   }

   public void createMetadata() {
   }

   public void metadaFolderExits() {
   }

   public void putMetadata() {
   }

   public void getMetadata() {
   }

   public void deleteGivenPath() {
   }

   public void list() {
   }

}
