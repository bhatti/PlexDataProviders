package com.plexobject.dp.json;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.Metadata;

public class DataRowSetDeserializer extends JsonDeserializer<DataRowSet> {
    @Override
    public DataRowSet deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        return DataRowSetDeserializer.doDeserialize(jp, ctxt, node);
    }

    static DataRowSet doDeserialize(JsonParser jp, DeserializationContext ctxt,
            JsonNode node) throws IOException, JsonProcessingException {
        DataRowSet rowset = new DataRowSet(Metadata.from());
        Iterator<JsonNode> it = node.elements();

        while (it.hasNext()) {
            JsonNode next = it.next();
            DataRow row = DataRowDeserializer.doDeserialize(jp, ctxt, next);
            rowset.addRow(row);
        }
        return rowset;
    }
}