package com.plexobject.dp.json;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;

public class DataRowSerializer extends StdSerializer<DataRow> {
    private static final Logger logger = Logger
            .getLogger(DataRowSerializer.class);

    public DataRowSerializer(Class<DataRow> t) {
        super(t);
    }

    @Override
    public void serialize(DataRow row, JsonGenerator jgen, SerializerProvider sp)
            throws IOException, JsonGenerationException {
        DataRowSerializer.doSerialize(row, jgen, sp);
    }

    static void doSerialize(DataRow row, JsonGenerator jgen,
            SerializerProvider sp) throws IOException, JsonGenerationException {
        jgen.writeStartArray(row.size());
        for (Map.Entry<MetaField, Object> e : row.getFields().entrySet()) {
            if (!row.hasFieldValue(e.getKey())) {
                continue;
            }
            jgen.writeStartObject();
            // jgen.writeBooleanField("keyField", e.getKey().isKeyField());
            switch (e.getKey().getType()) {
            case SCALAR_TEXT:
                jgen.writeStringField(e.getKey().getName(),
                        row.getValueAsText(e.getKey()));
                break;
            case SCALAR_INTEGER:
                jgen.writeNumberField(e.getKey().getName(),
                        row.getValueAsLong(e.getKey()));
                break;
            case SCALAR_DECIMAL:
                jgen.writeNumberField(e.getKey().getName(),
                        row.getValueAsDecimal(e.getKey()));
                break;
            case SCALAR_DATE:
                jgen.writeNumberField(e.getKey().getName(),
                        row.getValueAsDate(e.getKey()).getTime());
                break;
            case SCALAR_BOOLEAN:
                jgen.writeBooleanField(e.getKey().getName(),
                        row.getValueAsBoolean(e.getKey()));
                break;
            case VECTOR_TEXT: {
                String[] values = row.getValueAsTextVector(e.getKey());
                jgen.writeArrayFieldStart(e.getKey().getName());
                for (int i = 0; i < values.length; i++) {
                    jgen.writeString(values[i]);
                }
                jgen.writeEndArray();
            }
                break;
            case VECTOR_INTEGER: {
                long[] values = row.getValueAsLongVector(e.getKey());
                jgen.writeArrayFieldStart(e.getKey().getName());
                for (int i = 0; i < values.length; i++) {
                    jgen.writeNumber(values[i]);
                }
                jgen.writeEndArray();
            }
                break;
            case VECTOR_DECIMAL: {
                double[] values = row.getValueAsDecimalVector(e.getKey());
                jgen.writeArrayFieldStart(e.getKey().getName());
                for (int i = 0; i < values.length; i++) {
                    jgen.writeNumber(values[i]);
                }
                jgen.writeEndArray();
            }
                break;
            case VECTOR_DATE: {
                Date[] values = row.getValueAsDateVector(e.getKey());
                jgen.writeArrayFieldStart(e.getKey().getName());
                for (int i = 0; i < values.length; i++) {
                    jgen.writeNumber(values[i].getTime());
                }
                jgen.writeEndArray();

            }
                break;
            case VECTOR_BOOLEAN: {
                boolean[] values = row.getValueAsBooleanVector(e.getKey());
                jgen.writeArrayFieldStart(e.getKey().getName());
                for (int i = 0; i < values.length; i++) {
                    jgen.writeBoolean(values[i]);
                }
                jgen.writeEndArray();
            }
                break;
            case BINARY:
                jgen.writeStringField(e.getKey().getName(),
                        new String(row.getValueAsBinary(e.getKey()), "UTF-8"));
                break;
            case ROWSET:
                jgen.writeArrayFieldStart(e.getKey().getName());
                DataRowSetSerializer.doSerialize((DataRowSet) e.getValue(),
                        jgen, sp);
                jgen.writeEndArray();
                break;
            default:
                logger.error("Failed to serialize object " + e.getKey() + "/"
                        + e.getValue() + " of " + row);
                break;
            }

            //
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
    }
}
