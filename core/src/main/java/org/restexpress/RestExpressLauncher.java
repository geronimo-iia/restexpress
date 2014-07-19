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

import org.jboss.netty.channel.Channel;
import org.restexpress.serialization.SerializationProvider;
import org.restexpress.settings.RestExpressSettings;
import org.restexpress.settings.Settings;

/**
 * RestExpressLauncher is the main entry point
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class RestExpressLauncher implements Runnable {

	/**
	 * {@link RestExpress} instance.
	 */
	private RestExpress restExpress;

	/**
	 * Build a new instance with default parameter.
	 */
	public RestExpressLauncher() {
		this(new RestExpressSettings());
	}

	/**
	 * Build a new instance.
	 * 
	 * @param jsonSettingsPath
	 *            json settings file path
	 * @throws IOException
	 */
	public RestExpressLauncher(Path jsonSettingsPath) throws IOException {
		this(Settings.loadFromJson(jsonSettingsPath));
	}

	/**
	 * Build a new instance.
	 * 
	 * @param jsonSettingsStream
	 *            json settings stream
	 * @throws IOException
	 */
	public RestExpressLauncher(InputStream jsonSettingsStream) throws IOException {
		this(Settings.loadFromJson(jsonSettingsStream));
	}

	/**
	 * Build a new instance.
	 * 
	 * @param serializationProvider
	 * @param settings
	 */
	public RestExpressLauncher(RestExpressSettings settings) {
		server(new RestExpress(settings));
	}

	public RestExpressLauncher(RestExpressSettings settings, SerializationProvider serializationProvider) {
		server(new RestExpress(settings, serializationProvider));
	}

	protected void server(RestExpress restExpress) {
		this.restExpress = restExpress;
	}

	protected void initialize() {
		// nothing todo
	}

	protected void destroy() {
		// nothing todo
	}

	@Override
	public void run() {
		initialize();
		bind();
		awaitShutdown();
		destroy();
	}

	public Channel bind() {
		initialize();
		return server().bind();
	}

	public void awaitShutdown() {
		server().awaitShutdown();
	}

	public void shutdown() {
		server().shutdown();
		destroy();
	}

	public RestExpress server() {
		return restExpress;
	}

	/**
	 * Instantiate a new {@link RestExpressLauncher} from argument.
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
		Path settingsPath = Paths.get(".", "config", environmentName + ".json");
		RestExpressLauncher restExpressLauncher = null;
		if (settingsPath.toFile().exists()) {
			restExpressLauncher = new RestExpressLauncher(settingsPath);
		} else {
			// lookup to resource
			InputStream stream = RestExpressLauncher.class.getClassLoader().getResourceAsStream(environmentName);
			if (stream != null) {
				restExpressLauncher = new RestExpressLauncher(stream);
			} else {
				restExpressLauncher = new RestExpressLauncher();
			}
		}
		return restExpressLauncher;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static int main(String[] args) throws IOException {
		// load from settings
		RestExpressLauncher restExpressLauncher =  instanciateFrom(args,  "restexpress");
		// create and run
		restExpressLauncher.run();
		return 0;
	}

}