package com.kms.algotrade.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.kms.algotrade.enums.EnumInterface;

import java.io.IOException;

public class EnumSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, IOException {
        gen.writeString(((EnumInterface)value).getType());
    }
}
