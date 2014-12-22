package org.restexpress.plugin.swagger.core;

import java.util.Set;

import org.reflections.Reflections;

import com.wordnik.swagger.annotations.Api;

/**
 * 
 * {@link RestExpressScanner} scan all classes for specified resource package
 * which are annotated with {@link Api}..
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public class RestExpressScanner {

	String resourcePackage;

	public Set<Class<?>> classes() {
		return new Reflections(resourcePackage).getTypesAnnotatedWith(Api.class);
	}
}
