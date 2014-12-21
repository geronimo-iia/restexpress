package org.restexpress.plugin.swagger.model;

public class ModelProperty {
	String type;
	String qualifiedType;
	int position = 0;
	Boolean required = false;
	String description = null;
	AllowableValues allowableValues = new AnyAllowableValues();
	ModelRef items = null;

}
