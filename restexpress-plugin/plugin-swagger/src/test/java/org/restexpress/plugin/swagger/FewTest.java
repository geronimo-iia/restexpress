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

}
