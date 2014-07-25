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
package org.restexpress.pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.restexpress.pipeline.handler.DefaultRequestHandler;
import org.restexpress.pipeline.observer.CounterMessageObserver;
import org.restexpress.pipeline.writer.StringBufferHttpResponseWriter;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.util.TestUtilities;

public class AbstractWrapperResponse {

	protected DefaultRequestHandler messageHandler;
	protected CounterMessageObserver observer;
	protected Channel channel;
	protected ChannelPipeline pl;
	protected StringBuffer responseBody;
	protected Map<String, List<String>> responseHeaders;

	protected void initialize(RouteDeclaration routes) throws Exception {

		responseBody = new StringBuffer();
		responseHeaders = new HashMap<String, List<String>>();

		messageHandler = TestUtilities.newDefaultRequestHandler(routes, new StringBufferHttpResponseWriter(responseHeaders, responseBody));

		observer = new CounterMessageObserver();
		messageHandler.dispatcher().addMessageObserver(observer);

		PipelineBuilder pf = new PipelineBuilder().addRequestHandler(messageHandler);
		pl = pf.getPipeline();
		ChannelFactory channelFactory = new DefaultLocalServerChannelFactory();
		channel = channelFactory.newChannel(pl);
	}

}
