package com.plexobject.dp.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.plexobject.dp.provider.DataProvider;

public class DataProviderSerializer extends StdSerializer<DataProvider> {
    public DataProviderSerializer(Class<DataProvider> t) {
        super(t);
    }

    @Override
    public void serialize(DataProvider provider, JsonGenerator jgen,
            SerializerProvider sp) throws IOException, JsonGenerationException {
        jgen.writeStringField("providerName", provider.getName());
    }
}
