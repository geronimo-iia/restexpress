/**
 *        Licensed to the Apache Software Foundation (ASF) under one
 *        or more contributor license agreements.  See the NOTICE file
 *        distributed with this work for additional information
 *        regarding copyright ownership.  The ASF licenses this file
 *        to you under the Apache License, Version 2.0 (the
 *        "License"); you may not use this file except in compliance
 *        with the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *        Unless required by applicable law or agreed to in writing,
 *        software distributed under the License is distributed on an
 *        "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *        KIND, either express or implied.  See the License for the
 *        specific language governing permissions and limitations
 *        under the License.
 *
 */
package org.restexpress.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.restexpress.Exceptions;
import org.restexpress.Request;
import org.restexpress.http.BadRequestException;

/**
 * A factory for RestExpress-Common QueryFilter instance, parsing from a
 * Request.
 * 
 * @author toddf
 * @since Apr 12, 2011
 * @see org.restexpress.query.QueryFilter
 */
public abstract class QueryFilters {
	private static final String FILTER_HEADER_NAME = "filter";
	private static final String FILTER_SEPARATOR = "\\|";
	private static final String NAME_VALUE_SEPARATOR = "::";

	// SECTION: FACTORY

	/**
	 * Create an instance of QueryFilter from the RestExpress request. Assumes
	 * all resource properties are filterable.
	 * 
	 * @param request
	 *            the current request
	 */
	public static QueryFilter parseFrom(final Request request) {
		return parseFrom(request, (List<String>) null);
	}

	/**
	 * Create an instance of QueryFilter from the RestExpress request, setting
	 * the appropriate properties that can be filtered.
	 * 
	 * @param request
	 *            the current request
	 * @param allowedProperties
	 *            an array of property names on which the resource can be
	 *            filtered.
	 */
	public static QueryFilter parseFrom(final Request request, final String... allowedProperties) {
		return parseFrom(request, Arrays.asList(allowedProperties));
	}

	/**
	 * Create an instance of QueryFilter from the RestExpress request, setting
	 * the appropriate properties that can be filtered.
	 * 
	 * @param request
	 *            the current request
	 * @param allowedProperties
	 *            a list of property names on which the resource can be
	 *            filtered.
	 */
	public static QueryFilter parseFrom(final Request request, final List<String> allowedProperties) {
		final String filterString = request.getHeader(FILTER_HEADER_NAME);

		if ((filterString == null) || filterString.trim().isEmpty()) {
			return new QueryFilter();
		}

		final String[] nameValues = filterString.split(FILTER_SEPARATOR);

		if ((nameValues == null) || (nameValues.length == 0)) {
			return new QueryFilter();
		}

		String[] nameValuePair;
		final List<FilterComponent> filters = new ArrayList<FilterComponent>();

		for (final String nameValue : nameValues) {
			nameValuePair = nameValue.split(NAME_VALUE_SEPARATOR);
			enforceSupportedProperties(allowedProperties, nameValuePair[0]);

			if (nameValuePair.length == 1) {
				filters.add(new FilterComponent(nameValuePair[0], FilterOperator.CONTAINS, ""));
			} else {
				filters.add(new FilterComponent(nameValuePair[0], FilterOperator.CONTAINS, nameValuePair[1]));
			}
		}

		return new QueryFilter(filters);
	}

	private static void enforceSupportedProperties(final List<String> allowedProperties, final String requested) {
		if ((allowedProperties != null) && !allowedProperties.contains(requested)) {
			throw new BadRequestException(requested + " is not a supported filter. Supported filter names are: " + Exceptions.join(", ", allowedProperties));
		}
	}
}
