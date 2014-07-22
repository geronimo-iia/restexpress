package org.restexpress.plugin.route;

import java.io.IOException;

import org.restexpress.RestExpressLauncher;
import org.restexpress.pipeline.observer.SimpleConsoleLogMessageObserver;

/**
 * RoutePluginDebug is just for see page and ... 
 *
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 *
 */
public class RoutePluginDebug {
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		RestExpressLauncher launcher = new RestExpressLauncher();
		launcher.server().registerPlugin(new RouteMetadataPlugin());
		launcher.server().addMessageObserver(new SimpleConsoleLogMessageObserver());
		launcher.bind();
	}
}
