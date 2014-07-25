package org.restexpress.serialization.json.jackson;

import java.io.IOException;

import org.owasp.encoder.Encode;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JacksonEncodingStringSerializer extends JsonSerializer<String> {

	@Override
	public void serialize(final String value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(Encode.forXmlContent(value));
	}
}