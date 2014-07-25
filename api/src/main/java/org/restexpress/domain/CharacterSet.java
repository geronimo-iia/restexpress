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

import java.nio.charset.Charset;

/**
 * {@link CharacterSet} enumerate a small subset of {@link Charset} used in
 * RestExpress.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public enum CharacterSet {
	/**
	 * @see http://en.wikipedia.org/wiki/UTF-8
	 */
	UTF_8("UTF-8"),
	/**
	 * @see http://en.wikipedia.org/wiki/UTF-16
	 */
	UTF_16("UTF-16"),
	/**
	 * @see http://en.wikipedia.org/wiki/ISO_8859-1
	 */
	ISO_8859_1("ISO-8859-1");

	private final Charset charset;
	private final String charsetName;

	private CharacterSet(String name) {
		this.charsetName = name;
		this.charset = Charset.forName(name);
	}

	public Charset getCharset() {
		return charset;
	}

	public String getCharsetName() {
		return charsetName;
	}
}
