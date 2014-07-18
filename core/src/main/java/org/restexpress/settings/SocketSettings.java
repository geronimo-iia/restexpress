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

/**
 * SocketSettings group all socket settings.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since May 31, 2012
 */
public class SocketSettings implements Serializable{
	private static final long serialVersionUID = 2580399612791779048L;
	private boolean useTcpNoDelay = true;
	private int soLinger = -1; // disabled by default
	private int receiveBufferSize = 262140; // Java default
	private int connectTimeoutMillis = 10000; // netty default

	/**
	 * Build a new instance.
	 */
	public SocketSettings() {
		super();
	}

	public boolean useTcpNoDelay() {
		return useTcpNoDelay;
	}

	public SocketSettings setUseTcpNoDelay(final boolean useTcpNoDelay) {
		this.useTcpNoDelay = useTcpNoDelay;
		return this;
	}

	public int getSoLinger() {
		return soLinger;
	}

	public SocketSettings setSoLinger(final int soLinger) {
		this.soLinger = soLinger;
		return this;
	}

	public int getReceiveBufferSize() {
		return receiveBufferSize;
	}

	public SocketSettings setReceiveBufferSize(final int receiveBufferSize) {
		this.receiveBufferSize = receiveBufferSize;
		return this;
	}

	public int getConnectTimeoutMillis() {
		return connectTimeoutMillis;
	}

	public SocketSettings setConnectTimeoutMillis(final int connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
		return this;
	}

	@Override
	public String toString() {
		return "SocketSettings {useTcpNoDelay=\"" + useTcpNoDelay + "\", soLinger=\"" + soLinger + "\", receiveBufferSize=\"" + receiveBufferSize + "\", connectTimeoutMillis=\"" + connectTimeoutMillis + "\"}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + connectTimeoutMillis;
		result = (prime * result) + receiveBufferSize;
		result = (prime * result) + soLinger;
		result = (prime * result) + (useTcpNoDelay ? 1231 : 1237);
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
		final SocketSettings other = (SocketSettings) obj;
		if (connectTimeoutMillis != other.connectTimeoutMillis) {
			return false;
		}
		if (receiveBufferSize != other.receiveBufferSize) {
			return false;
		}
		if (soLinger != other.soLinger) {
			return false;
		}
		if (useTcpNoDelay != other.useTcpNoDelay) {
			return false;
		}
		return true;
	}

}
