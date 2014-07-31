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
package org.jclouds.orion.blobstore.fallbacks;

import javax.annotation.Resource;

import org.jclouds.Fallback;
import org.jclouds.logging.Logger;
import org.jclouds.orion.OrionResponseException;
import org.jclouds.orion.config.constans.OrionConstantValues;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Orion does not allow creation of a file and folder with the same names in the
 * same directory.
 * 
 * {@link Fallback} in case of creation of such a situation.
 * 
 * Behavior fail stop (force replace behavior can be found under the given link)
 * 
 * @see <a href="http://google.com">https ://github

 *      .com/timur87/orion-jclouds/blob/master/src/main/java/org/jclouds/orion
 *      /blobstore/fallbacks/SameFileWithDiffTypeFallback.java</a> )
 * 
 *
 */
public class SameFileWithDiffTypeFallback implements Fallback<Boolean> {
   @Resource Logger logger = Logger.CONSOLE;
   /*
    * (non-Javadoc)
    * 
    * @see
    * com.google.common.util.concurrent.FutureFallback#create(java.lang.Throwable
    * )
    */
   @Override
   public ListenableFuture<Boolean> create(Throwable arg0) throws Exception {
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.jclouds.Fallback#createOrPropagate(java.lang.Throwable)
    */
   @Override
   public Boolean createOrPropagate(Throwable t) throws Exception {

      if (t instanceof OrionResponseException) {
         OrionResponseException exception = (OrionResponseException) t;
         if (exception.getError().getHttpCode().equals("500")) {
            this.logger.error(exception.getMessage(),exception);
            this.logger.error(exception.getCommand().getCurrentRequest().getRequestLine());
            if (OrionConstantValues.DEBUG_MODE) {
               this.logger.error(exception.getMessage(),exception);
               this.logger.error(exception.getCommand().getCurrentRequest().getRequestLine());
            }
         }
      }
      throw Throwables.propagate(t);
   }
}
