package com.plexobject.dp.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.DataRowSet;

public class DataRowSetSerializer extends StdSerializer<DataRowSet> {
    public DataRowSetSerializer(Class<DataRowSet> t) {
        super(t);
    }

    @Override
    public void serialize(DataRowSet rowset, JsonGenerator jgen,
            SerializerProvider sp) throws IOException, JsonGenerationException {
        jgen.writeStartArray(rowset.size());
        DataRowSetSerializer.doSerialize(rowset, jgen, sp);
        jgen.writeEndArray();
    }

    static void doSerialize(DataRowSet rowset, JsonGenerator jgen,
            SerializerProvider sp) throws IOException, JsonGenerationException {
        for (DataRow row : rowset.getRows()) {
            DataRowSerializer.doSerialize(row, jgen, sp);
        }
    }
}
