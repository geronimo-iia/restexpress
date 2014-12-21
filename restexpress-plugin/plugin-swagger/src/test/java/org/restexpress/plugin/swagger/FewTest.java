package org.restexpress.plugin.swagger;

import java.lang.reflect.Method;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiImplicitParams;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.config.Scanner;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.core.SwaggerSpec;

import static com.wordnik.swagger.config.SwaggerConfig.*;
import static com.wordnik.swagger.core.SwaggerSpec.*;
import  scala.*;
import com.wordnik.swagger.model.AllowableValues;
import com.wordnik.swagger.model.Parameter;
import com.wordnik.swagger.reader.ClassReaderUtils;
import com.wordnik.swagger.reader.ClassReaders;

public class FewTest {

	public void scanAnnotation() {
		ScannerFactory.setScanner(new RestExpressScanner());
	}

	public class Configuration {
		// api.version
		private String apiVersion;
		// swagger.api.basepath
		private String basePath;
		// resource.package
		String resoucePackage;

		// filter: swagger.filter
		// do FilterFactory.filter =
		// SwaggerContext.loadClass(e).newInstance.asInstanceOf[SwaggerSpecFilter]
		// else DefaultSpecFilter

		// SwaggerConfig config = new SwaggerConfig(apiVersion, "1.2", basePath,
		// "");

		public SwaggerConfig config() {
			new SwaggerConfig(apiVersion, SwaggerSpec.version(), basePath, "", List, None.get());
			SwaggerConfig.this;
			return null;
		}

	}

	public class RestExpressReader {

		public void read(String docRoot, Class<?> cls, SwaggerConfig config) {
			Api api = cls.getAnnotation(Api.class);
			if (api != null) {
				String fullPath = api.value().startsWith("/") ? api.value().substring(1) : api.value();
				String resourcePath = null;
				String subpath = null;
				if (fullPath.indexOf("/") > 0) {
					int pos = fullPath.indexOf("/");
					resourcePath = "/" + fullPath.substring(0, pos);
					subpath = fullPath.substring(pos);
				} else {
					resourcePath = "/";
					subpath = fullPath;
				}
				System.err.println(String.format("read routes from classes: %s, %s", resourcePath, subpath));
				for (Method method : cls.getMethods()) {
					// only process mehods with @ApiOperation annotations
					if (method.getAnnotation(ApiOperation.class) != null) {
						List<Parameter> parameters = Lists.newArrayList();
						ApiImplicitParams paramListAnnotation = method.getAnnotation(ApiImplicitParams.class);
						if (paramListAnnotation != null) {
							for (ApiImplicitParam param : paramListAnnotation.value()) {
								ClassReaderUtils utils;
//								AllowableValues allowableValues = param.allowableValues();
//								Parameter parameter = new Parameter(param.name(),//
//										param.value(), //
//										param.defaultValue(),//
//										param.required(),//
//										param.allowMultiple(),//
//										param.dataType(),//
//										allowableValues,//
//										param.dataType(), //
//										param.access());
//								parameters.add(parameter);
							}

						}

					}
				}

			}
		}
	}

	/**
	 * 
	 * RestExpressScanner.
	 *
	 * {@link Api} class marks a class as a Swagger resource. Extends scanner
	 * 
	 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
	 *
	 */
	// public class RestExpressScanner implements Scanner {
	// String resoucePackage = "org.restexpress.plugin.swagger";
	//
	// public List<Class<?>> classes() {
	// ListSet<Class<?>> result = new ListSet<>();
	// return new ListSet(new
	// Reflections(resoucePackage).getTypesAnnotatedWith(Api.class));
	// return null;
	// }
	//
	// }
}
