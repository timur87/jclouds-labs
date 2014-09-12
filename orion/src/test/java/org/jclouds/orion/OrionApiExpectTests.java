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

package org.jclouds.orion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import org.jclouds.blobstore.options.ListContainerOptions;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.jclouds.orion.config.constans.OrionConstantValues;
import org.jclouds.orion.config.constans.OrionHttpFields;
import org.jclouds.orion.domain.Attributes;
import org.jclouds.orion.domain.OrionBlob;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;
import org.jclouds.orion.domain.internal.AttributesImpl;
import org.jclouds.orion.domain.internal.MutableBlobPropertiesImpl;
import org.jclouds.orion.domain.internal.OrionBlobImpl;
import org.jclouds.orion.domain.internal.OrionSpecificFileMetadataImpl;
import org.jclouds.rest.internal.BaseRestApiExpectTest;
import org.jclouds.util.Strings2;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.collections.Maps;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.mockwebserver.MockWebServer;

public class OrionApiExpectTests extends BaseRestApiExpectTest<OrionApi> {

   public OrionApiExpectTests() {
      provider = "orionblob";
      credential = OrionApiExpectTests.PASSWORD;
      identity = OrionApiExpectTests.USER_NAME;
   }

   public static MockWebServer mockOpenStackServer() throws IOException {
      final MockWebServer server = new MockWebServer();
      // URL url = server.getUrl("");
      // server.setDispatcher(getURLReplacingQueueDispatcher(url));
      return server;
   }

   private static final String USER_NAME = "userName";
   private static final String PASSWORD = "password";
   private static final String CONTAINER = "Container";

   private static final String PARENT_PATH = "PARENT/";
   private static final String BLOB_NAME = "BLOB";
   private static final String SESSION_VALUE = "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/";
   private static final String BLOB_CONTENTS = "BLOB_CONTENTS";
   private static final String LIST_RESPONSE = "list-response.json";

   protected HttpRequest.Builder<?> getBuilder() {
      return HttpRequest.builder().method(HttpMethod.GET);
   }

   protected HttpRequest.Builder<?> postBuilder() {
      return HttpRequest.builder().method("POST");
   }

   protected HttpRequest.Builder<?> deleteBuilder() {
      return HttpRequest.builder().method("DELETE");
   }

   protected HttpRequest.Builder<?> putBuilder() {
      return HttpRequest.builder().method("PUT").addHeader("Accept", MediaType.APPLICATION_JSON)
            .addHeader("Authorization", "Basic aWRlbnRpdHk6Y3JlZGVudGlhbA==");
   }

   protected HttpResponse.Builder<?> responseBuilder() {
      return HttpResponse.builder().statusCode(200).message("OK");
   }

   private HttpResponse getFormAuthenticationResponse() {
      return responseBuilder()
            .addHeader(HttpHeaders.SET_COOKIE, SESSION_VALUE)
            .addHeader(HttpHeaders.EXPIRES, "Thu, 01 Jan 1970 00:00:00 GMT")
            .addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=ISO-8859-1")
            .addHeader(HttpHeaders.TRANSFER_ENCODING, "chunked")
            .addHeader(HttpHeaders.SERVER, "Jetty(8.1.10.v20130312)")
            .payload(
                  ByteSource
                        .wrap("{\"Name\":\"timur\",\"uid\":\"A\",\"Location\":\"/users/A\",\"login\":\"timur\",\"lastlogintimestamp\":1385499643546}"
                              .getBytes())).build();
   }

   public OrionApi authenticate(HttpRequest req, HttpResponse res) throws IOException, InterruptedException {
      final Map<HttpRequest, HttpResponse> map = Maps.newHashMap();
      map.put(req, res);
      return authenticate(map);
   }

   public OrionApi authenticate(Map<HttpRequest, HttpResponse> map) throws IOException, InterruptedException {
      map.put(
            postBuilder().endpoint(OrionConstantValues.END_POINT + OrionConstantValues.ORION_AUTH_PATH)
                  .addFormParam(OrionHttpFields.USERNAME, USER_NAME).addFormParam(OrionHttpFields.PASSWORD, PASSWORD)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION).build(),
            getFormAuthenticationResponse());
      return requestsSendResponses(map);
   }

   @Test
   public void authenticate() throws IOException, InterruptedException {
      final OrionApi orionApi = requestSendsResponse(
            postBuilder().endpoint(OrionConstantValues.END_POINT + OrionConstantValues.ORION_AUTH_PATH)
                  .addFormParam(OrionHttpFields.USERNAME, USER_NAME).addFormParam(OrionHttpFields.PASSWORD, PASSWORD)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION).build(),
            getFormAuthenticationResponse());
      orionApi.formLogin(USER_NAME, PASSWORD);
   }

   @Test
   public void containerExists() throws InterruptedException, IOException {
      final OrionApi orionApi = authenticate(
            getBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/"
                              + CONTAINER + "?" + OrionHttpFields.QUERY_PARTS + "="
                              + OrionConstantValues.ORION_FILE_METADATA)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());
      Assert.assertTrue(orionApi.containerExists(OrionApiExpectTests.USER_NAME, CONTAINER));

   }

   @Test
   public void createContainer() throws InterruptedException, IOException {
      final OrionApi api = authenticate(
            postBuilder()
                  .endpoint(OrionConstantValues.END_POINT + OrionConstantValues.ORION_WORKSPACE_PATH + USER_NAME + "/")
                  .addHeader(OrionHttpFields.ORION_ECLIPSE_WEB_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(OrionHttpFields.HEADER_SLUG, CONTAINER)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());

      Assert.assertTrue(api.createContainer(USER_NAME, CONTAINER));
   }

   @Test
   public void blobExists() throws InterruptedException, IOException {
      final OrionApi orionApi = authenticate(
            getBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/"
                              + CONTAINER + "/" + PARENT_PATH + BLOB_NAME + "?" + OrionHttpFields.QUERY_PARTS + "="
                              + OrionConstantValues.ORION_FILE_METADATA)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());
      Assert.assertTrue(orionApi.blobExists(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME));
   }

   @Test
   public void listContainers() throws InterruptedException, IOException {
      final JsonObject object = new JsonObject();
      new Gson().toJson(object);
      try {
         final OrionApi orionApi = authenticate(
               getBuilder()
                     .endpoint(
                           OrionConstantValues.END_POINT + OrionConstantValues.ORION_WORKSPACE_PATH + USER_NAME + "/"
                                 + "?" + OrionHttpFields.QUERY_DEPTH + "=" + "1")
                     .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                     .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
               responseBuilder().payload(ByteSource.wrap(new Gson().toJson(object).getBytes())).build());
         Assert.assertNotNull(orionApi.listContainers(USER_NAME));
      } catch (final IllegalStateException e) {

      }

   }

   @Test
   public void listContainerContents() throws InterruptedException, IOException {
      final JsonObject object = new JsonObject();
      new Gson().toJson(object);
      try {
         final OrionApi orionApi = authenticate(
               getBuilder()
                     .endpoint(
                           OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/"
                                 + CONTAINER + "?" + OrionHttpFields.QUERY_DEPTH + "=" + "1")
                     .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                     .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
               responseBuilder().payload(ByteSource.wrap(new Gson().toJson(object).getBytes())).build());
         Assert.assertNotNull(orionApi.listContainerContents(USER_NAME, CONTAINER));
      } catch (final IllegalStateException e) {

      }
   }

   @Test
   public void deleteContainerMetadata() throws InterruptedException, IOException {
      final OrionApi orionApi = authenticate(
            deleteBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_WORKSPACE_PATH + USER_NAME + "/"
                              + OrionConstantValues.ORION_FILE_PATH + CONTAINER)
                  .addHeader(OrionHttpFields.ORION_ECLIPSE_WEB_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());
      Assert.assertTrue(orionApi.deleteContainerViaWorkspaceApi(USER_NAME, CONTAINER));
   }

   @Test
   public void deleteContainer() throws InterruptedException, IOException {
      final OrionApi orionApi = authenticate(
            deleteBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/"
                              + CONTAINER)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());
      Assert.assertTrue(orionApi.deleteContainer(USER_NAME, CONTAINER));
   }

   @Test
   public void removeBlob() throws InterruptedException, IOException {
      final OrionApi orionApi = authenticate(
            deleteBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/"
                              + CONTAINER + "/" + PARENT_PATH)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());

      Assert.assertTrue(orionApi.removeBlob(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME));
   }

   @Test
   public void createBlob() throws InterruptedException, IOException {
      final OrionApi orionApi = authenticate(
            postBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_IMPORT_PATH + USER_NAME + "/"
                              + CONTAINER + "/" + PARENT_PATH)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(OrionHttpFields.ORION_XFER_OPTIONS, OrionConstantValues.XFER_RAW)
                  .addHeader(OrionHttpFields.HEADER_SLUG, BLOB_NAME)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());

      final OrionBlob blob = new OrionBlobImpl(new MutableBlobPropertiesImpl());
      blob.getProperties().setName(BLOB_NAME);
      Assert.assertTrue(orionApi.createBlob(USER_NAME, CONTAINER, PARENT_PATH, blob));

   }

   @Test
   public void createFolder() throws InterruptedException, IOException {
      final OrionSpecificFileMetadata orionSpecificFileMetadata = new OrionSpecificFileMetadataImpl();
      orionSpecificFileMetadata.setDirectory(true);
      orionSpecificFileMetadata.setLocalTimeStamp((long) 0);
      orionSpecificFileMetadata.setContentLegth((long) 0);
      final Attributes attr = new AttributesImpl();
      attr.setReadOnly(true);
      attr.setHidden(true);
      orionSpecificFileMetadata.setAttributes(attr);
      final String payload = new Gson().toJson(orionSpecificFileMetadata);
      final HttpRequest req = postBuilder()
            .endpoint(
                  OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER
                        + "/" + PARENT_PATH)
            .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
            .addHeader(OrionHttpFields.HEADER_SLUG, BLOB_NAME)
            .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/")
            .payload(ByteSource.wrap(payload.getBytes())).build();
      req.getPayload().getContentMetadata().setContentLength((long) payload.length());
      req.getPayload().getContentMetadata().setContentType(MediaType.APPLICATION_JSON);
      final OrionApi orionApi = authenticate(req, responseBuilder().build());
      Assert.assertTrue(orionApi.createFolder(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME));
   }

   @Test
   public void getBlobContents() throws InterruptedException, IOException {

      final HttpRequest req = getBuilder()
            .endpoint(
                  OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER
                        + "/" + PARENT_PATH + BLOB_NAME)
            .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
            .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build();
      final HttpResponse res = responseBuilder().payload(ByteSource.wrap(BLOB_CONTENTS.getBytes())).build();
      res.getPayload().getContentMetadata().setContentLength((long) BLOB_CONTENTS.length());
      res.getPayload().getContentMetadata().setContentType(MediaType.TEXT_PLAIN);
      final OrionApi orionApi = authenticate(req, res);
      Assert.assertEquals(
            Strings2.toStringAndClose(orionApi.getBlobContents(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME)
                  .getPayload().openStream()), BLOB_CONTENTS);
   }

   @Test
   public void createMetadataFolder() throws InterruptedException, IOException {

      final OrionSpecificFileMetadata orionSpecificFileMetadata = new OrionSpecificFileMetadataImpl();
      orionSpecificFileMetadata.setDirectory(true);
      orionSpecificFileMetadata.setLocalTimeStamp((long) 0);
      orionSpecificFileMetadata.setContentLegth((long) 0);
      final Attributes attr = new AttributesImpl();
      attr.setReadOnly(true);
      attr.setHidden(true);
      orionSpecificFileMetadata.setAttributes(attr);
      final String payload = new Gson().toJson(orionSpecificFileMetadata);
      final HttpRequest req = postBuilder()
            .endpoint(
                  OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER
                        + "/" + PARENT_PATH)
            .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
            .addHeader(OrionHttpFields.HEADER_SLUG, OrionConstantValues.ORION_METADATA_FILE_NAME)
            .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/")
            .payload(ByteSource.wrap(payload.getBytes())).build();
      req.getPayload().getContentMetadata().setContentLength((long) payload.length());
      req.getPayload().getContentMetadata().setContentType(MediaType.APPLICATION_JSON);
      final OrionApi orionApi = authenticate(req, responseBuilder().build());

      Assert.assertTrue(orionApi.createMetadataFolder(USER_NAME, CONTAINER, PARENT_PATH));

   }

   @Test(enabled = false)
   // metadata contains a timestamp which cannot be predicted beforehand
   public void createMetadata() throws InterruptedException, IOException {
      final OrionBlob blob = new OrionBlobImpl(new MutableBlobPropertiesImpl());
      blob.getProperties().setName(BLOB_NAME);
      // blob.getProperties().se
      final HttpRequest req = postBuilder()
            .endpoint(
                  OrionConstantValues.END_POINT + OrionConstantValues.ORION_IMPORT_PATH + USER_NAME + "/" + CONTAINER
                        + "/" + PARENT_PATH + OrionConstantValues.ORION_METADATA_PATH)
            .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
            .addHeader(OrionHttpFields.ORION_XFER_OPTIONS, OrionConstantValues.XFER_RAW)
            .addHeader(OrionHttpFields.HEADER_SLUG, OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(BLOB_NAME)))
            .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build();
      req.setPayload(new Gson().toJson(blob.getProperties()));
      req.getPayload().getContentMetadata().setContentType(MediaType.APPLICATION_JSON);
      final OrionApi orionApi = authenticate(req, responseBuilder().build());
      Assert.assertTrue(orionApi.createMetadata(USER_NAME, CONTAINER, PARENT_PATH, blob));

   }

   @Test
   public void metadaFolderExits() throws InterruptedException, IOException {
      final OrionApi orionApi = authenticate(
            getBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/"
                              + CONTAINER + "/" + PARENT_PATH + OrionConstantValues.ORION_METADATA_FILE_NAME)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());
      Assert.assertTrue(orionApi.metadaFolderExits(USER_NAME, CONTAINER, PARENT_PATH));
   }

   @Test(enabled = false)
   // metadata contains a timestamp which cannot be predicted beforehand
   public void putMetadata() throws InterruptedException, IOException {
      final OrionBlob blob = new OrionBlobImpl(new MutableBlobPropertiesImpl());
      blob.getProperties().setName(BLOB_NAME);
      // blob.getProperties().se
      final HttpRequest req = postBuilder()
            .endpoint(
                  OrionConstantValues.END_POINT + OrionConstantValues.ORION_IMPORT_PATH + USER_NAME + "/" + CONTAINER
                        + "/" + PARENT_PATH + OrionConstantValues.ORION_METADATA_PATH)
            .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
            .addHeader(OrionHttpFields.ORION_XFER_OPTIONS, OrionConstantValues.XFER_RAW)
            .addHeader(OrionHttpFields.HEADER_SLUG, OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(BLOB_NAME)))
            .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build();
      req.setPayload(new Gson().toJson(blob.getProperties()));
      req.getPayload().getContentMetadata().setContentType(MediaType.APPLICATION_JSON);
      final OrionApi orionApi = authenticate(req, responseBuilder().build());
      Assert.assertTrue(orionApi.putMetadata(USER_NAME, CONTAINER, PARENT_PATH, blob));

   }

   @Test
   public void getMetadata() throws InterruptedException, IOException {
      final OrionBlob blob = new OrionBlobImpl(new MutableBlobPropertiesImpl());
      blob.getProperties().setName(BLOB_NAME);
      // blob.getProperties().se
      final HttpRequest req = getBuilder()
            .endpoint(
                  OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/" + CONTAINER
                        + "/" + PARENT_PATH + OrionConstantValues.ORION_METADATA_PATH
                        + OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(BLOB_NAME)))
            .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
            .addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build();
      final OrionApi orionApi = authenticate(req,
            responseBuilder().payload(ByteSource.wrap(new Gson().toJson(blob.getProperties()).getBytes())).build());
      Assert.assertEquals(orionApi.getMetadata(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME).getName(), blob
            .getProperties().getName());
   }

   @Test
   public void deleteGivenPath() throws InterruptedException, IOException {
      final OrionApi orionApi = authenticate(
            deleteBuilder().endpoint(OrionConstantValues.END_POINT + PARENT_PATH)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().build());

      Assert.assertTrue(orionApi.deleteGivenPath(PARENT_PATH));
   }

   @Test
   public void list() throws InterruptedException, IOException {
      final ByteSource fileSource = new ByteSource() {

         @Override
         public InputStream openStream() throws IOException {
            return getClass().getClassLoader().getResourceAsStream(LIST_RESPONSE);
         }
      };
      final String listResposne = fileSource.asCharSource(Charsets.UTF_8).read();
      final OrionApi orionApi = authenticate(
            getBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/"
                              + CONTAINER + "?" + OrionHttpFields.QUERY_DEPTH + "=" + "1")
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().payload(ByteSource.wrap(listResposne.getBytes())).build());

      Assert.assertEquals(orionApi.list(USER_NAME, CONTAINER, new ListContainerOptions()).size(), 1);
   }

   @Test
   public void getBlob() throws InterruptedException, IOException {
      final Map<HttpRequest, HttpResponse> map = Maps.newHashMap();
      final OrionBlob blob = new OrionBlobImpl(new MutableBlobPropertiesImpl());
      blob.getProperties().setName(BLOB_NAME);
      blob.setPayload(BLOB_CONTENTS);
      blob.getProperties().setContainer(CONTAINER);
      blob.getProperties().setParentPath(PARENT_PATH);
      // blob.getProperties().se
      map.put(
            getBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME + "/"
                              + CONTAINER + "/" + PARENT_PATH + OrionConstantValues.ORION_METADATA_PATH
                              + OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(BLOB_NAME)))
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().payload(ByteSource.wrap(new Gson().toJson(blob.getProperties()).getBytes())).build());

      map.put(
            getBuilder()
                  .endpoint(
                        OrionConstantValues.END_POINT + OrionConstantValues.ORION_FILE_PATH + USER_NAME
                              + OrionConstantValues.ORION_USER_CONTENT_ENDING + "/" + CONTAINER + "/" + PARENT_PATH
                              + BLOB_NAME)
                  .addHeader(OrionHttpFields.ORION_VERSION_FIELD, OrionConstantValues.ORION_VERSION)
                  .addHeader(HttpHeaders.COOKIE, "JSESSIONID=16d6pe0u4sqxy10s76y6xnvgng;Path=/").build(),
            responseBuilder().payload(blob.getPayload()).build());

      final OrionApi orionApi = authenticate(map);
      Assert.assertEquals(
            Strings2.toStringAndClose(orionApi.getBlob(USER_NAME, CONTAINER, PARENT_PATH, BLOB_NAME).getPayload()
                  .openStream()), BLOB_CONTENTS);

   }

   @Override
   protected org.jclouds.rest.internal.BaseRestApiExpectTest.HttpRequestComparisonType compareHttpRequestAsType(
         HttpRequest input) {
      if ((input.getPayload() == null) || (input.getPayload().getContentMetadata() == null)
            || (input.getPayload().getContentMetadata().getContentType() == null)) {
         return super.compareHttpRequestAsType(input);
      } else if (input.getPayload().getContentMetadata().getContentType().equals(MediaType.APPLICATION_JSON)) {
         return HttpRequestComparisonType.JSON;
      } else if (input.getPayload().getContentMetadata().getContentType().equals(MediaType.APPLICATION_XML)) {
         return HttpRequestComparisonType.XML;
      } else {
         return super.compareHttpRequestAsType(input);
      }
   }
}
