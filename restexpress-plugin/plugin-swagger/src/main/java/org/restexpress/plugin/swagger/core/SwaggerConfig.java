package org.restexpress.plugin.swagger.core;

import java.util.List;

import org.restexpress.plugin.swagger.model.ApiInfo;
import org.restexpress.plugin.swagger.model.AuthorizationType;

import com.google.common.collect.Lists;

/**
 * SwaggerConfig.
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public class SwaggerConfig {
	public static final String DEFAULT_URL_PATH = "/api-docs";

	private String apiVersion;
	private String swaggerVersion = SwaggerSpec.version();
	private String basePath = "http://localhost:8080";
	private String apiPath = "";
	private List<AuthorizationType> authorizations = Lists.newArrayList();
	private ApiInfo info;

	// def addAuthorization(auth: AuthorizationType) = {
	// authorizations = authorizations ++ List(auth)
	// }
}
