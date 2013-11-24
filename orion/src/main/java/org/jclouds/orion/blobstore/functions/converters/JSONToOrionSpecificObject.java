package org.jclouds.orion.blobstore.functions.converters;

import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

/**
 * A function to de-serialize {@link OrionSpecificFileMetadata} from a JSON
 * String
 * 
 * @author Timur
 * 
 */
public class JSONToOrionSpecificObject implements Function<String, OrionSpecificFileMetadata> {

   private final JSONUtils jsonConverter;

   @Inject
   public JSONToOrionSpecificObject(JSONUtils mapper) {
      jsonConverter = Preconditions.checkNotNull(mapper, "mapper is null");
   }

   @Override
   public OrionSpecificFileMetadata apply(String jsonStr) {
      return jsonConverter.getStringAsObject(jsonStr, OrionSpecificFileMetadata.class);
   }
}
