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
	APPLICATION_HAL_JSON("application/hal+json"), //
	APPLICATION_JSON("application/json"), //
	APPLICATION_ALL_XML("application/*+xml"), //
	APPLICATION_XML("application/xml"), //
	APPLICATION_HAL_XML("application/hal+xml"), //
	APPLICATION_JAVASCRIPT("application/javascript"), //
	TEXT_ALL("text/*"), //
	TEXT_XML("text/xml"), //
	TEXT_JAVASCRIPT("text/javascript"), //
	TEXT_PLAIN("text/plain"), //
	TEXT_HTML("text/html"), //
	TEXT_CSS("text/css");

	private final String mime;

	private MediaType(String mime) {
		this.mime = mime;
	}

	/**
	 * @return MIME type.
	 */
	public String getMime() {
		return mime;
	}

	public String withCharset(String charsetName) {
		return mime + "; charset=" + charsetName;
	}

	/**
	 * @param parameter
	 * @param value
	 * @return MIME type added with parameter information information
	 */
	public String getMime(String parameter, String value) {
		return mime + "; " + parameter + "=" + value;
	}
}
