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
package org.jclouds.orion.blobstore.config;

import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.attr.ConsistencyModel;
import org.jclouds.blobstore.strategy.ClearContainerStrategy;
import org.jclouds.blobstore.strategy.ClearListStrategy;
import org.jclouds.blobstore.strategy.DeleteDirectoryStrategy;
import org.jclouds.blobstore.strategy.GetBlobsInListStrategy;
import org.jclouds.blobstore.strategy.PutBlobsStrategy;
import org.jclouds.orion.blobstore.OrionBlobStore;
import org.jclouds.orion.blobstore.OrionBlobStoreContext;
import org.jclouds.orion.blobstore.strategy.internal.ClearFilesInContainer;
import org.jclouds.orion.blobstore.strategy.internal.DeleteAllKeysInList;
import org.jclouds.orion.blobstore.strategy.internal.GetAllBlobsInListAndRetryOnFailure;
import org.jclouds.orion.blobstore.strategy.internal.PutBlobsStrategyImpl;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Initialize OrionBlobStore modules
 * 
 * @author Timur
 * 
 */
public class OrionBlobStoreModule extends AbstractModule {

   @Override
   protected void configure() {

      this.bind(ConsistencyModel.class).toInstance(ConsistencyModel.STRICT);
      this.bind(BlobStoreContext.class).to(OrionBlobStoreContext.class).in(Scopes.SINGLETON);
      this.bind(GetBlobsInListStrategy.class).to(GetAllBlobsInListAndRetryOnFailure.class).in(Scopes.SINGLETON);
      this.bind(DeleteDirectoryStrategy.class)
      .to(org.jclouds.orion.blobstore.strategy.internal.MarkersDeleteDirectoryStrategy.class)
      .in(Scopes.SINGLETON);
      this.bind(PutBlobsStrategy.class).to(PutBlobsStrategyImpl.class).in(Scopes.SINGLETON);
      this.bind(ClearContainerStrategy.class).to(ClearFilesInContainer.class).in(Scopes.SINGLETON);
      this.bind(ClearListStrategy.class).to(DeleteAllKeysInList.class).in(Scopes.SINGLETON);
      this.bind(BlobStore.class).to(OrionBlobStore.class).in(Scopes.SINGLETON);

   }
}
