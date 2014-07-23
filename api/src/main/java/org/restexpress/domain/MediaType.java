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
package org.restexpress.domain;

/**
 * {@link MediaType} define few MIME type.
 * 
 * @see http://en.wikipedia.org/wiki/MIME
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum MediaType {

	ALL("*/*"), //
	APPLICATION_ALL("application/*"), //
	APPLICATION_ALL_JSON("application/*+json"), //
	APPLICATION_ALL_XML("application/*+xml"), //
	APPLICATION_JSON("application/json"), //
	APPLICATION_XML("application/xml"), //
	APPLICATION_HAL_JSON("application/hal+json"), //
	APPLICATION_HAL_XML("application/hal+xml"), //
	TEXT_ALL("text/*"), //
	TEXT_XML("text/xml"), //
	TEXT_JAVASCRIPT("text/javascript"), //
	TEXT_PLAIN("text/plain"), //
	TEXT_HTML("text/html"), //
	TEXT_CSS("text/css")//
	;
	private final String mime;

	private MediaType(String mime) {
		this.mime = mime;
	}

	public String getMime() {
		return mime;
	}

}
