package org.jclouds.orion.blobstore.functions.parsers.param;

import org.jclouds.orion.OrionUtils;

import com.google.common.base.Function;

/**
 * Encodes blob name, used in case if the name will be used in a path
 * 
 * @author timur
 * 
 */
public class MetadataNamePathParamParser implements Function<Object, String> {

   @Override
   public String apply(Object blobName) {

      return OrionUtils.getMetadataName(OrionUtils.getNamePath(((String) blobName)));
   }
}
