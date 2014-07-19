package org.restexpress.plugin.route;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.restexpress.Flags;
import org.restexpress.Format;
import org.restexpress.RestExpress;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.plugin.AbstractPlugin;
import org.restexpress.route.RouteBuilder;

/**
 * {@link RoutePlugin}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RoutePlugin extends AbstractPlugin {

	/**
	 * {@link RouteController} instance.
	 */
	private final RouteController routeController;

	/**
	 * {@link List} of {@link RouteBuilder}.
	 */
	private final List<RouteBuilder> routeBuilders;

	/**
	 * Build a new instance of {@link RoutePlugin}.
	 */
	public RoutePlugin() {
		super();
		routeController = new RouteController();
		routeBuilders = new ArrayList<RouteBuilder>();
	}

	@Override
	public RoutePlugin register(RestExpress server) {
		if (isRegistered())
			return this;
		// register
		super.register(server);
		// declare new route
		routeBuilders.add(server.uri("/routes/metadata.{format}", routeController) //
				.action("getAllRoutes", HttpMethod.GET) //
				.name("all.routes.metadata"));

		routeBuilders.add(server.uri("/routes/{routeName}/metadata.{format}", routeController) //
				.action("getSingleRoute", HttpMethod.GET) //
				.name("single.route.metadata"));

		routeBuilders.add(server.uri("/routes", routeController) //
				.action("getConsole", HttpMethod.GET) //
				.noSerialization() //
				.name("routes.metadata.console") //
				.format(Format.HTML));
		// declare new alias
		server.getSerializationProvider().alias("service", ServerMetadata.class);
		server.getSerializationProvider().alias("route", RouteMetadata.class);
		return this;
	}

	@Override
	public void bind(RestExpress server) {
		routeController.initialize(server.getRouteMetadata());
	}

	
	@Override
	public void shutdown(RestExpress server) {
		super.shutdown(server);
		routeBuilders.clear();	
		routeController.destroy();
	}
	
	/**
	 * Add flag to all route.
	 * 
	 * @param flagValue
	 * @return {@link RoutePlugin} instance.
	 */
	public RoutePlugin flag(Flags flagValue) {
		for (RouteBuilder routeBuilder : routeBuilders) {
			routeBuilder.flag(flagValue);
		}
		return this;
	}

	/**
	 * Add parameter to all route
	 * 
	 * @param name
	 * @param value
	 * @return {@link RoutePlugin} instance.
	 */
	public RoutePlugin parameter(String name, Object value) {
		for (RouteBuilder routeBuilder : routeBuilders) {
			routeBuilder.parameter(name, value);
		}
		return this;
	}
}
