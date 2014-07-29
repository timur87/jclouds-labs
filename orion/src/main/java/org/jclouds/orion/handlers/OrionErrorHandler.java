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
package org.jclouds.orion.handlers;

import java.io.IOException;

import javax.inject.Singleton;

import org.jclouds.domain.Credentials;
import org.jclouds.http.HttpCommand;
import org.jclouds.http.HttpCommandExecutorService;
import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.HttpResponse;
import org.jclouds.http.HttpResponseException;
import org.jclouds.http.HttpUtils;
import org.jclouds.location.Provider;
import org.jclouds.orion.OrionResponseException;
import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.OrionError;
import org.jclouds.orion.http.filters.FormAuthentication;
import org.jclouds.rest.AuthorizationException;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;

/**
 * This will parse and set an appropriate exception on the command object.
 * 
 *
 * 
 */
@Singleton
public class OrionErrorHandler implements HttpErrorHandler {

   private final String userWorkspace;
   private final HttpCommandExecutorService commandExecutor;
   private final JSONUtils jsonConverter;

   @Inject
   public OrionErrorHandler(@Provider Supplier<Credentials> creds, HttpCommandExecutorService commandExecutor,
         JSONUtils mapper) {
      this.commandExecutor = Preconditions.checkNotNull(commandExecutor, "HttpCommandExecutorService");
      this.jsonConverter = Preconditions.checkNotNull(mapper, "mapper is null");
      this.userWorkspace = Preconditions.checkNotNull(creds, "creds  is null").get().identity;
   }

   @Override
   public void handleError(HttpCommand command, HttpResponse response) {
      // it is important to always read fully and close streams
      byte[] data = HttpUtils.closeClientButKeepContentStream(response);
      String message = data != null ? new String(data) : null;

      Exception exception = message != null ? new HttpResponseException(command, response, message)
      : new HttpResponseException(command, response);
      message = message != null ? message : String.format("%s -> %s", command.getCurrentRequest().getRequestLine(),
            response.getStatusLine());

      try {
         String theString = CharStreams.toString(CharStreams.newReaderSupplier(ByteStreams.newInputStreamSupplier(ByteStreams.toByteArray(response.getPayload().openStream())), Charsets.UTF_8));
         OrionError error = this.jsonConverter.getStringAsObject(theString, OrionError.class);
         OrionResponseException orionException = new OrionResponseException(command, response, error);
         command.setException(orionException);

         switch (response.getStatusCode()) {

            case 401:
               if ((command.getFailureCount() < 3) && FormAuthentication.hasKey(this.userWorkspace)) {
                  // Remove the outdated key and replay the request
                  FormAuthentication.removeKey(this.userWorkspace);
                  this.commandExecutor.invoke(command);
               } else {
                  exception = new AuthorizationException(message, exception);
                  command.setException(exception);
                  exception.printStackTrace();
               }
            case 403:
               exception = new AuthorizationException(message, exception);
               command.setException(exception);
               exception.printStackTrace();
               break;

            case 409:
               exception = new IllegalStateException(message, exception);
               command.setException(exception);
               exception.printStackTrace();
               break;
         }
         return;
      }catch (IOException e) {
         e.printStackTrace();
         this.doStandardHandling(command, response, exception);

      }

   }

   /**
    * @param command
    * @param response
    * @param exception
    */
   private void doStandardHandling(HttpCommand command, HttpResponse response, Exception exception) {
      switch (response.getStatusCode()) {
         case 400:
            // command.setException(exception);
            exception.printStackTrace();
            break;
         case 401:
         case 403:
            if ((command.getFailureCount() < 3) && FormAuthentication.hasKey(this.userWorkspace)) {
               // Remove the outdated key and replay the request
               FormAuthentication.removeKey(this.userWorkspace);
               this.commandExecutor.invoke(command);
            } else {
               exception = new AuthorizationException(response.getMessage(), exception);
               command.setException(exception);
               exception.printStackTrace();
            }
            break;
         case 404:
            if (!command.getCurrentRequest().getMethod().equals("DELETE")) {
               // exception = new ResourceNotFoundException(message,
               // exception);
            }
            break;
         case 409:
            exception = new IllegalStateException(response.getMessage(), exception);
            command.setException(exception);
            exception.printStackTrace();
            break;
      }

   }
}
