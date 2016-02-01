package com.plexobject.dp.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;

public class DataRowDeserializer extends JsonDeserializer<DataRow> {
    private static final Logger logger = Logger
            .getLogger(DataRowDeserializer.class);

    @Override
    public DataRow deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        DataRow row = new DataRow();
        Iterator<JsonNode> it = node.elements();
        while (it.hasNext()) {
            JsonNode next = it.next();
            String name = next.get("name").asText();
            MetaField field = MetaFieldFactory.lookup(name);
            if (field != null) {
                switch (field.getType()) {
                case SCALAR_TEXT:
                    row.addField(field, next.get("value").asText());
                    break;
                case SCALAR_INTEGER:
                    row.addField(field, next.get("value").asLong());
                    break;
                case SCALAR_DECIMAL:
                    row.addField(field, next.get("value").asDouble());
                    break;
                case SCALAR_DATE:
                    row.addField(field, new Date(next.get("value").asLong()));
                    break;
                case SCALAR_BOOLEAN:
                    row.addField(field, next.get("value").asBoolean());
                    break;
                case VECTOR_TEXT: {
                    Iterator<JsonNode> iit = next.get("value").elements();
                    List<String> values = new ArrayList<>();
                    int i = 0;
                    while (iit.hasNext()) {
                        JsonNode inext = iit.next();
                        values.add(inext.get(i++).asText());
                    }
                    row.addField(field,
                            values.toArray(new String[values.size()]));
                }
                    break;
                case VECTOR_INTEGER: {
                    Iterator<JsonNode> iit = next.get("value").elements();
                    List<Long> values = new ArrayList<>();
                    int i = 0;
                    while (iit.hasNext()) {
                        JsonNode inext = iit.next();
                        values.add(inext.get(i++).asLong());
                    }
                    row.addField(field, values.toArray(new Long[values.size()]));
                }
                    break;
                case VECTOR_DECIMAL: {
                    Iterator<JsonNode> iit = next.get("value").elements();
                    List<Double> values = new ArrayList<>();
                    int i = 0;
                    while (iit.hasNext()) {
                        JsonNode inext = iit.next();
                        values.add(inext.get(i++).asDouble());
                    }
                    row.addField(field,
                            values.toArray(new Double[values.size()]));
                }
                    break;
                case VECTOR_DATE: {
                    Iterator<JsonNode> iit = next.get("value").elements();
                    List<Date> values = new ArrayList<>();
                    int i = 0;
                    while (iit.hasNext()) {
                        JsonNode inext = iit.next();
                        values.add(new Date(inext.get(i++).asLong()));
                    }
                    row.addField(field, values.toArray(new Date[values.size()]));

                }
                    break;
                case VECTOR_BOOLEAN: {
                    Iterator<JsonNode> iit = next.get("value").elements();
                    List<Boolean> values = new ArrayList<>();
                    int i = 0;
                    while (iit.hasNext()) {
                        JsonNode inext = iit.next();
                        values.add(inext.get(i++).asBoolean());
                    }
                    row.addField(field,
                            values.toArray(new Boolean[values.size()]));

                }
                    break;
                case BINARY:
                    row.addField(field,
                            next.get("value").asText().getBytes("UTF-8"));
                default:
                    logger.error("Failed to deserialize object " + next);
                    break;
                }
            }
        }
        return row;
    }
}