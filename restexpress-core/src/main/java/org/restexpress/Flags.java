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
package org.restexpress;

/**
 * Constants for built-in RestExpress flag values on routes.
 * 
 * Use these flags on routes where the security (e.g. preprocessors) is altered.
 * For instance, when all routes are behind authentication and authorization,
 * some routes may be made public (such as OAuth2 authentication routes).
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Jan 19, 2011
 */
public enum Flags {
	/**
	 * NO_CACHE flag: prevent caching header declaration
	 */
	NO_CACHE("no.caching"), //
	/**
	 * PUBLIC_ROUTE flag: declare a public route (not secured)
	 */
	PUBLIC_ROUTE("not.secured"), //
	NO_AUTHENTICATION("no.authentication"), //
	NO_AUTHORIZATION("no.authorization") //
	;

	private String value;

	/**
	 * Build a new instance of {@link Flags}.
	 * @param value
	 */
	private Flags(final String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

}
