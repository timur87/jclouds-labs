package org.jclouds.orion.blobstore.functions.parsers.param;

import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.domain.OrionBlob;

import com.google.common.base.Function;

/**
 * Fetch the file name from OrionBlob, convert it to metadata and name encode it
 * once.
 * 
 * @author timur
 * 
 */
public class BlobMetadataNameParamParser implements Function<Object, String> {

   @Override
   public String apply(Object blob) {
      return OrionUtils.getNamePath(OrionUtils.getMetadataName(OrionBlob.class.cast(blob).getProperties().getName()));
   }

}
