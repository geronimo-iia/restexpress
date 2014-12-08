package org.restexpress;

import org.jboss.netty.channel.Channel;
import org.restexpress.context.ServerContext;
import org.restexpress.domain.metadata.ServerMetadata;
import org.restexpress.settings.RestExpressSettings;

public interface RestExpressService {

	/**
	 * @return {@link RestExpressSettings} instance.
	 */
	public RestExpressSettings settings();

	/**
	 * @return {@link ServerContext} instance.
	 */
	public ServerContext context();

	/**
	 * Retrieve meta data about the routes in this RestExpress server.
	 * 
	 * @return ServerMetadata instance.
	 */
	public ServerMetadata getRouteMetadata();

	/**
	 * The last call in the building of a RestExpress server, bind() causes
	 * Netty to bind to the listening address and process incoming messages.
	 * 
	 * @return Channel
	 */
	public Channel bind();

	/**
	 * The last call in the building of a RestExpress server, bind() causes
	 * Netty to bind to the listening address and process incoming messages.
	 * 
	 * @param port
	 * @return Channel
	 */
	public Channel bind(final int port);

	/**
	 * Releases all resources associated with this server so the JVM can
	 * shutdown cleanly. Call this method to finish using the server. To utilize
	 * the default shutdown hook in main() provided by RestExpress, call
	 * awaitShutdown() instead.
	 */
	public void shutdown();

	/**
	 * Used in main() to install a default JVM shutdown hook and shut down the
	 * server cleanly. Calls shutdown() when JVM termination detected. To
	 * utilize your own shutdown hook(s), install your own shutdown hook(s) and
	 * call shutdown() instead of awaitShutdown().
	 */
	public void awaitShutdown();

}
