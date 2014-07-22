/**
 * 
 */
package org.restexpress.server;

import org.restexpress.RestExpress;
import org.restexpress.RestExpressEntryPoint;
import org.restexpress.plugin.common.cache.CacheControlPlugin;
import org.restexpress.plugin.route.RouteMetadataPlugin;

/**
 * {@link ServerRestExpressEntryPoint} add several plugin.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ServerRestExpressEntryPoint implements RestExpressEntryPoint {

	/**
	 * Build a new instance.
	 */
	public ServerRestExpressEntryPoint() {
	}

	/**
	 * @see org.restexpress.RestExpressEntryPoint#onLoad(org.restexpress.RestExpress)
	 */
	@Override
	public void onLoad(RestExpress restExpress) throws RuntimeException {
		// add Route Metadata plugin
		restExpress.registerPlugin(new RouteMetadataPlugin());
		// add cache plugin
		restExpress.registerPlugin(new CacheControlPlugin());
	}

}
