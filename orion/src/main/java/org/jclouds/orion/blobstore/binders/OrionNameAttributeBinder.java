package org.jclouds.orion.blobstore.binders;

import org.jclouds.http.HttpRequest;
import org.jclouds.orion.blobstore.functions.converters.JSONToOrionSpecificObject;
import org.jclouds.orion.blobstore.functions.converters.OrionSpecificObjectToJSON;
import org.jclouds.orion.domain.OrionSpecificFileMetadata;
import org.jclouds.rest.Binder;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

public class OrionNameAttributeBinder implements Binder{
   private final JSONToOrionSpecificObject json2OrionSpecificObj;
   private final OrionSpecificFileMetadata metadata;
   private final OrionSpecificObjectToJSON orionSpecificObject2JSON;

   @Inject
   public OrionNameAttributeBinder(OrionSpecificFileMetadata metadata, JSONToOrionSpecificObject json2OrionSpecificObj,
         OrionSpecificObjectToJSON orionSpecificObject2JSON) {
      this.metadata = Preconditions.checkNotNull(metadata, "metadata is null");
      this.json2OrionSpecificObj = Preconditions.checkNotNull(json2OrionSpecificObj, "json2OrionSpecificObjis null");
      this.orionSpecificObject2JSON = Preconditions.checkNotNull(orionSpecificObject2JSON,
            "orionSpecificObject2JSON is null");
   }
   @Override
   public <R extends HttpRequest> R bindToRequest(R req, Object name) {
      HttpRequest request = req;
      OrionSpecificFileMetadata metadata;

      metadata = json2OrionSpecificObj.apply((String) request.getPayload().getRawContent());
      if(metadata == null){
         metadata = this.metadata;
      }

      if(name instanceof String){
         metadata.setName((String)name);
      }else{
         System.err.println("Passed variable should be string");
      }

      return (R) request.toBuilder().payload(orionSpecificObject2JSON.apply(metadata)).build();
   }

}
