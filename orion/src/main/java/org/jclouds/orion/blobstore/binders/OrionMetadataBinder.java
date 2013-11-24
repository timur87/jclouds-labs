package org.jclouds.orion.blobstore.binders;

import java.util.Calendar;
import java.util.Date;

import org.jclouds.http.HttpRequest;
import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.config.constans.OrionHttpFields;
import org.jclouds.orion.domain.JSONUtils;
import org.jclouds.orion.domain.OrionBlob;
import org.jclouds.rest.Binder;

import com.google.inject.Inject;

public class OrionMetadataBinder implements Binder {

   private final JSONUtils jsonConverter;

   @Inject
   public OrionMetadataBinder(JSONUtils jsonConverter) {
      this.jsonConverter = jsonConverter;
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      OrionBlob blob = OrionBlob.class.cast(input);
      Date date = Calendar.getInstance().getTime();
      blob.getProperties().setLastModified(date);

      request = (R) request.toBuilder()
            .replaceHeader(OrionHttpFields.HEADER_SLUG, OrionUtils.getMetadataName(OrionUtils.convertNameToSlug(blob.getProperties().getName())))
            .build();

      request.setPayload(jsonConverter.getObjectAsString(blob.getProperties()));

      return request;
   }
}
