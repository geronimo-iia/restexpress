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

import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * {@link ContentSecurityPolicyHeaderPostProcessor} adds default
 * "Content-Security-Policy" response header.
 * 
 * @see <a href="http://www.w3.org/TR/CSP/">Content Security Policy</a>
 * 
 * @author pbj23000
 * @since 8 July, 2014
 * 
 */
public class ContentSecurityPolicyHeaderPostProcessor implements Postprocessor {
	private static final String CONTENTSECURITYPOLICY = "Content-Security-Policy";

	@Override
	public void process(MessageContext context) {
		// default-src
		// script-src
		// object-src
		// style-src
		// img-src
		// media-src
		// frame-src
		// font-src
		// connect-src
		// sandbox (optional)
		// report-uri
		context.getResponse().addHeader(CONTENTSECURITYPOLICY, "default-src \'self\'");
	}
}
