package org.jclouds.orion.blobstore.binders;

import org.jclouds.http.HttpRequest;
import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.config.constans.OrionHttpFields;
import org.jclouds.orion.domain.OrionBlob;
import org.jclouds.rest.Binder;

/**
 * 
 * @author timur
 * 
 */
public class SlugHeaderFromBlobBinder implements Binder {

	@Override
	public <R extends HttpRequest> R bindToRequest(R request, Object input) {
		OrionBlob blob = OrionBlob.class.cast(input);
		HttpRequest req = request.toBuilder().replaceHeader(OrionHttpFields.HEADER_SLUG, OrionUtils.convertNameToSlug(blob.getProperties().getName()))
		      .build();

		return (R) req;
	}
}
