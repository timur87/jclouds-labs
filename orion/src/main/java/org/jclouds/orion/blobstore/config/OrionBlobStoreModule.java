/*******************************************************************************
 * Copyright (c) 2013 University of Stuttgart.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and the Apache License 2.0 which both accompany this distribution,
 * and are available at http://www.eclipse.org/legal/epl-v10.html
 * and http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *    Timur Sungur - initial API and implementation
 *******************************************************************************/

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
