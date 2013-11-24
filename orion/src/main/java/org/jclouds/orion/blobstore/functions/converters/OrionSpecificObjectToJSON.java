package org.jclouds.orion.blobstore.functions.converters;

import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * A function to serialize to {@link OrionSpecificFileMetadata} to JSON format
 * 
 * @author Timur
 * 
 */
public class OrionSpecificObjectToJSON implements Function<OrionSpecificFileMetadata, String> {

   private final JSONUtils jsonConverter;

   @Inject
   public OrionSpecificObjectToJSON(JSONUtils mapper) {
      jsonConverter = Preconditions.checkNotNull(mapper, "mapper is null");
   }

   @Override
   public String apply(OrionSpecificFileMetadata jsonObj) {
      return jsonConverter.getObjectAsString(jsonObj);

   }
}
