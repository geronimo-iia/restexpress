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
package org.restexpress.util;

import org.restexpress.RestExpress;

/**
 * {@link DefaultShutdownHook} implements an hook which try to shutdown our
 * server when JVM stop.
 * 
 * @author toddf
 * @since Feb 1, 2011
 */
public class DefaultShutdownHook extends Thread {
	private final RestExpress server;

	/**
	 * Build a new instance of {@link DefaultShutdownHook}.
	 * 
	 * @param server
	 */
	public DefaultShutdownHook(final RestExpress server) {
		super();
		this.server = server;
	}

	@Override
	public void run() {
		System.out.println(server.settings().serverSettings().getName() + " server detected JVM shutdown...");
		server.shutdown();
	}
}
