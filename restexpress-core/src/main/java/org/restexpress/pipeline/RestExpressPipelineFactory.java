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

import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpContentDecompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.stream.ChunkedWriteHandler;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Provides a tiny DSL to define the pipeline features.
 * 
 * @author toddf
 * @since Aug 27, 2010
 */
public class RestExpressPipelineFactory implements ChannelPipelineFactory {

	/**
	 * Default max content length of the aggregated (chunked) content. If the
	 * length of the aggregated content exceeds this value, a
	 * TooLongFrameException will be raised during the request, which can be
	 * mapped in the RestExpress server to return a BadRequestException, if
	 * desired.
	 */
	public static final int DEFAULT_MAX_CONTENT_LENGTH = 20480;

	private final List<ChannelHandler> requestHandlers = Lists.newArrayList();
	private ExecutionHandler executionHandler = null;
	private int maxContentLength = DEFAULT_MAX_CONTENT_LENGTH;
	private SSLContext sslContext = null;

	/**
	 * Build a new instance of {@link RestExpressPipelineFactory}.
	 */
	public RestExpressPipelineFactory() {
		super();
	}

	/**
	 * @param handler
	 *            {@link ExecutionHandler} instance to set onto this pipeline
	 * @return this RestExpressPipelineFactory for method chaining.
	 */
	public RestExpressPipelineFactory setExecutionHandler(final ExecutionHandler handler) {
		this.executionHandler = handler;
		return this;
	}

	/**
	 * @param handler
	 *            {@link ChannelHandler} to add at pipeline end.
	 * @return this RestExpressPipelineFactory for method chaining.
	 * @throws NullPointerException
	 *             if handler is null
	 */
	public RestExpressPipelineFactory addRequestHandler(final ChannelHandler handler) throws NullPointerException {
		if (!requestHandlers.contains(Preconditions.checkNotNull(handler, "request handler could not be null"))) {
			requestHandlers.add(handler);
		}

		return this;
	}

	/**
	 * Set the maximum length of the aggregated (chunked) content. If the length
	 * of the aggregated content exceeds this value, a TooLongFrameException
	 * will be raised during the request, which can be mapped in the RestExpress
	 * server to return a BadRequestException, if desired. By default the
	 * maximum length is 20480 (
	 * {@link RestExpressPipelineFactory#DEFAULT_MAX_CONTENT_LENGTH}.
	 * 
	 * @param value
	 * @return this RestExpressPipelineFactory for method chaining.
	 */
	public RestExpressPipelineFactory setMaxContentLength(final int value) {
		this.maxContentLength = value;
		return this;
	}

	/**
	 * Set SSL context.
	 * 
	 * @param sslContext
	 * @return this RestExpressPipelineFactory for method chaining.
	 */
	public RestExpressPipelineFactory setSSLContext(final SSLContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}

	/**
	 * @return maximum length of the aggregated (chunked) content
	 */
	public int maxContentLength() {
		return maxContentLength;
	}

	/**
	 * @return a {@link List} of {@link ChannelHandler} added at pipeline end.
	 */
	public List<ChannelHandler> requestHandlers() {
		return requestHandlers;
	}

	/**
	 * @return {@link SSLContext} instance.
	 */
	public SSLContext sslContext() {
		return sslContext;
	}

	/**
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = Channels.pipeline();

		if (null != sslContext) {
			final SSLEngine sslEngine = sslContext.createSSLEngine();
			sslEngine.setUseClientMode(false);
			final SslHandler sslHandler = new SslHandler(sslEngine);
			pipeline.addLast("ssl", sslHandler);
		}
		// Upstream handlers
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("aggregator", new HttpChunkAggregator(maxContentLength));
		pipeline.addLast("inflater", new HttpContentDecompressor());
		// Downstream handlers
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("chunkWriter", new ChunkedWriteHandler());
		pipeline.addLast("deflater", new HttpContentCompressor());

		// add optional execution handler
		if (executionHandler != null) {
			pipeline.addLast("executionHandler", executionHandler);
		}
		// add all request handler
		for (final ChannelHandler handler : requestHandlers) {
			pipeline.addLast(handler.getClass().getSimpleName(), handler);
		}

		return pipeline;
	}
}
