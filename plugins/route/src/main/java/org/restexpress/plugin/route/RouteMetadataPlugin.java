package org.restexpress.plugin.route;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Format;
import org.restexpress.RestExpress;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.plugin.RoutePlugin;
import org.restexpress.route.RouteBuilder;

/**
 * {@link RouteMetadataPlugin}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RouteMetadataPlugin extends RoutePlugin {

	/**
	 * {@link RouteMetadataController} instance.
	 */
	private final RouteMetadataController routeMetadataController;

	/**
	 * {@link List} of {@link RouteBuilder}.
	 */
	private final List<RouteBuilder> routeBuilders;

	/**
	 * Build a new instance of {@link RouteMetadataPlugin}.
	 */
	public RouteMetadataPlugin() {
		super();
		routeMetadataController = new RouteMetadataController();
		routeBuilders = new ArrayList<RouteBuilder>();
	}

	@Override
	public RouteMetadataPlugin register(RestExpress server) {
		if (isRegistered())
			return this;
		// register
		super.register(server);
		// declare new route
		routeBuilders.add(server.uri("/routes/metadata.{format}", routeMetadataController) //
				.action("getAllRoutes", HttpMethod.GET) //
				.name("all.routes.metadata"));

		routeBuilders.add(server.uri("/routes/{routeName}/metadata.{format}", routeMetadataController) //
				.action("getSingleRoute", HttpMethod.GET) //
				.name("single.route.metadata"));

		routeBuilders.add(server.uri("/routes", routeMetadataController) //
				.action("getConsole", HttpMethod.GET) //
				.noSerialization() //
				.name("routes.metadata.console") //
				.format(Format.HTML));
		// apply flag and parameter
		for (RouteBuilder routeBuilder : routeBuilders) {
			applyFlags(routeBuilder);
			applyParameters(routeBuilder);
		}
		// declare new alias
		server.serializationProvider().alias("service", ServerMetadata.class);
		server.serializationProvider().alias("route", RouteMetadata.class);
		return this;
	}

	@Override
	public void bind(RestExpress server) {
		routeMetadataController.initialize(server.getRouteMetadata());
	}

	@Override
	public void shutdown(RestExpress server) {
		super.shutdown(server);
		routeBuilders.clear();
		routeMetadataController.destroy();
	}

}