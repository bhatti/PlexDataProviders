package com.plexobject.dp.json;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.provider.DataProvider;

public class DataRowSetJsonTest {
    private Metadata topMetadata = new Metadata();
    private MetaField symbol = MetaFieldFactory.createText("symbol", "Company",
            true);
    private MetaField rowNum = MetaFieldFactory.createInteger("rowNum", "Row",
            false);
    private MetaField updatedAt = MetaFieldFactory.createDate("updatedAt",
            "Company", false);
    private MetaField isOption = MetaFieldFactory.createBoolean("isOption",
            "Company", false);
    private MetaField vectorB = MetaFieldFactory.createVectorBoolean("vectorB",
            "Company", false);
    private MetaField vectorT = MetaFieldFactory.createVectorText("vectorT",
            "Company", false);
    private MetaField vectorI = MetaFieldFactory.createVectorInteger("vectorI",
            "Company", false);
    private MetaField vectorDate = MetaFieldFactory.createVectorDate(
            "vectorDate", "Company", false);
    private MetaField vectorD = MetaFieldFactory.createVectorDecimal("vectorD",
            "Company", false);
    private MetaField binary = MetaFieldFactory.createBinary("binary",
            "Company", false);
    private MetaField company = MetaFieldFactory.createText("company",
            "Company", false);
    private MetaField security = MetaFieldFactory.createRowset("security",
            "Security", false);
    private Metadata securityMetadata = new Metadata();
    private MetaField securityId = MetaFieldFactory.createInteger(
            "security.id", "Security", false);
    private MetaField exchange = MetaFieldFactory.createText(
            "security.exchange", "Security", false);
    private Metadata quoteMetadata = new Metadata();
    private MetaField quote = MetaFieldFactory.createRowset("quote", "Quote",
            false);
    private MetaField bid = MetaFieldFactory.createDecimal("quote.bid",
            "Quote", false);
    private MetaField ask = MetaFieldFactory.createDecimal("quote.ask",
            "Quote", false);

    private DataRowSet topRowSet;
    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.WARN);

        topMetadata.addMetaField(rowNum);
        topMetadata.addMetaField(symbol);
        topMetadata.addMetaField(company);
        topMetadata.addMetaField(updatedAt);
        topMetadata.addMetaField(isOption);
        topMetadata.addMetaField(vectorB);
        topMetadata.addMetaField(vectorT);
        topMetadata.addMetaField(vectorDate);
        topMetadata.addMetaField(vectorI);
        topMetadata.addMetaField(vectorD);
        topMetadata.addMetaField(binary);

        topMetadata.addMetaField(security);
        securityMetadata.addMetaField(securityId);
        securityMetadata.addMetaField(exchange);
        securityMetadata.addMetaField(quote);
        quoteMetadata.addMetaField(bid);
        quoteMetadata.addMetaField(ask);

        topRowSet = new DataRowSet(topMetadata);
        for (int i = 0; i < 3; i++) {
            topRowSet.addValueAtRow(rowNum, i, i);
            topRowSet.addValueAtRow(symbol, "symbol_" + i, i);
            topRowSet.addValueAtRow(company, "company_" + i, i);
            topRowSet.addValueAtRow(updatedAt, new Date(), i);
            topRowSet.addValueAtRow(isOption, false, i);
            topRowSet.addValueAtRow(vectorB, new boolean[] { true, true }, i);
            topRowSet.addValueAtRow(vectorT, new String[] { "text1", "text2" },
                    i);
            topRowSet.addValueAtRow(vectorDate, new Date[] { new Date(0) }, i);
            topRowSet.addValueAtRow(vectorI, new long[] { 1, 2 }, i);
            topRowSet.addValueAtRow(vectorD, new double[] { 1.5, 2.5 }, i);
            topRowSet.addValueAtRow(binary, new byte[] { 1 }, i);

            DataRowSet securityRowSet = new DataRowSet(securityMetadata);
            for (int j = 0; j < 3; j++) {
                securityRowSet.addValueAtRow(securityId, (i + 1) * (j + 1), j);
                securityRowSet.addValueAtRow(exchange, "exchange_" + i + "_"
                        + j, j);

                DataRowSet quoteRowSet = new DataRowSet(quoteMetadata);
                for (int k = 0; k < 2; k++) {
                    quoteRowSet.addValueAtRow(ask, (i + 1) * (j + 1) * (k + 1),
                            k);
                    quoteRowSet.addValueAtRow(bid, (i + 1) * (j + 1) * (k + 1)
                            * 1.1, k);
                }
                securityRowSet.addValueAtRow(quote, quoteRowSet, j);
            }
            topRowSet.addValueAtRow(security, securityRowSet, i);
        }

        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        SimpleModule module = new SimpleModule();
        module.addSerializer(DataRow.class,
                new DataRowSerializer(DataRow.class));
        module.addSerializer(DataRowSet.class, new DataRowSetSerializer(
                DataRowSet.class));
        module.addSerializer(Metadata.class, new MetadataSerializer(
                Metadata.class));
        module.addSerializer(DataProvider.class, new DataProviderSerializer(
                DataProvider.class));
        //
        module.addDeserializer(DataRow.class, new DataRowDeserializer());
        module.addDeserializer(DataRowSet.class, new DataRowSetDeserializer());
        module.addDeserializer(Metadata.class, new MetadataDeserializer());
        //
        mapper.registerModule(module);

    }

    @Test
    public void testSerialize() throws IOException {
        String topRowSetJson = mapper.writeValueAsString(topRowSet);
        // debugPrint(topRowSetJson);

        DataRowSet deserRowSet = mapper.readValue(topRowSetJson,
                DataRowSet.class);
        String deserRowSetJson = mapper.writeValueAsString(deserRowSet);

        // System.out.println(deserRowSetJson);

        assertEquals(topRowSetJson, deserRowSetJson);
    }

    @SuppressWarnings("unchecked")
    private void debugPrint(String topRowSetJson) throws IOException,
            JsonParseException, JsonMappingException {
        List<Object> topList = mapper.readValue(topRowSetJson, List.class);
        assertEquals(3, topList.size());
        for (int i = 0; i < 3; i++) {
            List<Object> list = (List<Object>) topList.get(i);
            debugPrint("\t", list);
            System.out.println();
        }
    }

    @SuppressWarnings("unchecked")
    private void debugPrint(String prefix, List<Object> list) {
        for (int j = 0; j < list.size(); j++) {
            Object value = list.get(j);
            if (value instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) value;
                debugPrint(prefix, map);
            } else if (value instanceof List) {
                List<Object> ilist = (List<Object>) value;
                debugPrint(prefix, ilist);
            } else {
                System.out.println(prefix + " " + value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void debugPrint(String prefix, Map<String, Object> map) {
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (e.getValue() instanceof List) {
                List<Object> list = (List<Object>) e.getValue();
                debugPrint(prefix + "\t", list);
            } else {
                System.out.println(prefix + e.getKey() + "=>" + e.getValue());
            }
        }
    }
}
