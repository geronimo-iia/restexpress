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
import java.util.Iterator;
import java.util.ServiceLoader;

import org.jboss.netty.channel.Channel;
import org.restexpress.settings.RestExpressSettings;
import org.restexpress.settings.Settings;

/**
 * {@link RestExpressLauncher} is the main entry point
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
     * @param settingsPath settings file path
     * @throws IOException
     */
    public RestExpressLauncher(Path settingsPath) throws IOException {
        this(Settings.loadFrom(settingsPath));
    }

    /**
     * Build a new instance.
     * 
     * @param settingsStream settings stream
     * @param settings format
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
        server(new RestExpress(settings));
    }

    protected void server(RestExpress restExpress) {
        this.restExpress = restExpress;
    }

    /**
     * Lookup for {@link RestExpressEntryPoint} in class path with {@link ServiceLoader} and initialize them.
     */
    protected void initialize() {
        final ServiceLoader<RestExpressEntryPoint> loader = ServiceLoader.load(RestExpressEntryPoint.class, this.getClass()
                .getClassLoader());
        final Iterator<RestExpressEntryPoint> iterator = loader.iterator();
        while (iterator.hasNext()) {
            RestExpressEntryPoint entryPoint = iterator.next();
            System.out.println("Load : " + entryPoint.getClass().getName());
            entryPoint.onLoad(restExpress);
        }
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
     * Mains methods instantiate {@link RestExpress} and add all existing {@link RestExpressEntryPoint} finded in class path.
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
