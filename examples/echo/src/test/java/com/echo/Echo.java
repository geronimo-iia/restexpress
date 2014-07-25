package com.echo;

import org.restexpress.RestExpressLauncher;

public class Echo {

	public static void main(String[] args) {
		RestExpressLauncher launcher = new RestExpressLauncher();
		launcher.bind();
		launcher.awaitShutdown();
	}

}
