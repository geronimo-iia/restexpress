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
package io.airdock.restexpress;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import org.restexpress.RestExpressLauncher;
import org.restexpress.serialization.SerializationProvider;
import org.restexpress.settings.RestExpressSettings;
import org.restexpress.settings.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * {@link Launcher} is the main entry point.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class Launcher extends RestExpressLauncher {
	/**
	 * Logger instance.
	 */
	private Logger logger = LoggerFactory.getLogger(Launcher.class);
	/**
	 * Context instance.
	 */
	private Context context;
	/**
	 * List of entry point.
	 */
	private List<ServiceEntryPoint> entryPoints;

	public Launcher() {
		super();
	}

	public Launcher(InputStream jsonSettingsStream) throws IOException {
		super(jsonSettingsStream);
	}

	public Launcher(Path jsonSettingsPath) throws IOException {
		super(jsonSettingsPath);
	}

	public Launcher(RestExpressSettings settings, SerializationProvider serializationProvider) {
		super(settings, serializationProvider);
	}

	public Launcher(RestExpressSettings settings) {
		super(settings);
	}

	protected void initialize() {
		// create context
		context = new Context(server(), new HashMap<String, Object>());
		// lookup for ServiceEntryPoint
		entryPoints = lookup(getClass().getClassLoader());
		if (entryPoints.isEmpty()) {
			logger.warn("No ServiceEntryPoint was found");
		}

		// initialize entry point
		for (ServiceEntryPoint entryPoint : entryPoints) {
			entryPoint.initialize(context);
		}
		// add a shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				destroy();
			}
		}));
	}

	protected void destroy() {
		if (entryPoints != null)
			for (ServiceEntryPoint entryPoint : entryPoints) {
				try {
					entryPoint.destroy();
				} catch (Throwable throwable) {
					logger.error("Error when destroying {} ", entryPoint.name(), throwable);
				}
			}
		entryPoints = null;
		context = null;
		logger.debug("Server is destroyed");
	}

	public Context context() {
		return context;
	}

	/**
	 * Instantiate a new {@link RestExpressLauncher} from argument.
	 * 
	 * @param args
	 * @param defaultEnvironmentName
	 * @return a {@link Launcher} instance.
	 * @throws IOException
	 */
	public static Launcher instanciateFrom(String[] args, String defaultEnvironmentName) throws IOException {
		String environmentName = defaultEnvironmentName;
		if (args.length > 0) {
			environmentName = args[0];
		}
		Path settingsPath = Paths.get(".", "config", environmentName + ".json");
		Launcher airDock = null;
		if (settingsPath.toFile().exists()) {
			airDock = new Launcher(Settings.loadFromJson(settingsPath));
		} else {
			// lookup to resource
			InputStream stream = RestExpressLauncher.class.getClassLoader().getResourceAsStream(environmentName);
			if (stream != null) {
				airDock = new Launcher(Settings.loadFromJson(stream));
			} else {
				airDock = new Launcher();
			}
		}
		return airDock;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static int main(String[] args) throws IOException {
		// load from settings
		Launcher launcher = instanciateFrom(args, "airdock");
		// create and run
		launcher.run();
		return 0;
	}

	/**
	 * Lookup in specified class loader all service {@link ServiceEntryPoint}.
	 * 
	 * @param classLoader
	 * @return a list of {@link ServiceEntryPoint}.
	 */
	public List<ServiceEntryPoint> lookup(final ClassLoader classLoader) {
		final ServiceLoader<ServiceEntryPoint> loader = ServiceLoader.load(ServiceEntryPoint.class, classLoader);
		final Iterator<ServiceEntryPoint> iterator = loader.iterator();
		List<ServiceEntryPoint> entryPoints = Lists.newArrayList();
		while (iterator.hasNext()) {
			try {
				ServiceEntryPoint entryPoint = iterator.next();
				entryPoints.add(entryPoint);
				logger.info("Find ServiceEntryPoint {}", entryPoint.name());
			} catch (Throwable throwable) {
				logger.error("Error when lookup for ServiceEntryPoint", throwable);
			}
		}
		return entryPoints;
	}

}
