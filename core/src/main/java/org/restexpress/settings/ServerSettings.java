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
 Copyright 2012, Strategic Gains, Inc.

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

package org.restexpress.settings;

import java.io.Serializable;

import org.restexpress.ContentType;

/**
 * {@link ServerSettings} group all server settings.
 * 
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 * @since May 31, 2012
 * 
 */
public class ServerSettings implements Serializable {

	private static final long serialVersionUID = 6771860371807291176L;
	private String baseUrl = "http://localhost:8081";
	private boolean useSystemOut = Boolean.TRUE;
	private boolean enforceHttpSpec = Boolean.FALSE;

	private String name = "RestExpress";
	private int port = 8081;
	private boolean keepAlive = Boolean.TRUE;
	private boolean reuseAddress = Boolean.TRUE;
	private int maxContentSize = 25600;

	/**
	 * This controls the number of concurrent connections the application can
	 * handle. Netty default is 2 * number of processors (or cores). Zero (0)
	 * indicates to use the Netty default.
	 **/
	private int ioThreadCount = 0;

	/**
	 * This controls the size of the thread pool for back-end executors. In
	 * essence, this is the number of blocking requests the application can
	 * process simultaneously.
	 */
	private int executorThreadPoolSize = 10;

	/***
	 * Default format serialization (JSON per default).
	 */
	private String defaultFormat = ContentType.JSON;

	/**
	 * Build a new instance.
	 */
	public ServerSettings() {
		super();
	}

	public String getName() {
		return name;
	}

	public ServerSettings setName(final String name) {
		this.name = name;
		return this;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public ServerSettings setKeepAlive(final boolean isKeepAlive) {
		this.keepAlive = isKeepAlive;
		return this;
	}

	public boolean isReuseAddress() {
		return reuseAddress;
	}

	public ServerSettings setReuseAddress(final boolean reuseAddress) {
		this.reuseAddress = reuseAddress;
		return this;
	}

	public int getIoThreadCount() {
		return ioThreadCount;
	}

	/**
	 * Set the number of NIO/HTTP-handling worker threads. This value controls
	 * the number of simultaneous connections the application can handle.
	 * 
	 * The default (if this value is not set, or set to zero) is the Netty
	 * default, which is 2 times the number of processors (or cores).
	 * 
	 * @param value
	 *            the number of desired NIO worker threads.
	 * @return the RestExpress instance.
	 */
	public ServerSettings setIoThreadCount(final int ioThreadCount) {
		this.ioThreadCount = ioThreadCount;
		return this;
	}

	/**
	 * Returns the number of background request-handling (executor) threads.
	 * 
	 * @return the number of executor threads.
	 */
	public int getExecutorThreadPoolSize() {
		return executorThreadPoolSize;
	}

	/**
	 * Set the number of background request-handling (executor) threads. This
	 * value controls the number of simultaneous blocking requests that the
	 * server can handle. For longer-running requests, a higher number may be
	 * indicated.
	 * 
	 * For VERY short-running requests, a value of zero will cause no background
	 * threads to be created, causing all processing to occur in the NIO
	 * (front-end) worker thread.
	 * 
	 * @param value
	 *            the number of executor threads to create.
	 * @return the RestExpress instance.
	 */
	public ServerSettings setExecutorThreadPoolSize(final int executorThreadCount) {
		this.executorThreadPoolSize = executorThreadCount;
		return this;
	}

	public int getPort() {
		return port;
	}

	public ServerSettings setPort(final int port) {
		this.port = port;
		return this;
	}

	public int getMaxContentSize() {
		return maxContentSize;
	}
	/**
	 * Set the maximum length of the content in a request. If the length of the
	 * content exceeds this value, the server closes the connection immediately
	 * without sending a response.
	 * 
	 * @param size
	 *            the maximum size in bytes.
	 * @return the RestExpress instance.
	 */
	public ServerSettings setMaxContentSize(final int maxContentSize) {
		this.maxContentSize = maxContentSize;
		return this;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public ServerSettings setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
		return this;
	}

	public boolean isUseSystemOut() {
		return useSystemOut;
	}

	public ServerSettings setUseSystemOut(final boolean useSystemOut) {
		this.useSystemOut = useSystemOut;
		return this;
	}

	public boolean isEnforceHttpSpec() {
		return enforceHttpSpec;
	}

	public ServerSettings setEnforceHttpSpec(final boolean enforceHttpSpec) {
		this.enforceHttpSpec = enforceHttpSpec;
		return this;
	}

	public String getDefaultFormat() {
		return defaultFormat;
	}

	public ServerSettings setDefaultFormat(String defaultFormat) {
		this.defaultFormat = defaultFormat;
		return this;
	}

	@Override
	public String toString() {
		return "ServerSettings {baseUrl=\"" + baseUrl + "\", useSystemOut=\"" + useSystemOut + "\", enforceHttpSpec=\"" + enforceHttpSpec + "\", name=\"" + name + "\", port=\"" + port + "\", keepAlive=\"" + keepAlive + "\", reuseAddress=\""
				+ reuseAddress + "\", maxContentSize=\"" + maxContentSize + "\", ioThreadCount=\"" + ioThreadCount + "\", executorThreadPoolSize=\"" + executorThreadPoolSize + "\", defaultFormat=\"" + defaultFormat + "\"}";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseUrl == null) ? 0 : baseUrl.hashCode());
		result = prime * result + ((defaultFormat == null) ? 0 : defaultFormat.hashCode());
		result = prime * result + (enforceHttpSpec ? 1231 : 1237);
		result = prime * result + executorThreadPoolSize;
		result = prime * result + ioThreadCount;
		result = prime * result + (keepAlive ? 1231 : 1237);
		result = prime * result + maxContentSize;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + port;
		result = prime * result + (reuseAddress ? 1231 : 1237);
		result = prime * result + (useSystemOut ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerSettings other = (ServerSettings) obj;
		if (baseUrl == null) {
			if (other.baseUrl != null)
				return false;
		} else if (!baseUrl.equals(other.baseUrl))
			return false;
		if (defaultFormat == null) {
			if (other.defaultFormat != null)
				return false;
		} else if (!defaultFormat.equals(other.defaultFormat))
			return false;
		if (enforceHttpSpec != other.enforceHttpSpec)
			return false;
		if (executorThreadPoolSize != other.executorThreadPoolSize)
			return false;
		if (ioThreadCount != other.ioThreadCount)
			return false;
		if (keepAlive != other.keepAlive)
			return false;
		if (maxContentSize != other.maxContentSize)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port != other.port)
			return false;
		if (reuseAddress != other.reuseAddress)
			return false;
		if (useSystemOut != other.useSystemOut)
			return false;
		return true;
	}

}
