package com.plexobject.dp.query.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Before;
import org.junit.Test;

import com.plexobject.dp.domain.DataRequest;
import com.plexobject.dp.domain.DataResponse;
import com.plexobject.dp.domain.DataRow;
import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.domain.QueryConfiguration;
import com.plexobject.dp.locator.DataProviderLocator;
import com.plexobject.dp.locator.impl.DataProviderLocatorImpl;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.provider.TaskGranularity;

public class QueryEngineImplTest {
    private static final String KIND_NAME = "Test";

    static class TimeoutProvider extends BaseProvider {
        private long sleepMillis;

        public TimeoutProvider(long sleepMillis, String name) {
            super(name, metaFrom(name), metaFrom("none"), metaFrom(name
                    + "-result"));
            this.sleepMillis = sleepMillis;
        }

        @Override
        public void produce(final DataRowSet requestRowSet,
                final DataRowSet responseRowSet, final QueryConfiguration config) {
            try {
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }

        @Override
        public String toString() {
            return "TimeoutProvider()";
        }
    }

    static class FailingProvider extends BaseProvider {
        private final RuntimeException error;

        public FailingProvider(String name, String errorName,
                RuntimeException error) {
            super(name, metaFrom(errorName), metaFrom("none"),
                    metaFrom(errorName + "-result"));
            this.error = error;
        }

        @Override
        public void produce(final DataRowSet requestRowSet,
                final DataRowSet responseRowSet, final QueryConfiguration config)
                throws DataProviderException {
            throw error;
        }

        @Override
        public String toString() {
            return "FailingProvider()";
        }
    }

    // this provider consumes and produces scalar input
    static class ScalarProvider extends BaseProvider {
        public ScalarProvider(String name, String[] requestFields,
                String[] optionalFields, String... responseFields) {
            super(name, metaFrom(requestFields), metaFrom(optionalFields),
                    metaFrom(responseFields));
        }

        @Override
        public void produce(final DataRowSet requestRowSet,
                final DataRowSet responseRowSet, final QueryConfiguration config)
                throws DataProviderException {
            for (int i = 0; i < requestRowSet.size(); i++) {
                for (MetaField metaField : getMandatoryRequestMetadata()
                        .getMetaFields()) {
                    requestRowSet.getValueAsText(metaField, i);
                }
                for (MetaField metaField : getOptionalRequestMetadata()
                        .getMetaFields()) {
                    if (requestRowSet.hasFieldValue(metaField, i)) {
                        requestRowSet.getValueAsText(metaField, i);
                    }
                }
            }
            int maxOutputRows = requestRowSet
                    .hasFieldValue(MetaFieldFactory.createInteger(
                            "maxOutputRows",
                            QueryConfiguration.class.getSimpleName(), false), 0) ? (int) requestRowSet
                    .getValueAsLong(MetaFieldFactory.createInteger(
                            "maxOutputRows",
                            QueryConfiguration.class.getSimpleName(), false), 0)
                    : requestRowSet.size();
            for (int i = 0; i < maxOutputRows; i++) {
                Collection<MetaField> responseFields = new ArrayList<>(
                        (responseRowSet.getMetadata().getMetaFields()));
                for (MetaField metaField : responseFields) {
                    if (getResponseMetadata().contains(metaField)) {
                        responseRowSet.addValueAtRow(metaField,
                                metaField.getName() + "-value-" + i, i);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "ScalarProvider(" + getMandatoryRequestMetadata() + "/"
                    + getOptionalRequestMetadata() + ") => "
                    + getResponseMetadata();
        }

    }

    static class VectorProvider extends BaseProvider {
        public VectorProvider(String name, String[] requestFields,
                String[] optionalFields, String... responseFields) {
            super(name, metaArrayFrom(requestFields),
                    metaArrayFrom(optionalFields),
                    metaArrayFrom(responseFields));
        }

        @Override
        public void produce(final DataRowSet requestRowSet,
                final DataRowSet responseRowSet, final QueryConfiguration config)
                throws DataProviderException {
            for (int i = 0; i < requestRowSet.size(); i++) {
                for (MetaField metaField : getMandatoryRequestMetadata()
                        .getMetaFields()) {
                    requestRowSet.getValueAsTextVector(metaField, i);
                }
            }
            int maxOutputRows = requestRowSet
                    .hasFieldValue(MetaFieldFactory.createInteger(
                            "maxOutputRows",
                            QueryConfiguration.class.getSimpleName(), false), 0) ? (int) requestRowSet
                    .getValueAsLong(MetaFieldFactory.createInteger(
                            "maxOutputRows",
                            QueryConfiguration.class.getSimpleName(), false), 0)
                    : requestRowSet.size();
            for (int i = 0; i < maxOutputRows; i++) {
                Collection<MetaField> responseFields = new ArrayList<>(
                        (responseRowSet.getMetadata().getMetaFields()));
                for (MetaField metaField : responseFields) {
                    if (getResponseMetadata().contains(metaField)) {
                        responseRowSet
                                .addValueAtRow(
                                        metaField,
                                        Arrays.asList(new String[] {
                                                metaField.getName()
                                                        + "-array-value1",
                                                metaField.getName()
                                                        + "-array-value2" }), i);
                    }
                }
            }
        }

    }

    private final DataProviderLocator dataProviderLocator = new DataProviderLocatorImpl();
    private final QueryEngineImpl dataProviders = new QueryEngineImpl(
            dataProviderLocator);
    private final QueryConfiguration config = new QueryConfiguration();

    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.INFO);
        MetaFieldFactory.reset();
        dataProviderLocator.register(new ScalarProvider("first",
                new String[] { "X" }, new String[] {}, "B", "F"));
        dataProviderLocator.register(new ScalarProvider("second",
                new String[] { "A" }, new String[] {}, "B", "C", "D"));
        dataProviderLocator.register(new ScalarProvider("third", new String[] {
                "B", "D" }, new String[] {}, "E", "F", "G"));
        dataProviderLocator.register(new ScalarProvider("fourth", new String[] {
                "B", "F" }, new String[] {}, "H", "I", "J"));
        dataProviderLocator.register(new ScalarProvider("fifth", new String[] {
                "L", "M" }, new String[] {}, "N", "O", "P"));
        dataProviderLocator.register(new ScalarProvider("sixth", new String[] {
                "B", "F", "L" }, new String[] {}, "Q", "R", "S"));
        dataProviderLocator.register(new ScalarProvider("seven", new String[] {
                "H", "N" }, new String[] {}, "V", "W", "X"));
        dataProviderLocator.register(new ScalarProvider("eight", new String[] {
                "L", "H" }, new String[] {}, "V", "W"));
        dataProviderLocator.register(new ScalarProvider("nine",
                new String[] { "F" }, new String[] {}, "G"));
        dataProviderLocator.register(new ScalarProvider("ten", new String[] {
                "V", "W" }, new String[] {}, "Z"));
    }

    @Test
    public void testGetDataProviders() {
        String[] datapoints = { "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "O", "P", "Q", "R", "S", "V", "W", "X", "Z" };
        for (String dp : datapoints) {
            Collection<DataProvider> providers = dataProviderLocator.locate(
                    metaFrom("A", "K", "L", "M"), metaFrom(dp));
            assertTrue(providers.size() > 0);
        }
    }

    @Test(expected = DataProviderException.class)
    public void testGetUnknownDataProviders() {
        dataProviderLocator.locate(metaFrom("A", "K", "L", "M"), metaFrom("Y"));
    }

    @Test
    public void testGetMissingMetaFields() {
        Metadata first = metaFrom("A", "B", "C");
        Metadata second = metaFrom("A", "B", "C");
        Metadata third = metaFrom("A", "B", "C", "D");
        Metadata fourth = metaFrom("X", "Y");

        assert first.containsAll(second);
        assert first.contains(second.getMetaFields().iterator().next());
        assert first.getMissingCount(second) == 0;
        assert first.getMissingCount(third) == 1;
        assert first.getMissingCount(fourth) == 2;
        assert second.getMissingCount(third) == 1;

        assertEquals(0, first.getMissingMetadata(second).size());
        assertEquals(1, first.getMissingMetadata(third).size());
        assertEquals(2, first.getMissingMetadata(fourth).size());
        assertEquals(1, second.getMissingMetadata(third).size());
    }

    @Test
    public void testProduceOptional() {
        dataProviderLocator.register(new ScalarProvider("one",
                new String[] { "input1" }, new String[] {}, "output1a",
                "output1b", "optional1"));
        dataProviderLocator.register(new ScalarProvider("two",
                new String[] { "output1b" }, new String[] { "optional1" },
                "output2a", "output2b"));

        DataRequest request = new DataRequest(rowsetFrom("input1"),
                metaFrom("output2a"), config);
        DataResponse response = dataProviders.query(request);
        assertEquals(0, response.getErrorsByProviderName().size());
        assertEquals(1, response.getFields().size());
        assertEquals(
                "output2a-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("output2a"), 0));
        assertEquals(
                "optional1-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("optional1"), 0));
    }

    @Test
    public void testProduceOptionalNotAvailable() {
        dataProviderLocator.register(new ScalarProvider("one",
                new String[] { "input1" }, new String[] {}, "output1a",
                "output1b"));
        dataProviderLocator.register(new ScalarProvider("two",
                new String[] { "output1b" }, new String[] { "optional1" },
                "output2a", "output2b"));

        DataRequest request = new DataRequest(rowsetFrom("input1"),
                metaFrom("output2a"), config);
        DataResponse response = dataProviders.query(request);
        assertEquals(0, response.getErrorsByProviderName().size());
        assertEquals(1, response.getFields().size());

        assertEquals(
                "output2a-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("output2a"), 0));
        assertFalse(response.getFields().hasFieldValue(
                MetaFieldFactory.lookup("optional1"), 0));
    }

    @Test
    public void testProduce() {
        long started = System.currentTimeMillis();
        DataRequest request = new DataRequest(rowsetFrom("A"), metaFrom("E",
                "F", "H"), config);
        DataResponse response = dataProviders.query(request);
        assertEquals(0, response.getErrorsByProviderName().size());
        assertEquals(1, response.getFields().size());

        assertEquals(
                "E-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("E"), 0));
        assertEquals(
                "F-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("F"), 0));
        assertEquals(
                "H-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("H"), 0));
        long elapsed = System.currentTimeMillis() - started;
        assertTrue(elapsed < 100);
    }

    @Test
    public void testProduceVector() {
        dataProviderLocator.register(new VectorProvider("one",
                new String[] { "uid" }, new String[] {}, "uname", "acctids"));
        dataProviderLocator.register(new VectorProvider("two",
                new String[] { "acctids" }, new String[] {}, "actname",
                "accttype"));
        dataProviderLocator.register(new VectorProvider("three",
                new String[] { "search" }, new String[] {}, "symbol",
                "company", "instrumentId"));
        dataProviderLocator.register(new VectorProvider("four", new String[] {
                "instrumentId", "symbol" }, new String[] {}, "positionCount",
                "orderCount"));

        long started = System.currentTimeMillis();
        DataRequest request = new DataRequest(rowsetArrayFrom("uid", "search"),
                metaArrayFrom("uname", "symbol", "positionCount"), config);
        DataResponse response = dataProviders.query(request);
        assertEquals(0, response.getErrorsByProviderName().size());
        assertEquals(1, response.getFields().size());

        assertEquals("uname-array-value1", response.getFields()
                .getValueAsTextVector(MetaFieldFactory.lookup("uname"), 0)[0]);
        assertEquals("uname-array-value2", response.getFields()
                .getValueAsTextVector(MetaFieldFactory.lookup("uname"), 0)[1]);
        assertEquals("symbol-array-value1", response.getFields()
                .getValueAsTextVector(MetaFieldFactory.lookup("symbol"), 0)[0]);
        assertEquals("symbol-array-value2", response.getFields()
                .getValueAsTextVector(MetaFieldFactory.lookup("symbol"), 0)[1]);
        assertEquals(
                "positionCount-array-value1",
                response.getFields().getValueAsTextVector(
                        MetaFieldFactory.lookup("positionCount"), 0)[0]);
        assertEquals(
                "positionCount-array-value2",
                response.getFields().getValueAsTextVector(
                        MetaFieldFactory.lookup("positionCount"), 0)[1]);
        long elapsed = System.currentTimeMillis() - started;
        assertTrue(elapsed < 100);
    }

    @Test
    public void testProduceMixedDataTypes() {
        dataProviderLocator.register(new ScalarProvider("one",
                new String[] { "q" }, new String[] {}, "instrumentId"));
        dataProviderLocator.register(new ScalarProvider("two",
                new String[] { "instrumentId" }, new String[] {},
                "companyName", "symbol"));
        dataProviderLocator.register(new ScalarProvider("three",
                new String[] { "symbol" }, new String[] {}, "bid", "ask",
                "volume"));
        dataProviderLocator.register(new ScalarProvider("four", new String[] {
                "bid", "bid" }, new String[] {}, "mark"));

        long started = System.currentTimeMillis();
        DataRequest request = new DataRequest(rowsetFrom("q"), metaFrom(
                "symbol", "bid", "mark"), config);
        request.getParameters()
                .addValueAtRow(
                        MetaFieldFactory
                                .createInteger("maxOutputRows",
                                        QueryConfiguration.class
                                                .getSimpleName(), false),
                        10, 0);

        DataResponse response = dataProviders.query(request);
        assertEquals(0, response.getErrorsByProviderName().size());
        assertEquals(10, response.getFields().size());

        for (int i = 0; i < 10; i++) {
            assertEquals("symbol-value-" + i, response.getFields()
                    .getValueAsText(MetaFieldFactory.lookup("symbol"), i));
            assertEquals(
                    "bid-value-" + i,
                    response.getFields().getValueAsText(
                            MetaFieldFactory.lookup("bid"), i));
            assertEquals("mark-value-" + i, response.getFields()
                    .getValueAsText(MetaFieldFactory.lookup("mark"), i));
        }
        long elapsed = System.currentTimeMillis() - started;
        assertTrue(elapsed < 100);
    }

    @Test
    public void testSimpleGetDataProviders() {
        // P1: X -> B, F
        // P2: A -> B, C, D
        // P3: B, D -> E, F, G
        // P4: B, F -> H, I, J
        // So Given A, we want to get E, F, H, which means we should look
        // E, which can be get metaFrom P3, but it requires B, D
        // B can be get metaFrom P1 and P2, but we will need X or A
        //
        Collection<DataProvider> providers = dataProviderLocator.locate(
                metaFrom("A"), metaFrom("E", "F", "H"));
        assertEquals(3, providers.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterTwice() {
        ScalarProvider provider1 = new ScalarProvider("one",
                new String[] { "a" }, new String[] {}, "b");
        ScalarProvider provider2 = new ScalarProvider("one",
                new String[] { "a" }, new String[] {}, "b");
        provider1.setTaskGranularity(TaskGranularity.FINE);
        dataProviderLocator.register(provider1);
        dataProviderLocator.register(provider2);
    }

    @Test
    public void testSingleProvider() {
        ScalarProvider provider1 = new ScalarProvider("one",
                new String[] { "a" }, new String[] {}, "b");
        provider1.setTaskGranularity(TaskGranularity.FINE);
        dataProviderLocator.register(provider1);

        DataRequest request = new DataRequest(rowsetFrom("a"), metaFrom("b"),
                config);
        DataResponse response = dataProviders.query(request);
        assertEquals(0, response.getErrorsByProviderName().size());
        assertEquals(1, response.getFields().size());

        assertEquals(
                "b-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("b"), 0));
    }

    @Test
    public void testUnregister() {
        DataProvider provider1 = new ScalarProvider("one",
                new String[] { "search" }, new String[] {}, "symbols");
        DataProvider provider2 = new ScalarProvider("two",
                new String[] { "symbols" }, new String[] {}, "quotes");
        DataProvider provider3 = new ScalarProvider("three",
                new String[] { "symbols" }, new String[] {}, "research");
        //
        dataProviderLocator.unregister(provider1);

        dataProviderLocator.register(provider1);
        dataProviderLocator.register(provider2);
        dataProviderLocator.register(provider3);
        config.setQueryTimeoutMillis(10);

        DataRequest request = new DataRequest(rowsetFrom("search"), metaFrom(
                "quotes", "research"), config);
        DataResponse response = dataProviders.query(request);
        assertEquals(0, response.getErrorsByProviderName().size());
        assertEquals(1, response.getFields().size());

        assertEquals(
                "quotes-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("quotes"), 0));
        assertEquals(
                "research-value-0",
                response.getFields().getValueAsText(
                        MetaFieldFactory.lookup("research"), 0));

        request = new DataRequest(rowsetFrom("search"), metaFrom("quotes",
                "research"), config);

        try {
            dataProviderLocator.unregister(provider2);
            dataProviderLocator.unregister(provider3);
            dataProviders.query(request);
            fail("Should have failed");
        } catch (DataProviderException e) {

        }
    }

    @Test(expected = DataProviderException.class)
    public void testTimeoutFine() {
        TimeoutProvider provider = new TimeoutProvider(200, "timeout");
        provider.setTaskGranularity(TaskGranularity.FINE);
        dataProviderLocator.register(provider);
        dataProviderLocator.register(new ScalarProvider("one",
                new String[] { "a" }, new String[] {}, "b"));
        dataProviderLocator.register(new ScalarProvider("two",
                new String[] { "b" }, new String[] {}, "d", "e"));

        config.setQueryTimeoutMillis(10);

        DataRequest request = new DataRequest(rowsetFrom("timeout", "a"),
                metaFrom("d", "e", "timeout-result"), config);
        dataProviders.query(request);
    }

    @Test(expected = DataProviderException.class)
    public void testTimeoutCoarse() {
        TimeoutProvider provider = new TimeoutProvider(200, "timeout");
        provider.setTaskGranularity(TaskGranularity.COARSE);
        dataProviderLocator.register(provider);
        dataProviderLocator.register(new ScalarProvider("one",
                new String[] { "a" }, new String[] {}, "b"));
        dataProviderLocator.register(new ScalarProvider("two",
                new String[] { "b" }, new String[] {}, "d", "e"));

        config.setQueryTimeoutMillis(10);
        final ExecutorService defaultExecutor = Executors.newFixedThreadPool(5);
        dataProviders.setDefaultExecutor(defaultExecutor);
        assertEquals(defaultExecutor, dataProviders.getDefaultExecutor());

        DataRequest request = new DataRequest(rowsetFrom("timeout", "a"),
                metaFrom("d", "e", "timeout-result"), config);
        DataResponse response = dataProviders.query(request);
        assertEquals(0, response.getErrorsByProviderName().size());
        assertEquals(1, response.getFields().size());

    }

    @Test
    public void testError() {
        FailingProvider provider = new FailingProvider("testError0", "error",
                new RuntimeException("failed"));
        provider.setTaskGranularity(TaskGranularity.COARSE);
        dataProviderLocator.register(provider);
        dataProviderLocator.register(new ScalarProvider("testError1",
                new String[] { "query" }, new String[] {}, "lookupResults"));
        dataProviderLocator.register(new ScalarProvider("testError2",
                new String[] { "lookupResults" }, new String[] {}, "somedata"));

        DataRequest request = new DataRequest(rowsetFrom("error", "query"),
                metaFrom("lookupResults", "somedata", "error-result"), config);
        DataResponse response = dataProviders.query(request);
        assertEquals(1, response.getErrorsByProviderName().size());
        assertEquals(1, response.getFields().size());
    }

    @Test
    public void testErrorWithAbort() {
        FailingProvider provider = new FailingProvider("testErrorWithAbort1",
                "error", new RuntimeException("failed"));
        provider.setTaskGranularity(TaskGranularity.COARSE);
        dataProviderLocator.register(provider);
        dataProviderLocator.register(new ScalarProvider("testErrorWithAbort2",
                new String[] { "query" }, new String[] {}, "lookupResults"));
        dataProviderLocator.register(new ScalarProvider("testErrorWithAbort3",
                new String[] { "lookupResults" }, new String[] {},
                "detailsData"));

        config.setAbortUponPartialFailure(true);
        DataRequest request = new DataRequest(rowsetFrom("error", "query"),
                metaFrom("lookupResults", "detailsData", "error-result"),
                config);
        DataResponse response = dataProviders.query(request);
        assertEquals("Error " + response.getErrorsByProviderName(), 1, response
                .getErrorsByProviderName().size());
    }

    static Metadata metaFrom(String... args) {
        Metadata metaFields = new Metadata();
        for (String arg : args) {
            metaFields.addMetaField(MetaFieldFactory.createText(arg,
                    KIND_NAME, false));
        }
        return metaFields;
    }

    static Metadata metaArrayFrom(String... args) {
        Metadata metaFields = new Metadata();
        for (String arg : args) {
            metaFields.addMetaField(MetaFieldFactory.createVectorText(arg,
                    KIND_NAME, false));
        }
        return metaFields;
    }

    static DataRowSet rowsetFrom(String... args) {
        Metadata metaFields = metaFrom(args);
        DataRowSet rowset = new DataRowSet(metaFields);
        DataRow row = new DataRow();
        for (String arg : args) {
            row.addField(
                    MetaFieldFactory.createText(arg, KIND_NAME, false), arg
                            + "-input");
        }
        rowset.addRow(row);
        return rowset;
    }

    static DataRowSet rowsetArrayFrom(String... args) {
        Metadata metaFields = metaArrayFrom(args);
        DataRowSet rowset = new DataRowSet(metaFields);
        for (int i = 0; i < args.length; i++) {
            rowset.addValueAtRow(MetaFieldFactory.createVectorText(args[i],
                    KIND_NAME, false), Collections.singleton(args[i]), 0);
        }
        return rowset;
    }
}
