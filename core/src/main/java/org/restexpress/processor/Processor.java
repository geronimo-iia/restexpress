package org.restexpress.processor;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;

/**
 * Processor define methods to manage:
 * <ul>
 * <li>supported media type</li>
 * <li>serialization into {@link ChannelBuffer}</li>
 * <li>deserialization from {@link ChannelBuffer}</li>
 * </ul>
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public interface Processor {

	/**
	 * @return a non empty {@link List} of supported media type.
	 */
	public List<String> supportedMediaTypes();

	/**
	 * @return the principal media type.
	 */
	public String mediaType();

	/**
	 * Write value into specified buffer.
	 * 
	 * @param value
	 * @param buffer
	 * @throws SerializationException
	 *             if error occurs
	 */
	public void write(Object value, ChannelBuffer buffer) throws SerializationException;

	/**
	 * Read object of specified type from buffer.
	 * 
	 * @param buffer
	 *            {@link ChannelBuffer} source
	 * @param valueType
	 *            expected type
	 * @return Object instance
	 * @throws DeserializationException
	 *             if error occurs
	 */
	public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException;
}
