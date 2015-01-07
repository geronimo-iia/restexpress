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
package org.restexpress.reader;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.restexpress.Request;
import org.restexpress.Response;

/**
 * {@link EchoService} implmentation.
 * 
 */
@Path("/")
public class EchoService {

	public EchoService() {
	}

	@GET
	public String read(@DefaultValue("arf") @QueryParam("echo") String echo) {
		if (echo == null) {
			return "Please set query-string parameter 'echo' (e.g. ?echo=value)";
		}
		return echo;
	}

	@GET
	@Path("/hello")
	public String hello() {
		return "hello";
	}

	@GET
	@Path("/greeting")
	public String greeting(Request request) {
		String who = request.getQueryStringMap().get("who");
		return "hello " + who + "!";
	}

	@GET
	@Path("/restexpress/{who}")
	public String restexpress(@PathParam("who") String who) {
		return "hello " + who + "!";
	}

	@POST
	@DELETE
	public void dummy(Request request, Response response) {

	}

	@HEAD
    public String info() {
    	return "some info";
    }
}
