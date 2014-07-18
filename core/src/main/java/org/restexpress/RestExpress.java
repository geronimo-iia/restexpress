/*
 * Copyright 2009-2012, Strategic Gains, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.restexpress;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.restexpress.domain.metadata.RouteMetadata;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.pipeline.DefaultRequestHandler;
import org.restexpress.pipeline.MessageObserver;
import org.restexpress.pipeline.PipelineBuilder;
import org.restexpress.pipeline.Postprocessor;
import org.restexpress.pipeline.Preprocessor;
import org.restexpress.plugin.Plugin;
import org.restexpress.response.DefaultHttpResponseWriter;
import org.restexpress.route.RouteBuilder;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.route.RouteResolver;
import org.restexpress.route.parameterized.ParameterizedRouteBuilder;
import org.restexpress.route.regex.RegexRouteBuilder;
import org.restexpress.serialization.DefaultSerializationProvider;
import org.restexpress.serialization.SerializationProvider;
import org.restexpress.settings.RestExpressSettings;
import org.restexpress.settings.Settings;
import org.restexpress.util.Bootstraps;
import org.restexpress.util.Callback;
import org.restexpress.util.DefaultShutdownHook;

/**
 * Primary entry point to create a RestExpress service. All that's required is a
 * RouteDeclaration. By default: port is 8081, serialization format is JSON,
 * supported formats are JSON and XML.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * @author toddf
 */
public class RestExpress {

	private static final ChannelGroup allChannels = new DefaultChannelGroup("RestExpress");

	/**
	 * {@link RestExpressSettings} instance.
	 */
	private final RestExpressSettings settings;

	private SerializationProvider serializationProvider = null;

	private ServerBootstrap bootstrap;

	private final List<MessageObserver> messageObservers;
	private final List<Preprocessor> preprocessors;
	private final List<Postprocessor> postprocessors;
	private final List<Postprocessor> finallyProcessors;
	private final List<Plugin> plugins;

	private final RouteDeclaration routeDeclarations;

	private SSLContext sslContext = null;

	/**
	 * Create a new RestExpress service. By default, RestExpress uses port 8081.
	 * Supports JSON, and XML, providing JSEND-style wrapped responses. And
	 * displays some messages on System.out. These can be altered with the with
	 * settings().
	 * 
	 * <p/>
	 * The default input and output format for messages is JSON. To change that,
	 * use the setDefaultFormat(String) DSL modifier, passing the format to use
	 * by default. Make sure there's a corresponding SerializationProcessor for
	 * that particular format. The Format class has the basics.
	 * 
	 * <p/>
	 * This DSL was created as a thin veneer on Netty functionality. The bind()
	 * method simply builds a Netty pipeline and uses this builder class to
	 * create it. Underneath the covers, RestExpress uses Google GSON for JSON
	 * handling and XStream for XML processing. However, both of those can be
	 * swapped out using the putSerializationProcessor(String,
	 * SerializationProcessor) method, creating your own instance of
	 * SerializationProcessor as necessary.
	 * 
	 */
	public RestExpress() {
		this(Settings.defaultRestExpressSettings());
	}

	/**
	 * Build a new instance of {@link RestExpress}.
	 * 
	 * @param settings
	 */
	public RestExpress(RestExpressSettings settings) {
		this(settings, new DefaultSerializationProvider(settings.serverSettings().isUseDefaultSerializationConfiguration()));
	}

	/**
	 * Build a new instance of {@link RestExpress}.
	 * 
	 * @param settings
	 *            {@link RestExpressSettings} to use.
	 * @param serializationProvider
	 *            {@link SerializationProvider} to use.
	 */
	public RestExpress(RestExpressSettings settings, SerializationProvider serializationProvider) {
		super();
		if (settings == null)
			throw new NullPointerException("RestExpressSettings can not be null");
		if (serializationProvider == null)
			throw new NullPointerException("SerializationProvider can not be null");
		this.settings = settings;
		this.serializationProvider = serializationProvider;
		messageObservers = new ArrayList<MessageObserver>();
		preprocessors = new ArrayList<Preprocessor>();
		postprocessors = new ArrayList<Postprocessor>();
		finallyProcessors = new ArrayList<Postprocessor>();
		plugins = new ArrayList<Plugin>();
		routeDeclarations = new RouteDeclaration();
	}

	/**
	 * @return {@link RestExpressSettings} instance.
	 */
	public RestExpressSettings settings() {
		return settings;
	}

	/**
	 * Change the default behavior for serialization. If no
	 * SerializationProcessor is set, default of DefaultSerializationProcessor
	 * is used, which uses Jackson for JSON, XStream for XML.
	 * 
	 * @param provider
	 *            a SerializationProvider instance.
	 */
	public RestExpress setSerializationProvider(final SerializationProvider provider) {
		serializationProvider = provider;
		return this;
	}

	/**
	 * @return {@link SerializationProvider} instance.
	 */
	public SerializationProvider getSerializationProvider() {
		return serializationProvider;
	}

	public RestExpress setSSLContext(final SSLContext sslContext) {
		this.sslContext = sslContext;
		return this;
	}

	public SSLContext getSSLContext() {
		return sslContext;
	}

	public RestExpress addMessageObserver(final MessageObserver observer) {
		if (!messageObservers.contains(observer)) {
			messageObservers.add(observer);
		}
		return this;
	}

	public List<MessageObserver> getMessageObservers() {
		return Collections.unmodifiableList(messageObservers);
	}

	/**
	 * Add a Preprocessor instance that gets called before an incoming message
	 * gets processed. Preprocessors get called in the order in which they are
	 * added. To break out of the chain, simply throw an exception.
	 * 
	 * @param processor
	 * @return
	 */
	public RestExpress addPreprocessor(final Preprocessor processor) {
		if (!preprocessors.contains(processor)) {
			preprocessors.add(processor);
		}
		return this;
	}

	public List<Preprocessor> getPreprocessors() {
		return Collections.unmodifiableList(preprocessors);
	}

	/**
	 * Add a Postprocessor instance that gets called after an incoming message
	 * is processed. A Postprocessor is useful for augmenting or transforming
	 * the results of a controller or adding headers, etc. Postprocessors get
	 * called in the order in which they are added. Note however, they do NOT
	 * get called in the case of an exception or error within the route.
	 * 
	 * @param processor
	 * @return
	 */
	public RestExpress addPostprocessor(final Postprocessor processor) {
		if (!postprocessors.contains(processor)) {
			postprocessors.add(processor);
		}
		return this;
	}

	public List<Postprocessor> getPostprocessors() {
		return Collections.unmodifiableList(postprocessors);
	}

	/**
	 * Add a Postprocessor instance that gets called right before the serialized
	 * message is sent to the client, or in a finally block after the message is
	 * processed, if an error occurs. Finally processors are Postprocessor
	 * instances that are guaranteed to run even if an error is thrown from the
	 * controller or somewhere else in the route. A Finally Processor is useful
	 * for adding headers or transforming results even during error conditions.
	 * Finally processors get called in the order in which they are added.
	 * 
	 * If an exception is thrown during finally processor execution, the finally
	 * processors following it are executed after printing a stack trace to the
	 * System.err stream.
	 * 
	 * @param processor
	 * @return RestExpress for method chaining.
	 */
	public RestExpress addFinallyProcessor(final Postprocessor processor) {
		if (!finallyProcessors.contains(processor)) {
			finallyProcessors.add(processor);
		}
		return this;
	}

	public List<Postprocessor> getFinallyProcessors() {
		return Collections.unmodifiableList(finallyProcessors);
	}

	/**
	 * Can be called after routes are defined to augment or get data from all
	 * the currently-defined routes.
	 * 
	 * @param callback
	 *            a Callback implementor.
	 */
	public void iterateRouteBuilders(final Callback<RouteBuilder> callback) {
		routeDeclarations.iterateRouteBuilders(callback);
	}

	/**
	 * Build a default request handler. Used instead of bind() so it may be used
	 * injected into any existing Netty pipeline.
	 * 
	 * @return ChannelHandler
	 */
	public ChannelHandler buildRequestHandler() {
		// Set up the event pipeline factory.
		final DefaultRequestHandler requestHandler = new DefaultRequestHandler(new RouteResolver(routeDeclarations.createRouteMapping()), //
				getSerializationProvider(), //
				new DefaultHttpResponseWriter(), settings.serverSettings().isEnforceHttpSpec());

		// Add MessageObservers to the request handler here, if desired...
		requestHandler.addMessageObserver(messageObservers.toArray(new MessageObserver[0]));

		// Add pre/post processors to the request handler here...
		addPreprocessors(requestHandler);
		addPostprocessors(requestHandler);
		addFinallyProcessors(requestHandler);

		return requestHandler;
	}

	/**
	 * The last call in the building of a RestExpress server, bind() causes
	 * Netty to bind to the listening address and process incoming messages.
	 * 
	 * @return Channel
	 */
	public Channel bind() {
		return bind(settings.serverSettings().getPort());
	}

	/**
	 * The last call in the building of a RestExpress server, bind() causes
	 * Netty to bind to the listening address and process incoming messages.
	 * 
	 * @param port
	 * @return Channel
	 */
	public Channel bind(final int port) {
		settings.serverSettings().setPort(port);

		// Configure the server.
		if (settings.serverSettings().getIoThreadCount() == 0) {
			bootstrap = Bootstraps.createServerNioBootstrap();
		} else {
			bootstrap = Bootstraps.createServerNioBootstrap(settings.serverSettings().getIoThreadCount());
		}

		final ChannelHandler requestHandler = buildRequestHandler();

		final PipelineBuilder pf = new PipelineBuilder().addRequestHandler(requestHandler).setSSLContext(sslContext).setMaxContentLength(settings.serverSettings().getMaxContentSize());

		if (settings.serverSettings().getExecutorThreadPoolSize() > 0) {
			final ExecutionHandler executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(settings.serverSettings().getExecutorThreadPoolSize(), 0, 0));
			pf.setExecutionHandler(executionHandler);
		}

		bootstrap.setPipelineFactory(pf);
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
		allChannels.add(channel);
		bindPlugins();
		return channel;
	}

	/**
	 * Used in main() to install a default JVM shutdown hook and shut down the
	 * server cleanly. Calls shutdown() when JVM termination detected. To
	 * utilize your own shutdown hook(s), install your own shutdown hook(s) and
	 * call shutdown() instead of awaitShutdown().
	 */
	public void awaitShutdown() {
		Runtime.getRuntime().addShutdownHook(new DefaultShutdownHook(this));
		boolean interrupted = false;

		do {
			try {
				Thread.sleep(300);
			} catch (final InterruptedException e) {
				interrupted = true;
			}
		} while (!interrupted);
	}

	/**
	 * Releases all resources associated with this server so the JVM can
	 * shutdown cleanly. Call this method to finish using the server. To utilize
	 * the default shutdown hook in main() provided by RestExpress, call
	 * awaitShutdown() instead.
	 */
	public void shutdown() {
		final ChannelGroupFuture future = allChannels.close();
		future.awaitUninterruptibly();
		shutdownPlugins();
		bootstrap.getFactory().releaseExternalResources();
	}

	/**
	 * Retrieve metadata about the routes in this RestExpress server.
	 * 
	 * @return ServerMetadata instance.
	 */
	public ServerMetadata getRouteMetadata() {
		final ServerMetadata m = new ServerMetadata();
		m.setName(settings.serverSettings().getName());
		m.setPort(settings.serverSettings().getPort());
		// TODO: create a good substitute for this...
		// m.setDefaultFormat(getDefaultFormat());
		// m.addAllSupportedFormats(getResponseProcessors().keySet());
		m.addAllRoutes(routeDeclarations.getMetadata());
		return m;
	}

	/**
	 * Retrieve the named routes in this RestExpress server, creating a Map of
	 * them by name, with the value portion being populated with the URL
	 * pattern. Any '.{format}' portion of the URL pattern is omitted.
	 * <p/>
	 * If the Base URL is set, it is included in the URL pattern.
	 * <p/>
	 * Only named routes are included in the output.
	 * 
	 * @return a Map of Route Name/URL pairs.
	 */
	public Map<String, String> getRouteUrlsByName() {
		final Map<String, String> urlsByName = new HashMap<String, String>();

		iterateRouteBuilders(new Callback<RouteBuilder>() {
			@Override
			public void process(final RouteBuilder routeBuilder) {
				final RouteMetadata route = routeBuilder.asMetadata();

				if (route.getName() != null) {
					urlsByName.put(route.getName(), settings.serverSettings().getBaseUrl() + route.getUri().getPattern().replace(".{format}", ""));
				}
			}
		});

		return urlsByName;
	}

	public RestExpress registerPlugin(final Plugin plugin) {
		if (!plugins.contains(plugin)) {
			plugins.add(plugin);
			plugin.register(this);
		}

		return this;
	}

	private void bindPlugins() {
		for (final Plugin plugin : plugins) {
			plugin.bind(this);
		}
	}

	private void shutdownPlugins() {
		for (final Plugin plugin : plugins) {
			plugin.shutdown(this);
		}
	}

	/**
	 * @param requestHandler
	 */
	private void addPreprocessors(final DefaultRequestHandler requestHandler) {
		for (final Preprocessor processor : getPreprocessors()) {
			requestHandler.addPreprocessor(processor);
		}
	}

	/**
	 * @param requestHandler
	 */
	private void addPostprocessors(final DefaultRequestHandler requestHandler) {
		for (final Postprocessor processor : getPostprocessors()) {
			requestHandler.addPostprocessor(processor);
		}
	}

	/**
	 * @param requestHandler
	 */
	private void addFinallyProcessors(final DefaultRequestHandler requestHandler) {
		for (final Postprocessor processor : getFinallyProcessors()) {
			requestHandler.addFinallyProcessor(processor);
		}
	}

	/**
	 * Create a route.
	 * 
	 * @param uriPattern
	 * @param controller
	 * @return {@link ParameterizedRouteBuilder}.
	 */
	public ParameterizedRouteBuilder uri(final String uriPattern, final Object controller) {
		return routeDeclarations.uri(uriPattern, controller);
	}

	/**
	 * Create a route.
	 * 
	 * @param uriPattern
	 * @param controller
	 * @return {@link RegexRouteBuilder}.
	 */
	public RegexRouteBuilder regex(final String uriPattern, final Object controller) {
		return routeDeclarations.regex(uriPattern, controller);
	}
}
