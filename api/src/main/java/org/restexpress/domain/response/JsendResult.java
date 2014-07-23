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
    Copyright 2011, Strategic Gains, Inc.

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
package org.restexpress.domain.response;

import java.io.Serializable;
import java.util.Locale;

/**
 * Generic JSEND-style wrapper for responses. Differs from the JSEND
 * recommendation as follows:</br>
 * <p/>
 * <ul>
 * <li>Error status illustrates a non-2xx and non-500 response (e.g. validation
 * errors causing a 400, Bad Request).</li>
 * <li>Fail status is essentially a 500 (internal server) error.</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 * @author toddf
 * @since Jan 11, 2011
 */
public class JsendResult implements Serializable {

	private static final long serialVersionUID = -1905504337512287014L;

	public enum State {
		SUCCESS, ERROR, FAIL;

		@Override
		public String toString() {
			return super.toString().toLowerCase(Locale.US);
		};
	}

	private String status;
	private String message;
	private Object data;

	public JsendResult() {
		super();
	}

	public JsendResult(final State status, final String errorMessage, final Object data) {
		super();
		this.status = status.toString();
		this.message = errorMessage;
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}

	public Object getData() {
		return data;
	}

}
