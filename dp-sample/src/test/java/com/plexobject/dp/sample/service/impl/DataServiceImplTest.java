package com.plexobject.dp.sample.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.plexobject.domain.Configuration;
import com.plexobject.domain.Constants;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.json.DataProviderDeserializer;
import com.plexobject.dp.json.DataProviderSerializer;
import com.plexobject.dp.json.DataRowDeserializer;
import com.plexobject.dp.json.DataRowSerializer;
import com.plexobject.dp.json.DataRowSetDeserializer;
import com.plexobject.dp.json.DataRowSetSerializer;
import com.plexobject.dp.json.MetadataDeserializer;
import com.plexobject.dp.json.MetadataSerializer;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.sample.domain.QueryResponse;
import com.plexobject.dp.sample.util.DataFactory;
import com.plexobject.encode.CodecConfigurer;
import com.plexobject.encode.CodecType;
import com.plexobject.encode.ObjectCodecFactory;
import com.plexobject.handler.BasePayload;
import com.plexobject.handler.RequestHandler;
import com.plexobject.handler.ws.WSRequestHandlerAdapter;
import com.plexobject.http.ServiceInvocationException;
import com.plexobject.service.Interceptor;
import com.plexobject.service.ServiceConfigDesc;
import com.plexobject.service.ServiceRegistry;

public class DataServiceImplTest {
    public static final int DEFAULT_PORT = 8186;

    private static ServiceRegistry serviceRegistry;
    private static WSRequestHandlerAdapter requestHandlerAdapter;

    @BeforeClass
    public static void setUp() throws Exception {
        Properties props = new Properties();
        props.setProperty(Constants.HTTP_PORT, String.valueOf(DEFAULT_PORT));
        props.setProperty(Constants.JAXWS_NAMESPACE, "");
        Configuration config = new Configuration(props);
        if (true || config.getBoolean("log")) {
            BasicConfigurator.configure();
            LogManager.getRootLogger().setLevel(Level.WARN);
        }
        DataFactory.addData();
        serviceRegistry = new ServiceRegistry(config);
        requestHandlerAdapter = new WSRequestHandlerAdapter(serviceRegistry);
        Map<ServiceConfigDesc, RequestHandler> handlers = requestHandlerAdapter
                .createFromPackages("com.plexobject.dp.sample.service");
        for (Map.Entry<ServiceConfigDesc, RequestHandler> e : handlers
                .entrySet()) {
            serviceRegistry.addRequestHandler(e.getKey(), e.getValue());
        }
        serviceRegistry
                .addInputInterceptor(new Interceptor<BasePayload<Object>>() {
                    @Override
                    public BasePayload<Object> intercept(
                            BasePayload<Object> input) {
                        return input;
                    }
                });
        serviceRegistry
                .addOutputInterceptor(new Interceptor<BasePayload<Object>>() {
                    @Override
                    public BasePayload<Object> intercept(
                            BasePayload<Object> output) {
                        return output;
                    }
                });
        ObjectCodecFactory.getInstance().getObjectCodec(CodecType.JSON)
                .setCodecConfigurer(new CodecConfigurer() {
                    @Override
                    public void configureCodec(Object underlyingEncoder) {
                        if (underlyingEncoder instanceof ObjectMapper) {
                            ObjectMapper mapper = (ObjectMapper) underlyingEncoder;
                            SimpleModule module = new SimpleModule();
                            module.addSerializer(DataRow.class,
                                    new DataRowSerializer(DataRow.class));
                            module.addSerializer(DataRowSet.class,
                                    new DataRowSetSerializer(DataRowSet.class));
                            module.addSerializer(Metadata.class,
                                    new MetadataSerializer(Metadata.class));
                            module.addSerializer(DataProvider.class,
                                    new DataProviderSerializer(
                                            DataProvider.class));
                            //
                            module.addDeserializer(DataRow.class,
                                    new DataRowDeserializer());
                            module.addDeserializer(DataRowSet.class,
                                    new DataRowSetDeserializer());
                            module.addDeserializer(Metadata.class,
                                    new MetadataDeserializer());
                            module.addDeserializer(DataProvider.class,
                                    new DataProviderDeserializer(null));
                            //
                            mapper.registerModule(module);
                        }
                    }
                });
        serviceRegistry.start();
        Thread.sleep(500);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        serviceRegistry.stop();
    }

    @Test
    public void testGetSymbols() throws Throwable {
        String jsonResp = httpGet("http://localhost:" + DEFAULT_PORT
                + "/data?responseFields=symbol");
        System.out.println(jsonResp);
        QueryResponse response = decode(jsonResp);
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolsProvider"));

        assertTrue(response.getQueryResponse().getResponseFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
    }

    @Test
    public void testSearch() throws Throwable {
        String jsonResp = httpGet("http://localhost:"
                + DEFAULT_PORT
                + "/data?responseFields=exchange,symbol,underlyingSymbol&symbolQuery=G");
        QueryResponse response = decode(jsonResp);
        assertTrue(response.getQueryResponse().getResponseFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolSearchProvider"));
        MetaField symbol = MetaFieldFactory.create("symbol",
                MetaFieldType.SCALAR_TEXT);
        MetaField underlyingSymbol = MetaFieldFactory.create(
                "underlyingSymbol", MetaFieldType.SCALAR_TEXT);

        for (int i = 0; i < response.getQueryResponse().getResponseFields()
                .size(); i++) {
            assertTrue(response.getQueryResponse().getResponseFields()
                    .getValueAsText(symbol, i).contains("G"));
            assertTrue(response.getQueryResponse().getResponseFields()
                    .getValueAsText(underlyingSymbol, i).contains("G"));
        }
    }

    @Test
    public void testGetSecuritiesBySearch() throws Throwable {
        String jsonResp = httpGet("http://localhost:"
                + DEFAULT_PORT
                + "/data?responseFields=exchange,symbol,underlyingSymbol&symbolQuery=G");
        QueryResponse response = decode(jsonResp);
        assertTrue(response.getQueryResponse().getResponseFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolSearchProvider"));
        MetaField symbol = MetaFieldFactory.create("symbol",
                MetaFieldType.SCALAR_TEXT);
        MetaField underlyingSymbol = MetaFieldFactory.create(
                "underlyingSymbol", MetaFieldType.SCALAR_TEXT);

        for (int i = 0; i < response.getQueryResponse().getResponseFields()
                .size(); i++) {
            assertTrue(response.getQueryResponse().getResponseFields()
                    .getValueAsText(symbol, i).contains("G"));
            assertTrue(response.getQueryResponse().getResponseFields()
                    .getValueAsText(underlyingSymbol, i).contains("G"));
        }
    }

    @Test
    public void testGetCompaniesBySearch() throws Throwable {
        String jsonResp = httpGet("http://localhost:"
                + DEFAULT_PORT
                + "/data?responseFields=exchange,symbol,underlyingSymbol,security.type,company.name,company.address&symbolQuery=G");
        QueryResponse response = decode(jsonResp);
        // System.out.println("XX " +
        // response.queryResponse.getResponseFields());

        assertTrue(response.getQueryResponse().getResponseFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolSearchProvider"));
        MetaField symbol = MetaFieldFactory.create("symbol",
                MetaFieldType.SCALAR_TEXT);
        MetaField underlyingSymbol = MetaFieldFactory.create(
                "underlyingSymbol", MetaFieldType.SCALAR_TEXT);

        for (int i = 0; i < response.getQueryResponse().getResponseFields()
                .size(); i++) {
            assertTrue(response.getQueryResponse().getResponseFields()
                    .getValueAsText(symbol, i).contains("G"));
            assertTrue(response.getQueryResponse().getResponseFields()
                    .getValueAsText(underlyingSymbol, i).contains("G"));
        }
    }

    @Test
    public void testGetSecuritiesBySymbol() throws Throwable {
        String jsonResp = httpGet("http://localhost:" + DEFAULT_PORT
                + "/data?responseFields=exchange,underlyingSymbol&symbol=F");
        QueryResponse response = decode(jsonResp);
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SecuritiesBySymbolsProvider"));

        assertTrue(response.getQueryResponse().getResponseFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        MetaField symbol = MetaFieldFactory.create("symbol",
                MetaFieldType.SCALAR_TEXT);
        MetaField underlyingSymbol = MetaFieldFactory.create(
                "underlyingSymbol", MetaFieldType.SCALAR_TEXT);
        assertEquals("F", response.getQueryResponse().getResponseFields()
                .getValueAsText(symbol, 0));
        assertEquals("F", response.getQueryResponse().getResponseFields()
                .getValueAsText(underlyingSymbol, 0));
    }

    private QueryResponse decode(String jsonResp) {
        QueryResponse response = ObjectCodecFactory.getInstance()
                .getObjectCodec(CodecType.JSON)
                .decode(jsonResp, QueryResponse.class, null);
        return response;
    }

    static String httpGet(String target, String... headers) throws IOException {
        URL url = new URL(target);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        if (headers.length == 0) {
            con.setRequestProperty("Content-Type", "application/json");
        }
        for (int i = 0; i < headers.length - 1; i += 2) {
            con.setRequestProperty(headers[i], headers[i + 1]);
        }
        return getResponse(con);
    }

    private static String getResponse(HttpURLConnection con) throws IOException {
        if (con.getResponseCode() != 200) {
            System.err.println("HTTP ERROR " + con.getResponseCode() + ": "
                    + con.getResponseMessage());
            throw new ServiceInvocationException("Status "
                    + con.getResponseCode() + ": " + con.getResponseMessage(),
                    con.getResponseCode());
        }
        String resp = toString(con.getInputStream());
        return resp;
    }

    private static String toString(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
