package com.plexobject.dp.json;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;

public class MetadataDeserializer extends JsonDeserializer<Metadata> {
    @Override
    public Metadata deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        Metadata metadata = Metadata.from();
        Iterator<JsonNode> it = node.elements();
        while (it.hasNext()) {
            JsonNode next = it.next();
            String name = next.get("name").asText();
            String type = next.get("type").asText();
            String kind = next.get("kind").asText();
            boolean keyField = next.get("keyField").asBoolean();
            MetaField field = MetaFieldFactory.create(name, kind,
                    MetaFieldType.valueOf(type), keyField);
            metadata.addMetaField(field);
        }
        return metadata;
    }
}
