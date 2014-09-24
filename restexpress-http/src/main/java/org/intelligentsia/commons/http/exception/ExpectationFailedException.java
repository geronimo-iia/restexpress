/**
 * ====
 *            Licensed to the Apache Software Foundation (ASF) under one
 *            or more contributor license agreements.  See the NOTICE file
 *            distributed with this work for additional information
 *            regarding copyright ownership.  The ASF licenses this file
 *            to you under the Apache License, Version 2.0 (the
 *            "License"); you may not use this file except in compliance
 *            with the License.  You may obtain a copy of the License at
 *
 *              http://www.apache.org/licenses/LICENSE-2.0
 *
 *            Unless required by applicable law or agreed to in writing,
 *            software distributed under the License is distributed on an
 *            "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *            KIND, either express or implied.  See the License for the
 *            specific language governing permissions and limitations
 *            under the License.
 *
 * ====
 *
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
package org.intelligentsia.commons.http.exception;

import org.intelligentsia.commons.http.status.HttpResponseStandardStatus;

/**
 * ExpectationFailedException.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class ExpectationFailedException extends HttpRuntimeException {

	private static final long serialVersionUID = 5007025212270536321L;

	public ExpectationFailedException() {
		super(HttpResponseStandardStatus.EXPECTATION_FAILED);
	}

	public ExpectationFailedException(final String message) {
		super(HttpResponseStandardStatus.EXPECTATION_FAILED, message);
	}

	public ExpectationFailedException(final Throwable cause) {
		super(HttpResponseStandardStatus.EXPECTATION_FAILED, cause);
	}

	public ExpectationFailedException(final String message, final Throwable cause) {
		super(HttpResponseStandardStatus.EXPECTATION_FAILED, message, cause);
	}
}
