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
package org.restexpress.settings;

import java.io.Serializable;
import java.nio.file.Path;

/**
 * {@link RestExpressSettings} is our main settings class. In order to manage
 * your application paramater, you can extends this class, and reuse
 * {@link RestExpressSettings#newInstanceFromJson(Path, Class)} to load it.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 */
public class RestExpressSettings implements Serializable{

	private static final long serialVersionUID = -7187596937362551328L;

	/**
	 * {@link ServerSettings} instance.
	 */
	private final ServerSettings serverSettings;

	/**
	 * {@link SocketSettings} instance.
	 */
	private final SocketSettings socketSettings;

	/**
	 * Build a new instance with default value.
	 */
	public RestExpressSettings() {
		super();
		serverSettings = new ServerSettings();
		socketSettings = new SocketSettings();
	}

	public ServerSettings serverSettings() {
		return serverSettings;
	}

	public SocketSettings socketSettings() {
		return socketSettings;
	}

	@Override
	public String toString() {
		return "RestExpressSettings {serverSettings=\"" + serverSettings + "\", socketSettings=\"" + socketSettings + "\"}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((serverSettings == null) ? 0 : serverSettings.hashCode());
		result = (prime * result) + ((socketSettings == null) ? 0 : socketSettings.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RestExpressSettings other = (RestExpressSettings) obj;
		if (serverSettings == null) {
			if (other.serverSettings != null) {
				return false;
			}
		} else if (!serverSettings.equals(other.serverSettings)) {
			return false;
		}
		if (socketSettings == null) {
			if (other.socketSettings != null) {
				return false;
			}
		} else if (!socketSettings.equals(other.socketSettings)) {
			return false;
		}
		return true;
	}

}
