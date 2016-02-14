package com.plexobject.dp.sample.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.plexobject.domain.Configuration;
import com.plexobject.domain.Constants;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.sample.domain.InfoResponse;
import com.plexobject.dp.sample.domain.QueryResponse;
import com.plexobject.dp.sample.domain.SharedMeta;
import com.plexobject.dp.sample.util.DataFactory;
import com.plexobject.handler.BasePayload;
import com.plexobject.handler.RequestHandler;
import com.plexobject.handler.ws.WSRequestHandlerAdapter;
import com.plexobject.service.Interceptor;
import com.plexobject.service.ServiceConfigDesc;
import com.plexobject.service.ServiceRegistry;

public class DataServiceImplTest {
    public static final int DEFAULT_PORT = 8186;

    private static ServiceRegistry serviceRegistry;
    private static WSRequestHandlerAdapter requestHandlerAdapter;
    private static final MetaField[] INPUT_FIELDS = new MetaField[] {
            MetaFieldFactory.create("symbol", "Security",
                    MetaFieldType.SCALAR_TEXT, true),
            MetaFieldFactory.create("userId", "User",
                    MetaFieldType.SCALAR_INTEGER, true),
            MetaFieldFactory.create("symbolQuery", "Security",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("watchlistName", "Watchlist",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("accountId", "Account",
                    MetaFieldType.SCALAR_INTEGER, true), };

    private static final MetaField[] OUTPUT_FIELDS = new MetaField[] {
            MetaFieldFactory.create("order.date", "Order",
                    MetaFieldType.SCALAR_DATE, false),
            MetaFieldFactory.create("security.optionType", "Security",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("user.portfolio", "User",
                    MetaFieldType.ROWSET, false),
            MetaFieldFactory.create("underlyingSymbol", "Security",
                    MetaFieldType.SCALAR_TEXT, true),
            MetaFieldFactory.create("security.description", "Security",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("security.exDividendDate", "Security",
                    MetaFieldType.SCALAR_DATE, false),
            MetaFieldFactory.create("security.type", "Security",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("beta", "Security", MetaFieldType.ROWSET,
                    false),
            MetaFieldFactory.create("orderId", "Order",
                    MetaFieldType.SCALAR_INTEGER, true),
            MetaFieldFactory.create("security.dividendInterval", "Security",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("quote.markPrice", "Quote",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("order.priceType", "Order",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("quote.sales", "Quote",
                    MetaFieldType.ROWSET, false),
            MetaFieldFactory.create("order.fillQuantity", "Order",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("quote.bidPrice", "Quote",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("security.strikePrice", "Security",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("user.roles", "User",
                    MetaFieldType.VECTOR_TEXT, false),
            MetaFieldFactory.create("order.fillPrice", "Order",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("order.status", "Order",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("watchlist.securities", "Watchlist",
                    MetaFieldType.ROWSET, false),
            MetaFieldFactory.create("order.fillDate", "Order",
                    MetaFieldType.SCALAR_DATE, false),
            MetaFieldFactory.create("position.price", "Position",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("symbol", "Security",
                    MetaFieldType.SCALAR_TEXT, true),
            MetaFieldFactory.create("order.quantity", "Order",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("quote.volume", "Quote",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("company.address", "Company",
                    MetaFieldType.ROWSET, false),
            MetaFieldFactory.create("position.quantity", "Position",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("positionId", "Position",
                    MetaFieldType.SCALAR_INTEGER, true),
            MetaFieldFactory.create("user.email", "User",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("quote.closePrice", "Quote",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("company.name", "Company",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("security.expirationDate", "Security",
                    MetaFieldType.SCALAR_DATE, false),
            MetaFieldFactory.create("exchange", "Security",
                    MetaFieldType.SCALAR_TEXT, true),
            MetaFieldFactory.create("account.name", "Account",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("user.address", "User",
                    MetaFieldType.ROWSET, false),
            MetaFieldFactory.create("order.orderLegs", "Order",
                    MetaFieldType.ROWSET, false),
            MetaFieldFactory.create("securityId", "Security",
                    MetaFieldType.SCALAR_INTEGER, true),
            MetaFieldFactory.create("security.optionRoot", "Security",
                    MetaFieldType.ROWSET, false),
            MetaFieldFactory.create("quote.askPrice", "Quote",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("order.price", "Order",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("security.dividendRate", "Security",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("watchlistId", "Watchlist",
                    MetaFieldType.SCALAR_INTEGER, false),
            MetaFieldFactory.create("watchlist.name", "Watchlist",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("quote.tradePrice", "Quote",
                    MetaFieldType.SCALAR_DECIMAL, false),
            MetaFieldFactory.create("accountId", "Account",
                    MetaFieldType.SCALAR_INTEGER, true),
            MetaFieldFactory.create("user.name", "User",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("userId", "User",
                    MetaFieldType.SCALAR_INTEGER, true),
            MetaFieldFactory.create("marketSession", "Security",
                    MetaFieldType.SCALAR_TEXT, false),
            MetaFieldFactory.create("user.dateOfBirth", "User",
                    MetaFieldType.SCALAR_DATE, false),
            MetaFieldFactory.create("account.type", "Account",
                    MetaFieldType.SCALAR_TEXT, false), };

    @BeforeClass
    public static void setUp() throws Exception {
        Properties props = new Properties();
        props.setProperty(Constants.HTTP_PORT, String.valueOf(DEFAULT_PORT));
        props.setProperty(Constants.JAXWS_NAMESPACE, "");
        Configuration config = new Configuration(props);
        if (config.getBoolean("log")) {
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
        DataServiceImpl.addCustomerSerialization(null);

        serviceRegistry.start();
        Thread.sleep(500);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        serviceRegistry.stop();
    }

    @Test
    public void testInfo() throws Throwable {
        String jsonResp = TestWebUtils.httpGet("http://localhost:"
                + DEFAULT_PORT + "/data/info");
        InfoResponse response = TestWebUtils.decodeDataInfoResponse(jsonResp);
        assertTrue(response.getInfoResponse().getRequestMetadata().size() > 0);
        assertTrue(response.getInfoResponse().getResponseMetadata().size() > 0);
        for (MetaField f : INPUT_FIELDS) {
            assertTrue("Expecting " + f, response.getInfoResponse()
                    .getRequestMetadata().contains(f));
        }
        for (MetaField f : OUTPUT_FIELDS) {
            assertTrue("Expecting " + f, response.getInfoResponse()
                    .getResponseMetadata().contains(f));
        }
    }

    @Test
    public void testInfoByKind() throws Throwable {
        String jsonResp = TestWebUtils.httpGet("http://localhost:"
                + DEFAULT_PORT + "/data/info?kinds=Order");
        InfoResponse response = TestWebUtils.decodeDataInfoResponse(jsonResp);
        assertTrue(response.getInfoResponse().getResponseMetadata().size() > 0);
        for (MetaField f : OUTPUT_FIELDS) {
            if (f.getKind().equals("Order")) {
                assertTrue("Expecting " + f, response.getInfoResponse()
                        .getResponseMetadata().contains(f));
            }
        }
    }

    @Test
    public void testGetSymbols() throws Throwable {
        String jsonResp = TestWebUtils.httpGet("http://localhost:"
                + DEFAULT_PORT + "/data?responseFields="
                + SharedMeta.symbol.getName());
        QueryResponse response = TestWebUtils.decodeQueryResponse(jsonResp);
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolsProvider"));

        assertTrue(response.getQueryResponse().getFields().size() > 0);
        for (DataRow row : response.getQueryResponse().getFields()
                .getRows()) {
            assertTrue(row.getValueAsText(SharedMeta.symbol).length() > 0);
        }
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
    }

    @Test
    public void testSearch() throws Throwable {
        String jsonResp = TestWebUtils
                .httpGet("http://localhost:"
                        + DEFAULT_PORT
                        + "/data?responseFields=exchange,symbol,underlyingSymbol&symbolQuery=G");
        QueryResponse response = TestWebUtils.decodeQueryResponse(jsonResp);
        assertTrue(response.getQueryResponse().getFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolSearchProvider"));

        for (int i = 0; i < response.getQueryResponse().getFields()
                .size(); i++) {
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.symbol, i).contains("G"));
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.underlyingSymbol, i)
                    .contains("G"));
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.exchange, i).length() > 0);
        }
    }

    @Test
    public void testGetSecuritiesBySearch() throws Throwable {
        String jsonResp = TestWebUtils
                .httpGet("http://localhost:"
                        + DEFAULT_PORT
                        + "/data?responseFields=exchange,symbol,underlyingSymbol&symbolQuery=G");
        QueryResponse response = TestWebUtils.decodeQueryResponse(jsonResp);
        assertTrue(response.getQueryResponse().getFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolSearchProvider"));

        for (int i = 0; i < response.getQueryResponse().getFields()
                .size(); i++) {
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.symbol, i).contains("G"));
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.underlyingSymbol, i)
                    .contains("G"));
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.exchange, i).length() > 0);
        }
    }

    @Test
    public void testGetCompaniesBySearch() throws Throwable {
        String jsonResp = TestWebUtils
                .httpGet("http://localhost:"
                        + DEFAULT_PORT
                        + "/data?responseFields=exchange,symbol,underlyingSymbol,security.type,company.name,company.address&symbolQuery=M");
        QueryResponse response = TestWebUtils.decodeQueryResponse(jsonResp);

        assertTrue(response.getQueryResponse().getFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolSearchProvider"));

        for (int i = 0; i < response.getQueryResponse().getFields()
                .size(); i++) {
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.symbol, i).contains("M"));
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.underlyingSymbol, i)
                    .contains("M"));
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.exchange, i).length() > 0);
            assertTrue(response
                    .getQueryResponse()
                    .getFields()
                    .getValueAsText(MetaFieldFactory.lookup("security.type"), i)
                    .length() > 0);
            DataRowSet address = response
                    .getQueryResponse()
                    .getFields()
                    .getValueAsRowSet(
                            MetaFieldFactory.lookup("company.address"), 0);
            assertEquals(1, address.size());
            assertTrue(address.getValueAsText(
                    MetaFieldFactory.lookup("address.zip"), 0).length() > 0);
            assertTrue(address.getValueAsText(
                    MetaFieldFactory.lookup("address.city"), 0).length() > 0);
            assertTrue(address.getValueAsText(
                    MetaFieldFactory.lookup("address.street"), 0).length() > 0);
        }
    }

    @Test
    public void testGetQuoteBySearch() throws Throwable {
        String jsonResp = TestWebUtils
                .httpGet("http://localhost:"
                        + DEFAULT_PORT
                        + "/data?responseFields=exchange,symbol,quote.bidPrice,quote.askPrice,quote.sales,company.name&symbolQuery=AAPL");
        QueryResponse response = TestWebUtils.decodeQueryResponse(jsonResp);

        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertEquals(2, response.getQueryResponse().getFields().size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("QuotesBySymbolsProvider"));
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SymbolSearchProvider"));
        assertTrue(response.getQueryResponse().getProviders()
                .contains("CompaniesBySymbolsProvider"));

        for (int i = 0; i < response.getQueryResponse().getFields()
                .size(); i++) {
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.symbol, i).contains("AAPL"));
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsText(SharedMeta.exchange, i).length() > 0);
            DataRowSet sales = response
                    .getQueryResponse()
                    .getFields()
                    .getValueAsRowSet(MetaFieldFactory.lookup("quote.sales"), 0);
            assertEquals(5, sales.size());
            for (int j = 0; j < 5; j++) {
                assertTrue(sales.getValueAsLong(
                        MetaFieldFactory.lookup("timeOfSale.volume"), j) > 0);
                assertTrue(sales.getValueAsDecimal(
                        MetaFieldFactory.lookup("timeOfSale.price"), j) > 0);
                assertTrue(sales.getValueAsText(
                        MetaFieldFactory.lookup("symbol"), j).length() > 0);
            }
        }
    }

    @Test
    public void testGetAccounts() throws Throwable {
        String jsonResp = TestWebUtils.httpGet("http://localhost:"
                + DEFAULT_PORT
                + "/data?responseFields=userId,user.accounts,user.portfolio");

        QueryResponse response = TestWebUtils.decodeQueryResponse(jsonResp);

        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertEquals(10, response.getQueryResponse().getFields().size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("UsersProvider"));
        assertTrue(response.getQueryResponse().getProviders()
                .contains("UsersByIdsProvider"));

        for (int i = 0; i < response.getQueryResponse().getFields()
                .size(); i++) {
            assertTrue(response.getQueryResponse().getFields()
                    .getValueAsLong(SharedMeta.userId, i) > 0);
            DataRowSet accounts = response
                    .getQueryResponse()
                    .getFields()
                    .getValueAsRowSet(MetaFieldFactory.lookup("user.accounts"),
                            0);
            assertEquals(5, accounts.size());
            for (int j = 0; j < 5; j++) {
                assertTrue(accounts.getValueAsLong(
                        MetaFieldFactory.lookup("accountId"), j) > 0);
                assertTrue(accounts.getValueAsText(
                        MetaFieldFactory.lookup("account.type"), j).length() > 0);
                assertTrue(accounts.getValueAsText(
                        MetaFieldFactory.lookup("account.name"), j).length() > 0);
            }
        }
    }

    @Test
    public void testGetSecuritiesBySymbol() throws Throwable {
        String jsonResp = TestWebUtils.httpGet("http://localhost:"
                + DEFAULT_PORT
                + "/data?responseFields=exchange,underlyingSymbol&symbol=F");
        QueryResponse response = TestWebUtils.decodeQueryResponse(jsonResp);
        assertTrue(response.getQueryResponse().getProviders()
                .contains("SecuritiesBySymbolsProvider"));

        assertTrue(response.getQueryResponse().getFields().size() > 0);
        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertEquals("F", response.getQueryResponse().getFields()
                .getValueAsText(SharedMeta.symbol, 0));
        assertEquals("F", response.getQueryResponse().getFields()
                .getValueAsText(SharedMeta.underlyingSymbol, 0));
    }

    @Test
    public void testGetAccountsOrders() throws Throwable {
        String jsonResp = TestWebUtils.httpGet("http://localhost:"
                + DEFAULT_PORT + "/data?responseFields=user.accounts");
        QueryResponse response = TestWebUtils.decodeQueryResponse(jsonResp);

        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertEquals(10, response.getQueryResponse().getFields().size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("UsersProvider"));
        long accountId = 0;
        for (int i = 0; i < response.getQueryResponse().getFields()
                .size(); i++) {
            DataRowSet accounts = response
                    .getQueryResponse()
                    .getFields()
                    .getValueAsRowSet(MetaFieldFactory.lookup("user.accounts"),
                            0);
            assertEquals(5, accounts.size());
            for (int j = 0; j < 5; j++) {
                accountId = accounts.getValueAsLong(
                        MetaFieldFactory.lookup("accountId"), j);
            }
        }

        jsonResp = TestWebUtils
                .httpGet("http://localhost:"
                        + DEFAULT_PORT
                        + "/data?responseFields=orderId,order.date,order.price,order.quantity,order.status,order.orderLegs&accountId="
                        + accountId);
        response = TestWebUtils.decodeQueryResponse(jsonResp);

        assertEquals(0, response.getQueryResponse().getErrorsByProviderName()
                .size());
        assertEquals(20, response.getQueryResponse().getFields().size());
        assertTrue(response.getQueryResponse().getProviders()
                .contains("OrdersByAccountIdsProvider"));
        for (int i = 0; i < response.getQueryResponse().getFields()
                .size(); i++) {
            assertTrue(response
                    .getQueryResponse()
                    .getFields()
                    .getValueAsDecimal(
                            MetaFieldFactory.lookup("order.quantity"), i) > 0);
            DataRowSet legs = response
                    .getQueryResponse()
                    .getFields()
                    .getValueAsRowSet(
                            MetaFieldFactory.lookup("order.orderLegs"), i);
            assertEquals(5, legs.size());
        }

    }
}
