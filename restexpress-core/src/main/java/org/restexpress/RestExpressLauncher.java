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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.net.ssl.SSLContext;

import org.jboss.netty.channel.Channel;
import org.restexpress.settings.RestExpressSettings;
import org.restexpress.settings.Settings;

/**
 * {@link RestExpressLauncher} is the main entry point.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RestExpressLauncher implements Runnable, RestExpressLifeCycle {

	/**
	 * {@link RestExpressService} instance.
	 */
	private RestExpressService restExpressService;

	/**
	 * Build a new instance with default parameter.
	 */
	public RestExpressLauncher() {
		this(new RestExpressSettings());
	}

	/**
	 * Build a new instance.
	 * 
	 * @param settingsPath
	 *            settings file path
	 * @throws IOException
	 */
	public RestExpressLauncher(Path settingsPath) throws IOException {
		this(Settings.loadFrom(settingsPath));
	}

	/**
	 * Build a new instance.
	 * 
	 * @param settingsStream
	 *            settings stream
	 * @param settings
	 *            format
	 * @throws IOException
	 */
	public RestExpressLauncher(InputStream settingsStream, Settings settings) throws IOException {
		this(Settings.loadFrom(settingsStream, settings));
	}

	/**
	 * Build a new instance.
	 * 
	 * @param serializationProvider
	 * @param settings
	 */
	public RestExpressLauncher(RestExpressSettings settings) {
		this.restExpressService = RestExpressService.newBuilder(settings)//
				.lookupRestExpressEntryPoint();
	}

	@Override
	public void run() {
		restExpressService.bind();
		restExpressService.awaitShutdown();
	}

	/**
	 * @return {@link RestExpress} server instance.
	 */
	public RestExpress server() {
		return restExpressService;
	}

	/**
	 * @return {@link RestExpressLifeCycle} instance.
	 */
	public RestExpressLifeCycle restExpressLifeCycle() {
		return restExpressService;
	}

	/**
	 * @return
	 * @see org.restexpress.RestExpressService#bind()
	 */
	public Channel bind() {
		return restExpressService.bind();
	}

	/**
	 * @param port
	 * @return
	 * @see org.restexpress.RestExpressService#bind(int)
	 */
	public Channel bind(int port) {
		return restExpressService.bind(port);
	}

	/**
	 * @param sslContext
	 * @param port
	 * @return
	 * @see org.restexpress.RestExpressService#bind(javax.net.ssl.SSLContext,
	 *      int)
	 */
	public Channel bind(SSLContext sslContext, int port) {
		return restExpressService.bind(sslContext, port);
	}

	/**
	 * 
	 * @see org.restexpress.RestExpressService#awaitShutdown()
	 */
	public void awaitShutdown() {
		restExpressService.awaitShutdown();
	}

	/**
	 * 
	 * @see org.restexpress.RestExpressService#shutdown()
	 */
	public void shutdown() {
		restExpressService.shutdown();
	}

	/**
	 * Create an {@link SSLContext}.
	 * 
	 * @param keyStore
	 * @param filePassword
	 * @param keyPassword
	 * @return {@link SSLContext} instance.
	 * @throws Exception
	 * @see {@link RestExpressService#loadContext(String, String, String)}
	 */
	public static SSLContext loadContext(final String keyStore, final String filePassword, final String keyPassword) throws Exception {
		return RestExpressService.loadContext(keyStore, filePassword, keyPassword);
	}

	/**
	 * Instantiate a new {@link RestExpressLauncher} from argument.
	 * 
	 * @param args
	 * @param defaultEnvironmentName
	 * @return a {@link RestExpressLauncher} instance.
	 * @throws IOException
	 */
	public static RestExpressLauncher instanciateFrom(String[] args, String defaultEnvironmentName) throws IOException {
		String environmentName = defaultEnvironmentName;
		if (args.length > 0) {
			environmentName = args[0];
		}

		for (Settings settings : Settings.values()) {
			Path settingsPath = Paths.get(".", "config", environmentName + "." + settings.name().toLowerCase());
			if (settingsPath.toFile().exists()) {
				return new RestExpressLauncher(settingsPath);
			}
			InputStream stream = RestExpressLauncher.class.getClassLoader().getResourceAsStream(environmentName);
			if (stream != null) {
				return new RestExpressLauncher(stream, settings);
			}
		}
		return new RestExpressLauncher();
	}

	/**
	 * Mains methods instantiate {@link RestExpress} and add all existing
	 * {@link RestExpressEntryPoint} find in class path.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static int main(String[] args) throws IOException {
		// load from settings
		RestExpressLauncher restExpressLauncher = instanciateFrom(args, "restexpress");
		// create and run
		restExpressLauncher.run();
		return 0;
	}

}
