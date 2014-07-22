package org.restexpress.plugin.route;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.intelligentsia.commons.http.exception.NotFoundException;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;
import org.restexpress.ContentType;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;

/**
 * RouteMetadataController.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RouteMetadataController {
	/**
	 * {@link ServerMetadata} instance.
	 */
	private ServerMetadata serverMetadata;
	/**
	 * {@link Map} of route name and {@link RouteMetadata}.
	 */
	private Map<String, RouteMetadata> routeMetadata;
	/**
	 * {@link CompiledTemplate} for console page.
	 */
	private CompiledTemplate console;

	/**
	 * Build a new instance of {@link RouteMetadataController}.
	 */
	public RouteMetadataController() {
		super();
	}

	/**
	 * Initialize controller and build cache of Route.
	 * 
	 * @param serverMetadata
	 *            {@link ServerMetadata} to use.
	 */
	public void initialize(ServerMetadata serverMetadata) {
		this.serverMetadata = serverMetadata;
		routeMetadata = new HashMap<String, RouteMetadata>();
		for (RouteMetadata routeInfo : serverMetadata.getRoutes()) {
			// cache the named routes.
			if (routeInfo.getName() != null && !routeInfo.getName().trim().isEmpty()) {
				routeMetadata.put(routeInfo.getName().toLowerCase(Locale.US), routeInfo);
			}
		}

		// load template
		console = TemplateCompiler.compileTemplate(getClass().getClassLoader().getResourceAsStream("org.restexpress.plugin.route.console.html"));
		if (console == null)
			throw new IllegalStateException("no console template");
	}

	/**
	 * Destroy all Resources
	 */
	public void destroy() {
		serverMetadata = null;
		if (routeMetadata != null)
			routeMetadata.clear();
		routeMetadata = null;
		console = null;
	}

	/**
	 * Returns information on all routes. <code>/routes/metadata</code>
	 * 
	 * @param request
	 * @param response
	 * @return {@link ServerMetadata} instance.
	 */
	public ServerMetadata getAllRoutes(Request request, Response response) {
		return serverMetadata;
	}

	/**
	 * Returns information on a single route.
	 * <code>/routes/metadata/{routeName}</code>
	 * 
	 * @param request
	 * @param response
	 * @return {@link ServerMetadata}
	 * @throws NotFoundException
	 *             if route can not be found
	 */
	public ServerMetadata getSingleRoute(Request request, Response response) throws NotFoundException {
		String routeName = request.getHeader("routeName", "Route name must be provided");
		RouteMetadata routeInfo = routeMetadata.get(routeName.toLowerCase(Locale.US));
		if (routeInfo == null) {
			throw new NotFoundException("Route name not found: " + routeName);
		}
		return new ServerMetadata(serverMetadata, routeInfo);
	}

	/**
	 * Returns a console page. <code>/routes</code>
	 * 
	 * @param request
	 * @param response
	 * @return HTML content of console page.
	 */
	public String getConsole(Request request, Response response) {
		response.setContentType(ContentType.HTML);
		Map<String, Object> vars = new HashMap<>();
		vars.put("server", serverMetadata);
		return TemplateRuntime.execute(console, vars).toString();
	}

}
