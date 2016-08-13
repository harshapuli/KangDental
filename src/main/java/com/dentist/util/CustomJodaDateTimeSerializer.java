package com.dentist.util;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CustomJodaDateTimeSerializer extends JsonSerializer<DateTime> {

	DateTimeFormatter format = DateTimeFormat.forPattern("d  MMMM,  yyyy  hh:mm:ss  a");

	@Override
	public void serialize(DateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {

		if (value == null) {
			gen.writeNull();
		} else {
			gen.writeString(format.print(value));
		}

	}

}
