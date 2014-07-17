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
package org.restexpress.postprocessor;

import org.restexpress.pipeline.MessageContext;
import org.restexpress.pipeline.Postprocessor;

/**
 * @author toddf
 * @since Jul 2, 2014
 */
public class TestPostprocessor implements Postprocessor {
	private int callCount = 0;

	@Override
	public void process(MessageContext context) {
		++callCount;
	}

	public int callCount() {
		return callCount;
	}

	public void resetCallCount() {
		this.callCount = 0;
	}
}
