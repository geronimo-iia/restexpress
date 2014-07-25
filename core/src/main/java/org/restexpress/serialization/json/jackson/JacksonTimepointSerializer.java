package org.restexpress.serialization.json.jackson;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.TimestampAdapter;

public class JacksonTimepointSerializer extends JsonSerializer<Date> {
	private final DateAdapter adapter;

	public JacksonTimepointSerializer() {
		this(new TimestampAdapter());
	}

	public JacksonTimepointSerializer(final DateAdapter adapter) {
		super();
		this.adapter = adapter;
	}

	@Override
	public void serialize(final Date date, final JsonGenerator gen, final SerializerProvider sp) throws IOException, JsonProcessingException {
		gen.writeString(adapter.format(date));
	}
}