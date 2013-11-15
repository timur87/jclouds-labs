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
package org.jclouds.orion.blobstore.functions.parsers.response;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.http.HttpResponse;
import org.jclouds.orion.blobstore.functions.converters.ChildMetadataToStorageMetadata;
import org.jclouds.orion.blobstore.functions.converters.ConvertToFlatListofContainers;
import org.jclouds.orion.domain.OrionStorageMetadata;
import org.jclouds.orion.domain.internal.OrionPageSet;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

/**
 * @author timur
 * 
 */
public class ListContainersResponseParser implements Function<HttpResponse, PageSet<? extends StorageMetadata>> {

	private final ConvertToFlatListofContainers folderListParser;
	private final ChildMetadataToStorageMetadata childMetadataToStorageMetadata;
	private final ObjectMapper mapper;

	@Inject
	public ListContainersResponseParser(ConvertToFlatListofContainers folderListParser,
	      ChildMetadataToStorageMetadata childMetadataToStorageMetadata, ObjectMapper mapper) {
		this.folderListParser = Preconditions.checkNotNull(folderListParser, "folderListParser is null");
		this.childMetadataToStorageMetadata = Preconditions.checkNotNull(childMetadataToStorageMetadata,
		      "childMetadataToStorageMetadata is null");
		this.mapper = mapper;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.common.base.Function#apply(java.lang.Object)
	 */
	@Override
	public PageSet<? extends StorageMetadata> apply(HttpResponse res) {
		JsonNode jsonNode;
		try {
			jsonNode = this.mapper.readTree(res.getPayload().getInput());
			List<OrionStorageMetadata> storageDataList = Lists.newArrayList(Lists.transform(
			      this.folderListParser.apply(jsonNode), this.childMetadataToStorageMetadata));
			return new OrionPageSet(storageDataList);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
