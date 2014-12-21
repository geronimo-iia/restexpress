package org.restexpress.plugin.swagger.model;

public class Parameter {
	String name;
	String description = null;
	String defaultValue = null;
	Boolean required;
	Boolean allowMultiple;
	String dataType;
	AllowableValues allowableValues = new AnyAllowableValues();
	String paramType;
	String paramAccess;

}
