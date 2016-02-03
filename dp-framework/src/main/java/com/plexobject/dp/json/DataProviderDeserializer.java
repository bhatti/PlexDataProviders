package com.plexobject.dp.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.plexobject.dp.locator.DataProviderLocator;
import com.plexobject.dp.provider.DataProvider;

public class DataProviderDeserializer extends JsonDeserializer<DataProvider> {
    private final DataProviderLocator dataProviderLocator;
    public DataProviderDeserializer(DataProviderLocator dataProviderLocator) {
        this.dataProviderLocator = dataProviderLocator;
    }

    @Override
    public DataProvider deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        String name = node.get("providerName").asText();
        return dataProviderLocator.getByName(name);
   }
}