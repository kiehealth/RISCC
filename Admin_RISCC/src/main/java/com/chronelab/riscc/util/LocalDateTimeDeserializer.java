package com.chronelab.riscc.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    protected LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");//Can be used if custom format is required.
        //return LocalDate.parse(jsonParser.readValueAs(String.class), DateTimeFormatter.ISO_LOCAL_DATE);//yyyy-MM-dd
        long timestampMillis = jsonParser.readValueAs(Long.class); // Read it as a long value
        Instant instant = Instant.ofEpochMilli(timestampMillis);
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC); //
//        return Instant.ofEpochMilli(jsonParser.readValueAs(Long.class)).atZone(ZoneOffset.UTC).toLocalDateTime();
    }
}
