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

import java.util.Arrays;
import java.util.List;

import org.restexpress.Exceptions;
import org.restexpress.Request;
import org.restexpress.http.BadRequestException;

/**
 * A factory for RestExpress-Common QueryOrder instances, parsing them from a
 * Request.
 * 
 * @author toddf
 * @since Apr 12, 2011
 * @see org.restexpress.query.QueryOrder
 */
public abstract class QueryOrders {
	private static final String SORT_HEADER_NAME = "sort";
	private static final String SORT_SEPARATOR = "\\|";

	/**
	 * Create a QueryOrder instance from the RestExpress request. Assumes all
	 * resource properties can be sorted-on.
	 * 
	 * @param request
	 *            the current request
	 * @return a QueryOrder instance
	 */
	public static QueryOrder parseFrom(final Request request) {
		return parseFrom(request, (List<String>) null);
	}

	/**
	 * Create a QueryOrder instance from the RestExpress request, setting the
	 * properties on which the resource can be sorted.
	 * 
	 * @param request
	 *            the current request
	 * @param allowedProperties
	 *            an array of property names on which the resource can be
	 *            sorted.
	 * @return a QueryOrder instance
	 */
	public static QueryOrder parseFrom(final Request request, final String... allowedProperties) {
		return parseFrom(request, Arrays.asList(allowedProperties));
	}

	/**
	 * Create a QueryOrder instance from the RestExpress request, setting the
	 * properties on which the resource can be sorted.
	 * 
	 * @param request
	 *            the current request
	 * @param allowedProperties
	 *            a list of property names on which the resource can be sorted.
	 * @return a QueryOrder instance
	 */
	public static QueryOrder parseFrom(final Request request, final List<String> allowedProperties) {
		final String sortString = request.getHeader(SORT_HEADER_NAME);

		if ((sortString == null) || sortString.trim().isEmpty()) {
			return new QueryOrder();
		}

		final String[] strings = sortString.split(SORT_SEPARATOR);
		enforceAllowedProperties(allowedProperties, strings);

		return new QueryOrder(strings);
	}

	private static void enforceAllowedProperties(final List<String> allowedProperties, final String[] requestedProperties) {
		if (requestedProperties == null) {
			return;
		}

		if (allowedProperties != null) {
			int i = 0;

			while (i < requestedProperties.length) {
				final String requested = requestedProperties[i++];

				for (final String allowed : allowedProperties) {
					if (requested.endsWith(allowed)) {
						return;
					}
				}

				throw new BadRequestException(requested + " is not a supported sort field. Supported sort fields are: " + Exceptions.join(", ", allowedProperties));
			}
		}
	}
}
