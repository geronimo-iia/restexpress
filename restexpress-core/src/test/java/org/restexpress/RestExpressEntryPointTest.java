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
package org.restexpress;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restexpress.observer.SimpleConsoleLogMessageObserver;

/**
 * {@link RestExpressEntryPointTest} test {@link RestExpressEntryPoint}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RestExpressEntryPointTest {

	/**
	 * The REST server that handles the test calls.
	 */
	private static RestExpressLauncher launcher;

	@BeforeClass
	public static void beforeClass() throws Exception {
		launcher = new RestExpressLauncher();
		launcher.server().addMessageObserver(new SimpleConsoleLogMessageObserver());
		launcher.bind();
	}

	@AfterClass
	public static void afterClass() {
		launcher.shutdown();
	}

	@Test
	public void checkFakeEntryPointIsLoaded() {
		Assert.assertEquals(Boolean.TRUE, launcher.server().context().get("TEST"));
	}
}
