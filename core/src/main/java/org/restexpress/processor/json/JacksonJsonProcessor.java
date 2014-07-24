/**
 * 
 */
package org.restexpress.processor.json;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.restexpress.domain.MediaType;
import org.restexpress.exception.DeserializationException;
import org.restexpress.exception.SerializationException;
import org.restexpress.processor.json.JacksonJsonModule.JacksonEncodingStringSerializer;
import org.restexpress.processor.json.JacksonJsonModule.JacksonTimepointDeserializer;
import org.restexpress.processor.json.JacksonJsonModule.JacksonTimepointSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.strategicgains.util.date.DateAdapterConstants;

/**
 * JacksonJsonProcessor.
 * 
 * @author <a href="mailto:jguibert@intelligents-ia.com" >Jerome Guibert</a>
 * 
 */
public class JacksonJsonProcessor extends JsonProcessor {

	private final ObjectMapper objectMapper;

	/**
	 * Build a new instance.
	 */
	public JacksonJsonProcessor() {
		super();
		this.objectMapper = newObjectMapper();
	}

	public JacksonJsonProcessor(List<String> supportedMediaType) {
		super(supportedMediaType);
		this.objectMapper = newObjectMapper();
	}

	public JacksonJsonProcessor(MediaType... mediaTypes) {
		super(mediaTypes);
		this.objectMapper = newObjectMapper();
	}

	public JacksonJsonProcessor(String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
		this.objectMapper = newObjectMapper();
	}

	public JacksonJsonProcessor(ObjectMapper objectMapper, List<String> mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
		this.objectMapper = objectMapper;
	}

	public JacksonJsonProcessor(ObjectMapper objectMapper, MediaType... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
		this.objectMapper = objectMapper;
	}

	public JacksonJsonProcessor(ObjectMapper objectMapper, String... mediaTypes) throws IllegalArgumentException {
		super(mediaTypes);
		this.objectMapper = objectMapper;
	}

	/**
	 * @see org.restexpress.processor.Processor#write(java.lang.Object,
	 *      org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	public void write(Object value, ChannelBuffer buffer) throws SerializationException {
		try {
			if (value != null) {
				objectMapper.writeValue(getOutputStreamWriter(buffer), value);
			}
		} catch (final JsonProcessingException e) {
			throw new SerializationException(e);
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}

	/**
	 * @see org.restexpress.processor.Processor#read(org.jboss.netty.buffer.ChannelBuffer,
	 *      java.lang.Class)
	 */
	@Override
	public <T> T read(ChannelBuffer buffer, Class<T> valueType) throws DeserializationException {
		try {
			return ((buffer == null) || (buffer.readableBytes() == 0) ? null : objectMapper.readValue(getInputStreamReader(buffer), valueType));
		} catch (final JsonProcessingException e) {
			throw new DeserializationException(e);
		} catch (final IOException e) {
			throw new DeserializationException(e);
		}
	}

	/**
	 * Template method for sub-classes to augment the module with desired
	 * serializers and/or deserializers. Sub-classes should call super() to get
	 * default settings.
	 */
	protected SimpleModule initializeModule() {
		return new SimpleModule().addSerializer(Date.class, new JacksonTimepointSerializer())//
				.addSerializer(String.class, new JacksonEncodingStringSerializer())//
				.addDeserializer(Date.class, new JacksonTimepointDeserializer());
	}

	/**
	 * Template method for sub-classes to augment the mapper with desired
	 * settings. Sub-classes should call super() to get default settings.
	 * 
	 * @param module
	 *            a SimpleModule
	 */
	protected ObjectMapper initializeMapper(final ObjectMapper mapper) {
		return mapper
		// .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				// Ignore additional/unknown properties in a payload.
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				// Only serialize populated properties (do no serialize nulls)
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				// Use fields directly.
				.setVisibility(PropertyAccessor.FIELD, Visibility.ANY)
				// Ignore accessor and mutator methods (use fields per above).
				.setVisibility(PropertyAccessor.GETTER, Visibility.NONE)//
				.setVisibility(PropertyAccessor.SETTER, Visibility.NONE)//
				.setVisibility(PropertyAccessor.IS_GETTER, Visibility.NONE)
				// Set default date output format.
				.setDateFormat(new SimpleDateFormat(DateAdapterConstants.TIME_POINT_OUTPUT_FORMAT));
	}

	protected ObjectMapper newObjectMapper() {
		return initializeMapper(new ObjectMapper()).registerModule(initializeModule());
	}
}
