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
 * Encoding.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum Encoding {
	/** All s acceptable. */
	ALL("*"),

	/** The common Unix file compression. */
	COMPRESS("compress"),

	/** The zlib format defined by RFC 1950 and 1951. */
	DEFLATE("deflate"),

	/** The zlib format defined by RFC 1950 and 1951, without wrapping. */
	DEFLATE_NOWRAP("deflate-no-wrap"),

	/** The FreeMarker . */
	FREEMARKER("freemarker"),

	/** The GNU Zip . */
	GZIP("gzip"),

	/** The default (identity)  */
	IDENTITY("identity"),

	/** The Velocity . */
	VELOCITY("velocity"),

	/** The Info-Zip . */
	ZIP("zip");

	private final String value;

	private Encoding(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
