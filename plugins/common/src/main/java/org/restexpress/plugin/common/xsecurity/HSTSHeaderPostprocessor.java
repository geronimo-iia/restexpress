/*
    Copyright 2014, Strategic Gains, Inc.

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
package org.restexpress.plugin.common.xsecurity;

import org.restexpress.Response;
import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * {@link HSTSHeaderPostprocessor} add {@link Response} header
 * "Strict-Transport-Security" with 6 month.
 * <p>
 * See <a href="http://tools.ietf.org/html/rfc6797">HTTP Strict Transport
 * Security</a>
 * </p>
 * 
 * @author pbj23000
 * @since 8 July, 2014
 * 
 */
public class HSTSHeaderPostprocessor implements Postprocessor {

	@Override
	public void process(MessageContext context) {
		// use 6 months
		context.getResponse().addHeader("Strict-Transport-Security", "max-age=15768000; includeSubDomains");
	}
}
