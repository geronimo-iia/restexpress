package org.restexpress.pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.local.DefaultLocalServerChannelFactory;
import org.restexpress.TestUtilities;
import org.restexpress.route.RouteDeclaration;

public class AbstractWrapperResponse {

	protected DefaultRequestHandler messageHandler;
	protected WrappedResponseObserver observer;
	protected Channel channel;
	protected ChannelPipeline pl;
	protected StringBuffer responseBody;
	protected Map<String, List<String>> responseHeaders;

	protected void initialize(RouteDeclaration routes) throws Exception {

		responseBody = new StringBuffer();
		responseHeaders = new HashMap<String, List<String>>();

		messageHandler = TestUtilities.newDefaultRequestHandler(routes, new StringBufferHttpResponseWriter(responseHeaders, responseBody));

		observer = new WrappedResponseObserver();
		messageHandler.addMessageObserver(observer);

		PipelineBuilder pf = new PipelineBuilder().addRequestHandler(messageHandler);
		pl = pf.getPipeline();
		ChannelFactory channelFactory = new DefaultLocalServerChannelFactory();
		channel = channelFactory.newChannel(pl);
	}

}
