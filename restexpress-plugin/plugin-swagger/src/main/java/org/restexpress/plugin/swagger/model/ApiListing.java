package org.restexpress.plugin.swagger.model;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class ApiListing {

	String apiVersion;
	String swaggerVersion;
	String basePath;
	String resourcePath;
	List<String> produces = Lists.newArrayList();
	List<String> consumes = Lists.newArrayList();
	List<String> protocols = Lists.newArrayList();
	List<Authorization> authorizations = Lists.newArrayList();
	List<ApiDescription> apis = Lists.newArrayList();
	Map<String, Model> models = null;
	String description = null;
	int position = 0;

}
