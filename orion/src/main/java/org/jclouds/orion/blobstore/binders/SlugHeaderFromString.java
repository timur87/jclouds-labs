
package org.jclouds.orion.blobstore.binders;
import org.jclouds.http.HttpRequest;
import org.jclouds.orion.OrionUtils;
import org.jclouds.orion.config.constans.OrionHttpFields;
import org.jclouds.rest.Binder;

public class SlugHeaderFromString implements Binder {

	@Override
	public <R extends HttpRequest> R bindToRequest(R request, Object name) {
		HttpRequest req = request.toBuilder().replaceHeader(OrionHttpFields.HEADER_SLUG, OrionUtils.convertNameToSlug((String) name))
		      .build();
		return (R) req;
	}

}
