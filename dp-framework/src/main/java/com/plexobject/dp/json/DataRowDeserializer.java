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
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;

public class DataRowDeserializer extends JsonDeserializer<DataRow> {
    private static final Logger logger = Logger
            .getLogger(DataRowDeserializer.class);

    @Override
    public DataRow deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return doDeserialize(jp, ctxt, node);
    }

    static DataRow doDeserialize(JsonParser jp, DeserializationContext ctxt,
            JsonNode node) throws IOException, JsonProcessingException {
        DataRow row = new DataRow();
        //

        Iterator<JsonNode> it = node.elements();
        while (it.hasNext()) {
            JsonNode next = it.next();
            Iterator<String> namesIt = next.fieldNames();
            while (namesIt.hasNext()) {
                String name = namesIt.next();
                MetaField field = MetaFieldFactory.lookup(name);
                if (field != null) {
                    switch (field.getType()) {
                    case SCALAR_TEXT:
                        row.addField(field, next.get(name).asText());
                        break;
                    case SCALAR_INTEGER:
                        row.addField(field, next.get(name).asLong());
                        break;
                    case SCALAR_DECIMAL:
                        row.addField(field, next.get(name).asDouble());
                        break;
                    case SCALAR_DATE:
                        row.addField(field,
                                new Date(next.get(name).asLong()));
                        break;
                    case SCALAR_BOOLEAN:
                        row.addField(field, next.get(name).asBoolean());
                        break;
                    case VECTOR_TEXT: {
                        Iterator<JsonNode> iit = next.get(name).elements();
                        List<String> values = new ArrayList<>();
                        while (iit.hasNext()) {
                            JsonNode inext = iit.next();
                            values.add(inext.asText());
                        }
                        row.addField(field,
                                values.toArray(new String[values.size()]));
                    }
                        break;
                    case VECTOR_INTEGER: {
                        Iterator<JsonNode> iit = next.get(name).elements();
                        List<Long> values = new ArrayList<>();
                        while (iit.hasNext()) {
                            JsonNode inext = iit.next();
                            values.add(inext.asLong());
                        }
                        row.addField(field,
                                values.toArray(new Long[values.size()]));
                    }
                        break;
                    case VECTOR_DECIMAL: {
                        Iterator<JsonNode> iit = next.get(name).elements();
                        List<Double> values = new ArrayList<>();
                        while (iit.hasNext()) {
                            JsonNode inext = iit.next();
                            values.add(inext.asDouble());
                        }
                        row.addField(field,
                                values.toArray(new Double[values.size()]));
                    }
                        break;
                    case VECTOR_DATE: {
                        Iterator<JsonNode> iit = next.get(name).elements();
                        List<Date> values = new ArrayList<>();
                        while (iit.hasNext()) {
                            JsonNode inext = iit.next();
                            values.add(new Date(inext.asLong()));
                        }
                        row.addField(field,
                                values.toArray(new Date[values.size()]));

                    }
                        break;
                    case VECTOR_BOOLEAN: {
                        Iterator<JsonNode> iit = next.get(name).elements();
                        List<Boolean> values = new ArrayList<>();
                        while (iit.hasNext()) {
                            JsonNode inext = iit.next();
                            values.add(inext.asBoolean());
                        }
                        row.addField(field,
                                values.toArray(new Boolean[values.size()]));

                    }
                        break;
                    case BINARY:
                        row.addField(field, next.get(name).asText()
                                .getBytes("UTF-8"));
                        break;
                    case ROWSET: {
                        DataRowSet rowset = DataRowSetDeserializer
                                .doDeserialize(jp, ctxt, next.get(name));
                        row.addField(field, rowset);
                        break;
                    }
                    default:
                        logger.error("Failed to deserialize object " + next);
                        break;
                    }
                } else {
                    logger.error("Could not find meta field for " + field
                            + ": " + next);
                }
            }
        }
        return row;
    }
}