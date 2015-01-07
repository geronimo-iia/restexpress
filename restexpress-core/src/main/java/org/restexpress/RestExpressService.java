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

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.ws.rs.ext.ParamConverterProvider;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.restexpress.context.ServerContext;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.domain.response.ErrorResult;
import org.restexpress.pipeline.MessageObserver;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.pipeline.RestExpressPipelineFactory;
import org.restexpress.pipeline.handler.RestExpressRequestHandler;
import org.restexpress.pipeline.handler.RestExpressRequestHandlerBuilder;
import org.restexpress.plugin.Plugin;
import org.restexpress.plugin.PluginManager;
import org.restexpress.plugin.PluginService;
import org.restexpress.processor.DefaultContentTypeFinallyProcessor;
import org.restexpress.processor.FileHeaderPostProcessor;
import org.restexpress.reader.JaxRsReader;
import org.restexpress.response.ResponseWrapper;
import org.restexpress.response.SerializationProvider;
import org.restexpress.response.Wrapper;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.route.RouteMapping;
import org.restexpress.route.invoker.RestExpressParamConverterProvider;
import org.restexpress.route.parameterized.ParameterizedRouteBuilder;
import org.restexpress.route.regex.RegexRouteBuilder;
import org.restexpress.serialization.JacksonJsonProcessor;
import org.restexpress.serialization.JacksonXmlProcessor;
import org.restexpress.serialization.Processor;
import org.restexpress.serialization.TextProcessor;
import org.restexpress.settings.RestExpressSettings;
import org.restexpress.settings.Settings;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

/**
 * {@link RestExpressService} implement {@link RestExpress} and
 * {@link RestExpressLifeCycle} and act as a builder.
 * 
 * Primary entry point to create a RestExpress service. All that's required is a
 * RouteDeclaration.
 * 
 * <p>
 * By default, RestExpress uses port 8081. Supports JSON, XML, etc.. And
 * displays some messages on System.out. These can be altered with the with
 * settings(). By default, all error are wrapper using this format (@see
 * {@link ErrorResult}.
 * <p>
 * The default input and output format for messages is JSON. To change that, use
 * the setDefaultFormat(String) DSL modifier, passing the format to use by
 * default. Make sure there's a corresponding SerializationProcessor for that
 * particular format. The Format class has the basics.
 * 
 * @see RestExpressSettings.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 */
public class RestExpressService implements RestExpress, RestExpressLifeCycle {

	/**
	 * Open Channel.
	 */
	private final ChannelGroup channelGroup;

	/**
	 * {@link RestExpressSettings} instance.
	 */
	private final RestExpressSettings settings;

	/**
	 * {@link ServerContext} instance.
	 */
	private final ServerContext context;

	/**
	 * Builder of {@link RestExpressRequestHandler}.
	 */
	private final RestExpressRequestHandlerBuilder requestHandlerBuilder;

	/**
	 * {@link PluginManager} manage all {@link Plugin}.
	 */
	private final PluginManager pluginManager;

	/**
	 * {@link RouteDeclaration} manager.
	 */
	private final RouteDeclaration routeDeclarations;

	/**
	 * {@link RouteMapping} instance.
	 */
	private RouteMapping routeMapping;

	/**
	 * {@link SerializationProvider} instance.
	 */
	private SerializationProvider serializationProvider;

	/**
	 * {@link ServerBootstrap} instance.
	 */
	private ServerBootstrap bootstrap = null;

	/**
	 * {@link ParamConverterProvider} instance.
	 */
	private ParamConverterProvider paramConverterProvider = null;

	/**
	 * Build a new instance of {@link RestExpressService} with specified
	 * settings.
	 * 
	 * @param settings
	 *            {@link RestExpressSettings} instance
	 * @param requestHandlerBuilder
	 *            {@link RestExpressRequestHandlerBuilder} to use
	 * @throws NullPointerException
	 *             if settings is null
	 */
	private RestExpressService(RestExpressSettings settings, RestExpressRequestHandlerBuilder requestHandlerBuilder) throws NullPointerException {
		super();
		this.settings = Preconditions.checkNotNull(settings);
		this.requestHandlerBuilder = Preconditions.checkNotNull(requestHandlerBuilder);
		pluginManager = new PluginManager();
		routeDeclarations = new RouteDeclaration();
		context = new ServerContext();

		// add standard Serialization
		if (settings.serverSettings().isUseDefaultSerializationConfiguration()) {
			add(new JacksonJsonProcessor(), Wrapper.newErrorResponseWrapper(), true);
			add(new JacksonXmlProcessor(), Wrapper.newErrorResponseWrapper());
			add(new TextProcessor(), Wrapper.newErrorResponseWrapper());
			// TODO extends with JSEND Wrapper
			// add(new JacksonJsonProcessor(),
			// Wrapper.newJsendResponseWrapper());
			// add(new JacksonXmlProcessor(),
			// Wrapper.newJsendResponseWrapper());
		}

		// add standard Post Processor
		if (settings.serverSettings().isUseDefaultPipelineProcessor()) {
			addPostprocessor(new FileHeaderPostProcessor());
			addFinallyProcessor(new DefaultContentTypeFinallyProcessor());
		}

		// create Channel Group
		channelGroup = new DefaultChannelGroup("group-" + settings.serverSettings().getName());
	}

	/**
	 * @return a new {@link RestExpressService} instance with default settings.
	 */
	public static RestExpressService newBuilder() {
		return newBuilder(Settings.defaultRestExpressSettings());
	}

	/**
	 * @param settings
	 *            {@link RestExpressSettings} to use
	 * @return a new {@link RestExpressService} instance with specified
	 *         settings.
	 */
	public static RestExpressService newBuilder(RestExpressSettings settings) {
		return newBuilder(settings, RestExpressRequestHandlerBuilder.newBuilder());
	}

	/**
	 * 
	 * @param settings
	 *            {@link RestExpressSettings} to use
	 * @param requestHandlerBuilder
	 *            {@link RestExpressRequestHandlerBuilder} to use
	 * @return a new {@link RestExpressService} instance with specified
	 *         settings.
	 */
	public static RestExpressService newBuilder(RestExpressSettings settings, RestExpressRequestHandlerBuilder requestHandlerBuilder) {
		return new RestExpressService(settings, requestHandlerBuilder);
	}

	@Override
	public Channel bind() {
		return bind(settings.serverSettings().getPort());
	}

	@Override
	public Channel bind(int port) {
		return bind(null, port);
	}

	@Override
	public Channel bind(SSLContext sslContext, int port) {

		// update settings if needed
		settings.serverSettings().setPort(port);

		/* initialize plugins */
		pluginManager.initialize(this);

		/* finalize RequestHandler */
		routeDeclarations.createRouteMapping(this);
		routeDeclarations.clear();
		requestHandlerBuilder.setRouteResolver(routeMapping);

		/* bind plugins */
		pluginManager.bind(this);

		final RestExpressRequestHandler requestHandler = requestHandlerBuilder.build();

		// for test purpose
		serializationProvider = requestHandler.serializationProvider();

		/* pipeline factory */
		final RestExpressPipelineFactory pipelineFactory = new RestExpressPipelineFactory()//
				.addRequestHandler(requestHandler)//
				.setMaxContentLength(settings.serverSettings().getMaxContentSize())//
				.setSSLContext(sslContext);

		if (settings.serverSettings().getExecutorThreadPoolSize() > 0) {
			final ExecutionHandler executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(settings.serverSettings().getExecutorThreadPoolSize(), 0, 0));
			pipelineFactory.setExecutionHandler(executionHandler);
		}

		/* server bootstrap */

		// Configure the server.
		if (settings.serverSettings().getIoThreadCount() == 0) {
			bootstrap = createServerNioBootstrap();
		} else {
			bootstrap = createServerNioBootstrap(settings.serverSettings().getIoThreadCount());
		}
		bootstrap.setPipelineFactory(pipelineFactory);
		bootstrap.setOption("child.tcpNoDelay", settings.socketSettings().useTcpNoDelay());
		bootstrap.setOption("child.keepAlive", settings.serverSettings().isKeepAlive());
		bootstrap.setOption("reuseAddress", settings.serverSettings().isReuseAddress());
		bootstrap.setOption("child.soLinger", settings.socketSettings().getSoLinger());
		bootstrap.setOption("connectTimeoutMillis", settings.socketSettings().getConnectTimeoutMillis());
		bootstrap.setOption("receiveBufferSize", settings.socketSettings().getReceiveBufferSize());

		// Bind and start to accept incoming connections.
		if (settings.serverSettings().isUseSystemOut()) {
			System.out.println(settings.serverSettings().getName() + " server listening on port " + port);
		}

		final Channel channel = bootstrap.bind(new InetSocketAddress(port));
		channelGroup.add(channel);

		return channel;
	}

	@Override
	public void awaitShutdown() {
		/* just in case */
		if (bootstrap == null) {
			bind();
		}
		/* add shutdown hook */
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				shutdown();
			}
		});
		// wait for exit
		boolean interrupted = false;
		do {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				interrupted = true;
			}
		} while (!interrupted);
	}

	@Override
	public void shutdown() {
		/* if a previous shutdown has occurs, then we have nothing to do */
		if (bootstrap != null) {
			if (settings.serverSettings().isUseSystemOut()) {
				System.out.println(settings().serverSettings().getName() + " server shutdown...");
			}
			final ChannelGroupFuture future = channelGroup.close();
			future.awaitUninterruptibly();
			// shut down all plugins
			pluginManager.destroy(this);
			// release resources
			bootstrap.getFactory().releaseExternalResources();
			context.clear();
			bootstrap = null;
			serializationProvider = null;
			routeDeclarations.clear();
			routeMapping = null;
		}
	}

	/**
	 * Lookup for {@link RestExpressEntryPoint} in class path with
	 * {@link ServiceLoader} and initialize them.
	 */
	public RestExpressService lookupRestExpressEntryPoint() {
		final ServiceLoader<RestExpressEntryPoint> loader = ServiceLoader.load(RestExpressEntryPoint.class, this.getClass().getClassLoader());
		final Iterator<RestExpressEntryPoint> iterator = loader.iterator();
		while (iterator.hasNext()) {
			RestExpressEntryPoint entryPoint = iterator.next();
			System.out.println("Load : " + entryPoint.getClass().getName());
			entryPoint.onLoad(this);
		}
		return this;
	}

	@Override
	public RestExpressService addMessageObserver(final MessageObserver observer) {
		requestHandlerBuilder.addMessageObserver(observer);
		return this;
	}

	@Override
	public RestExpressService addPreprocessor(final Preprocessor preprocessor) {
		requestHandlerBuilder.addPreprocessor(preprocessor);
		return this;
	}

	@Override
	public RestExpressService addPostprocessor(final Postprocessor processor) {
		requestHandlerBuilder.addPostprocessor(processor);
		return this;
	}

	@Override
	public RestExpressService addFinallyProcessor(final Postprocessor processor) {
		requestHandlerBuilder.addFinallyProcessor(processor);
		return this;
	}

	@Override
	public RestExpressService add(Processor processor, ResponseWrapper responseWrapper) {
		requestHandlerBuilder.add(processor, responseWrapper);
		return this;
	}

	@Override
	public RestExpressService add(Processor processor, ResponseWrapper responseWrapper, boolean isDefault) {
		requestHandlerBuilder.add(processor, responseWrapper, isDefault);
		return this;
	}

	@Override
	public RestExpressService shouldEnforceHttpSpec() {
		settings.serverSettings().setEnforceHttpSpec(Boolean.TRUE);
		requestHandlerBuilder.shouldEnforceHttpSpec();
		return this;
	}

	@Override
	public RestExpressService shouldNotEnforceHttpSpec() {
		settings.serverSettings().setEnforceHttpSpec(Boolean.FALSE);
		requestHandlerBuilder.shouldNotEnforceHttpSpec();
		return this;
	}

	@Override
	public RestExpressService register(Plugin plugin) {
		pluginManager.register(plugin);
		return this;
	}

	@Override
	public PluginService plugin() {
		return pluginManager;
	}

	@Override
	public RestExpressSettings settings() {
		return settings;
	}

	@Override
	public ServerContext context() {
		return context;
	}

	@Override
	public ParameterizedRouteBuilder uri(final String uriPattern, final Object controller) {
		return routeDeclarations.uri(uriPattern, controller);
	}

	@Override
	public RegexRouteBuilder regex(final String uriPattern, final Object controller) {
		return routeDeclarations.regex(uriPattern, controller);
	}

	@Override
	public ServerMetadata getRouteMetadata() {
		if (routeMapping == null)
			throw new IllegalStateException("Server is not yet binded");
		final ServerMetadata metadata = new ServerMetadata( //
				settings.serverSettings().getName(), //
				settings.serverSettings().getPort(), //
				settings.serverSettings().getBaseUrl(), //
				serializationProvider != null ? serializationProvider.supportedMediaType() : null, //
				serializationProvider != null ? serializationProvider.defaultProcessor().mediaType() : null,//
				routeMapping.routeMetadata());
		return metadata;
	}

	@Override
	public RouteMapping routeMapping() {
		if (routeMapping == null) {
			routeMapping = new RouteMapping(settings.serverSettings().getBaseUrl());
		}
		return routeMapping;
	}

	@Override
	public Map<String, String> getRouteUrlsByName() {
		if (routeMapping == null) {
			routeDeclarations.createRouteMapping(this);
		}
		return routeMapping().getRouteUrlsByName(settings.serverSettings().getBaseUrl());
	}

	@Override
	public RestExpress route(Object controller) throws ConfigurationException {
		JaxRsReader.register(this, controller);
		return this;
	}

	@Override
	public ParamConverterProvider paramConverterProvider() {
		if (paramConverterProvider == null) {
			paramConverterProvider = new RestExpressParamConverterProvider();
		}
		return paramConverterProvider;
	}

	/**
	 * Load an SSL Context.
	 * 
	 * @param keyStore
	 * @param filePassword
	 * @param keyPassword
	 * @return {@link SSLContext}.
	 * @throws Exception
	 */
	public static SSLContext loadContext(final String keyStore, final String filePassword, final String keyPassword) throws Exception {
		FileInputStream fin = null;

		try {
			final KeyStore ks = KeyStore.getInstance("JKS");
			fin = new FileInputStream(keyStore);
			ks.load(fin, filePassword.toCharArray());

			final KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, keyPassword.toCharArray());

			final SSLContext context = SSLContext.getInstance("TLS");
			context.init(kmf.getKeyManagers(), null, null);
			return context;
		} finally {
			if (null != fin) {
				try {
					fin.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	/**
	 * @return {@link SerializationProvider}.
	 */
	@VisibleForTesting
	protected final SerializationProvider serializationProvider() {
		return serializationProvider;
	}

	/**
	 * @return {@link RestExpressRequestHandlerBuilder.
	 */
	@VisibleForTesting
	protected final RestExpressRequestHandlerBuilder requestHandlerBuilder() {
		return requestHandlerBuilder;
	}

	/**
	 * Build up a server with NIO channels and default cached thread pools.
	 * 
	 * @author kevwil
	 * 
	 * @since October 1, 2010
	 * 
	 * @see ServerBootstrap
	 * @see NioServerSocketChannelFactory
	 * @see Executors
	 * @return An {@link ServerBootstrap} instance.
	 */
	private final static ServerBootstrap createServerNioBootstrap() {
		return new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
	}

	/**
	 * Build up a server with NIO channels and default cached thread pools,
	 * specifying the number of worker threads.
	 * 
	 * @author kevwil
	 * 
	 * @since October 1, 2010
	 * 
	 * @param workerCount
	 *            the number of worker threads desired.
	 * @see ServerBootstrap
	 * @see NioServerSocketChannelFactory
	 * @see Executors
	 * @return An {@link ServerBootstrap} instance.
	 */
	private final static ServerBootstrap createServerNioBootstrap(final int workerCount) {
		return new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(), workerCount));
	}

}
