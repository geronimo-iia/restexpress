package org.restexpress.plugin.swagger.core;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * {@link SwaggerSpec} specifies base type, container type and version.
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public enum SwaggerSpec {
	;
	private static Set<String> baseTypes = Sets.newHashSet("byte", "boolean", "int", "long", "float", "double", "string", "date", "void", "Date", "BigDecimal", "UUID");
	private static Set<String> containerTypes = Sets.newHashSet("Array", "List", "Set");
	private static String version = "1.2";

	public static Set<String> baseTypes() {
		return baseTypes;
	}

	public static Set<String> containerTypes() {
		return containerTypes;
	}

	public static String version() {
		return version;
	}
}
