package org.restexpress.serialization.json.jackson;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.restexpress.exception.DeserializationException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.Iso8601TimepointAdapter;

public class JacksonTimepointDeserializer extends JsonDeserializer<Date> {
	private final DateAdapter adapter;

	public JacksonTimepointDeserializer() {
		this(new Iso8601TimepointAdapter());
	}

	public JacksonTimepointDeserializer(final DateAdapter adapter) {
		super();
		this.adapter = adapter;
	}

	@Override
	public Date deserialize(final JsonParser parser, final DeserializationContext context) throws IOException, JsonProcessingException {
		try {
			return adapter.parse(parser.getText());
		} catch (final ParseException e) {
			throw new DeserializationException(e);
		}
	}
}