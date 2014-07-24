package org.restexpress;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.restexpress.domain.CharacterSet;
import org.restexpress.pipeline.DefaultHttpResponseWriter;
import org.restexpress.pipeline.DefaultRequestHandler;
import org.restexpress.pipeline.HttpResponseWriter;
import org.restexpress.processor.Processor;
import org.restexpress.response.ResponseProcessorManager;
import org.restexpress.route.RouteDeclaration;
import org.restexpress.route.RouteResolver;

/**
 * {@link TestUtilities} give utilities method for testing purpose.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public enum TestUtilities {
	;

	public static Request newRequest(HttpRequest httpRequest) {
		return new Request(httpRequest, null);
	}

	public static String serialize(Object value, Processor processor) {
		ChannelBuffer channelBuffer = ChannelBuffers.dynamicBuffer();
		processor.write(value, channelBuffer);
		return channelBuffer.toString(CharacterSet.UTF_8.getCharset());
	}

	public static <T> T deserialize(String buffer, Class<T> valueType, Processor processor) {
		return buffer == null ? null : processor.read(ChannelBuffers.copiedBuffer(buffer, CharacterSet.UTF_8.getCharset()), valueType);
	}

	public static <T> T deserialize(ChannelBuffer buffer, Class<T> valueType, Processor processor) {
		return processor.read(buffer, valueType);
	}

	public static SerializationProvider newSerializationProvider() {
		return new ResponseProcessorManager();
	}

	public static DefaultRequestHandler newDefaultRequestHandler(RouteDeclaration routeDeclaration) {
		return new DefaultRequestHandler(new RouteResolver(routeDeclaration.createRouteMapping()), new ResponseProcessorManager(), new DefaultHttpResponseWriter(), false);
	}

	public static DefaultRequestHandler newDefaultRequestHandler(RouteDeclaration routeDeclaration, HttpResponseWriter httpResponseWriter) {
		return new DefaultRequestHandler(new RouteResolver(routeDeclaration.createRouteMapping()), new ResponseProcessorManager(), httpResponseWriter, false);
	}
}
