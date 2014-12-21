package org.restexpress.plugin.swagger.model;

import java.util.List;

import com.google.common.collect.Lists;

public class ApiDescription {
	String path;
	String description;
	List<Operation> operations = Lists.newArrayList();
	Boolean hidden = false;
}
