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
/*
 Copyright 2013, Strategic Gains, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.restexpress.preprocessor;

import javax.xml.bind.DatatypeConverter;

import org.intelligentsia.commons.http.RequestHeader;
import org.intelligentsia.commons.http.ResponseHeader;
import org.intelligentsia.commons.http.exception.UnauthorizedException;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.restexpress.Flags;
import org.restexpress.Request;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.route.Route;

/**
 * This preprocessor implements HTTP Basic authentication. It simply parses the
 * Authorization header, putting the username and password in request headers.
 * To use it, simply add it to your RestExpress server as follows:
 * 
 * <code>
 * server.addPreprocessor(new HttpBasicAuthenticationPreprocessor("my realm"));
 * </code>
 * <p/>
 * Once this preprocessor completes successfully, it places the username and
 * password in the request as headers, X-AuthenticatedUser and
 * X-AuthenticatedPassword, respectively.
 * <p/>
 * If the preprocessor fails, it throws an UnauthorizedException, which results
 * in an HTTP status of 401 to the caller. It also sets the WWW-Authenticate
 * header to 'Basic realm=<provided realm>' where <provided realm> is the
 * arbitrary realm name passed in on instantiation.
 * <p/>
 * Use of this preprocessor assumes you'll implement an authorization
 * preprocessor that validates the username and password.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since Feb 28, 2013
 */
public class HttpBasicAuthenticationPreprocessor implements Preprocessor {

	private final String realm;

	/**
	 * Utilize HTTP Basic Authentication with the given realm returned on an
	 * unauthenticated request.
	 * 
	 * @param realm
	 *            any value to identify the secure area and may used by HTTP
	 *            clients to manage passwords.
	 */
	public HttpBasicAuthenticationPreprocessor(final String realm) {
		super();
		this.realm = realm;
	}

	@Override
	public void process(final MessageContext context) {
		final Request request = context.getRequest();
		final Route route = request.getResolvedRoute();

		if ((route != null) && (route.isFlagged(Flags.PUBLIC_ROUTE) || route.isFlagged(Flags.NO_AUTHENTICATION))) {
			return;
		}

		final String authorization = request.getHeader(HttpHeaders.Names.AUTHORIZATION);

		if ((authorization == null) || !authorization.startsWith("Basic ")) {
			throw newUnauthorizedException(context);
		}

		final String[] pieces = authorization.split(" ");
		final byte[] bytes = DatatypeConverter.parseBase64Binary(pieces[1]);
		final String credentials = new String(bytes);
		final String[] parts = credentials.split(":");

		if (parts.length < 2) {
			throw newUnauthorizedException(context);
		}

		request.addHeader(RequestHeader.X_AUTHENTICATED_USER.getHeader(), parts[0]);
		request.addHeader(RequestHeader.X_AUTHENTICATED_PASSWORD.getHeader(), parts[1]);
	}

	/**
	 * Update Response Header.
	 * 
	 * @param context
	 * @return {@link UnauthorizedException} instance.
	 */
	private UnauthorizedException newUnauthorizedException(final MessageContext context) {
		context.getResponse().addHeader(ResponseHeader.WWW_AUTHENTICATE.getHeader(), "Basic realm=\"" + realm + "\"");
		return new UnauthorizedException("Authentication required");
	}
}
