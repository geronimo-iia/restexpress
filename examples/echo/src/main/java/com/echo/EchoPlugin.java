package com.echo;

import org.restexpress.RestExpress;
import org.restexpress.plugin.AbstractPlugin;

public class EchoPlugin extends AbstractPlugin {

	private EchoController controller;

	public EchoPlugin() {
		super();
	}

	@Override
	public AbstractPlugin register(RestExpress server) {
		super.register(server);
		controller = new EchoController();
		server.uri("/", controller).name("echo.routes");
		return this;
	}

	@Override
	public void shutdown(RestExpress server) {
		super.shutdown(server);
		controller = null;
	}
}
