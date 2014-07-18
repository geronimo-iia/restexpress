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
package com.echo;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.echo.serialization.ResponseProcessors;
import com.strategicgains.restexpress.Format;
import com.strategicgains.restexpress.Parameters;
import com.strategicgains.restexpress.RestExpress;
import com.strategicgains.restexpress.pipeline.SimpleConsoleLogMessageObserver;
import com.strategicgains.restexpress.plugin.route.RoutesMetadataPlugin;
import com.strategicgains.restexpress.util.Environment;

/**
 * The main entry-point into the RestExpress Echo example services.
 * 
 * @author toddf
 * @since Aug 31, 2009
 */
public class Main
{
	public static void main(String[] args) throws Exception
	{
		Configuration config = loadEnvironment(args);
		RestExpress server = new RestExpress()
		    .setName(config.getName())
		    .setPort(config.getPort())
		    .setDefaultFormat(config.getDefaultFormat())
		    .putResponseProcessor(Format.JSON, ResponseProcessors.json())
		    .putResponseProcessor(Format.XML, ResponseProcessors.xml())
		    .putResponseProcessor(Format.WRAPPED_JSON, ResponseProcessors.wrappedJson())
		    .putResponseProcessor(Format.WRAPPED_XML, ResponseProcessors.wrappedXml())
   		    .addMessageObserver(new SimpleConsoleLogMessageObserver());


		defineRoutes(server, config);

	    if (config.getWorkerCount() > 0)
	    {
	    	server.setExecutorThreadCount(config.getWorkerCount());
	    }
	    
	    if (config.getExecutorThreadCount() > 0)
	    {
	    	server.setExecutorThreadCount(config.getExecutorThreadCount());
	    }

	    new RoutesMetadataPlugin().register(server)
			.parameter(Parameters.Cache.MAX_AGE, 86400);	// Cache for 1 day (24 hours).

		mapExceptions(server);
		server.bind();
		server.awaitShutdown();
	}

	/**
     * @param server
     * @param config
     */
    private static void defineRoutes(RestExpress server, Configuration config)
    {
		// This route supports POST and PUT, echoing the body in the response.
    	// GET and DELETE are also supported but require an 'echo' header or query-string parameter.
		server.uri("/echo/{delay_ms}", config.getEchoController())
			.noSerialization();

		// Waits the delay_ms number of milliseconds and responds with a 200.
		// Supports GET, PUT, POST, DELETE methods.
		server.uri("/success/{delay_ms}.{format}", config.getSuccessController());

		// Waits the delay_ms number of milliseconds and responds with the
		// specified HTTP response code.
		// Supports GET, PUT, POST, DELETE methods.
		server.uri("/status/{delay_ms}/{http_response_code}.{format}", config.getStatusController());
    }

	/**
     * @param server
     */
    private static void mapExceptions(RestExpress server)
    {
//    	server
//    	.mapException(ItemNotFoundException.class, NotFoundException.class)
//    	.mapException(DuplicateItemException.class, ConflictException.class)
//    	.mapException(ValidationException.class, BadRequestException.class);
    }

	private static Configuration loadEnvironment(String[] args)
    throws FileNotFoundException, IOException
    {
	    if (args.length > 0)
		{
			return Environment.from(args[0], Configuration.class);
		}

	    return Environment.fromDefault(Configuration.class);
    }
}
