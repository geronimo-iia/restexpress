package org.restexpress.serialization.text;

import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.domain.CharacterSet;
import org.restexpress.domain.MediaType;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.serialization.AbstractProcessor;
import org.restexpress.serialization.Processor;

/**
 * {@link TextProcessor} implement a {@link Processor} for all text/* media
 * type.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class TextProcessor extends AbstractProcessor {

	public TextProcessor() {
		super(MediaType.TEXT_ALL);
	}

	public TextProcessor(MediaType... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	public TextProcessor(String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	public TextProcessor(List<String> mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
	}

	@Override
	public void write(Object value, ChannelBuffer buffer) throws SerializationException {
		if (value != null) {
			buffer.writeBytes(value.toString().getBytes(CharacterSet.UTF_8.getCharset()));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException {
		if (!String.class.getName().equals(valueType.getName()))
			throw new DeserializationException("Only String class is supported");
		return (T) buffer.toString(CharacterSet.UTF_8.getCharset());
	}

}
