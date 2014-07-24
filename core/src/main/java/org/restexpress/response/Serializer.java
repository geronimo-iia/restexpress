package org.restexpress.response;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;

/**
 * {@link Serializer} define methods to serialize and deserialize data from/to
 * {@link Request} and {@link Response}.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface Serializer {

	/**
	 * Deserialize specified class type from {@link Request}.
	 * 
	 * @param request
	 *            {@link Request}
	 * @param type
	 *            class type
	 * @return object instance.
	 * @throws DeserializationException
	 *             if an error occurs
	 */
	public <T> T deserialize(final Request request, final Class<T> type) throws DeserializationException;

	/**
	 * Serialize {@link Response}.
	 * 
	 * @param response
	 *            {@link Response} instance.
	 * @return {@link ChannelBuffer} or null if no object was found.
	 * @throws SerializationException
	 *             if an error occurs
	 */
	public ChannelBuffer serialize(final Response response) throws SerializationException;
}
