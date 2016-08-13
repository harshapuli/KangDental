package com.dentist.util;

import java.io.IOException;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class CustomJodaLocalDateSerializer extends JsonSerializer<LocalDate> {

	DateTimeFormatter format = DateTimeFormat.forPattern("MM-dd-yyyy");

	@Override
	public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {

		if (value == null) {
			gen.writeNull();
		} else {
			gen.writeString(format.print(value));
		}

	}

}
