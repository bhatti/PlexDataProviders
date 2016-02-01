package com.plexobject.dp.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.Metadata;

public class MetadataSerializer extends StdSerializer<Metadata> {
    public MetadataSerializer(Class<Metadata> t) {
        super(t);
    }

    @Override
    public void serialize(Metadata metadata, JsonGenerator jgen,
            SerializerProvider sp) throws IOException, JsonGenerationException {
        jgen.writeStartArray(metadata.size());
        for (MetaField field : metadata.getMetaFields()) {
            jgen.writeStartObject();
            jgen.writeStringField("name", field.getName());
            jgen.writeStringField("type", field.getType().name());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }
}
