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

package org.jclouds.orion.blobstore;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import javax.inject.Inject;

import org.jclouds.Context;
import org.jclouds.blobstore.AsyncBlobStore;
import org.jclouds.blobstore.BlobRequestSigner;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.blobstore.attr.ConsistencyModel;
import org.jclouds.internal.BaseView;
import org.jclouds.location.Provider;
import org.jclouds.rest.Utils;
import org.jclouds.rest.annotationparsing.ClosableApiTest;

import com.google.common.io.Closeables;
import com.google.common.reflect.TypeToken;

public class OrionBlobStoreContext extends BaseView implements BlobStoreContext {

   private final BlobStore blobStore;
   private final ConsistencyModel consistencyModel;
   private final Utils utils;
   private final BlobRequestSigner blobRequestSigner;

   @Inject
   public OrionBlobStoreContext(@Provider Context backend, @Provider TypeToken<? extends Context> backendType,
         Utils utils, ConsistencyModel consistencyModel, BlobStore blobStore, BlobRequestSigner blobRequestSigner) {

      super(backend, backendType);
      this.consistencyModel = checkNotNull(consistencyModel, "consistencyModel");
      this.blobStore = checkNotNull(blobStore, "blobStore");
      this.utils = checkNotNull(utils, "utils");
      this.blobRequestSigner = checkNotNull(blobRequestSigner, "blobRequestSigner");
   }

   @Override
   public ConsistencyModel getConsistencyModel() {
      return this.consistencyModel;
   }

   @Override
   public BlobStore getBlobStore() {
      return this.blobStore;
   }

   @Override
   @Deprecated
   public AsyncBlobStore getAsyncBlobStore() {
      return null;
   }

   @Override
   public Utils utils() {
      return this.utils;
   }

   @Override
   public BlobRequestSigner getSigner() {
      return this.blobRequestSigner;
   }

   @Override
   public void close() {
	   try {
		Closeables.close(delegate(), true);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }

   @Override
   public int hashCode() {
      return this.delegate().hashCode();
   }

   @Override
   public String toString() {
      return this.delegate().toString();
   }

   @Override
   public boolean equals(Object obj) {
      return this.delegate().equals(obj);
   }

}
