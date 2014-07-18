/*
    Copyright 2010, Strategic Gains, Inc.

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.restexpress.common.exception.ConfigurationException;

/**
 * @author kevwil
 * @author toddf
 * @since Dec 16, 2010
 */
public abstract class Environment {
	private static final String ENVIRONMENT_DIR = "config/";
	private static final String PROPERTIES_FILENAME = "/environment.properties";
	private static final String DEFAULT_ENVIRONMENT = "dev";

	public static <T extends Environment> T fromDefault(final Class<T> type) throws FileNotFoundException, IOException {
		return from(DEFAULT_ENVIRONMENT, type);
	}

	public static <T extends Environment> T from(final String environmentName, final Class<T> type) throws FileNotFoundException, IOException {
		final T instance = newEnvironment(type);

		if (environmentName != null) {
			instance.load(ENVIRONMENT_DIR + environmentName + PROPERTIES_FILENAME);
		}

		return instance;
	}

	protected void load(final String filename) throws ConfigurationException, FileNotFoundException, IOException {
		// log.debug("loading environment properties from " + environmentFile);
		final Properties p = readProperties(filename);
		fillValues(p);
	}

	protected abstract void fillValues(Properties p) throws ConfigurationException;

	protected Properties readProperties(final String environmentFile) throws FileNotFoundException, IOException {
		FileInputStream fis = null;
		try {
			final File envFile = new File(environmentFile);
			final Properties properties = new Properties();
			fis = new FileInputStream(envFile);
			properties.load(fis);
			return properties;
		} catch (final FileNotFoundException e) {
			// log.error("could not find properties file: " +
			// e.getLocalizedMessage());
			throw e;
		} catch (final IOException e) {
			// log.error("error reading properties file: ", e);
			throw e;
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (final IOException e) {
				// too late to care at this point
			}
		}
	}

	private static <T> T newEnvironment(final Class<T> type) {
		T instance = null;

		try {
			instance = type.newInstance();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}

		return instance;
	}
}
