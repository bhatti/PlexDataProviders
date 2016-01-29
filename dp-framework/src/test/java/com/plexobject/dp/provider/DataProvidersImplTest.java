package com.plexobject.dp.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.Before;
import org.junit.Test;

import com.plexobject.dp.domain.DataConfiguration;
import com.plexobject.dp.domain.DataFieldRow;
import com.plexobject.dp.domain.DataFieldRowSet;
import com.plexobject.dp.domain.MetaField;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.MetaFieldType;
import com.plexobject.dp.domain.MetaFields;

public class DataProvidersImplTest {
    public static class TimeoutProvider extends BaseProvider {
        private long sleepMillis;

        public TimeoutProvider(long sleepMillis, String name) {
            super(metaFrom(name), metaFrom("none"), metaFrom(name + "-result"));
            this.sleepMillis = sleepMillis;
        }

        @Override
        public void produce(final DataFieldRowSet requestRowSet,
                final DataFieldRowSet responseRowSet,
                final DataConfiguration config) {
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

    public static class FailingProvider extends BaseProvider {
        private final RuntimeException error;

        public FailingProvider(String name, RuntimeException error) {
            super(metaFrom(name), metaFrom("none"), metaFrom(name + "-result"));
            this.error = error;
        }

        @Override
        public void produce(final DataFieldRowSet requestRowSet,
                final DataFieldRowSet responseRowSet,
                final DataConfiguration config) throws DataProviderException {
            throw error;
        }

        @Override
        public String toString() {
            return "FailingProvider()";
        }
    }

    public static class OptionalProvider extends BaseProvider {
        public OptionalProvider(String[] requestFields,
                String[] optionalFields, String... responseFields) {
            super(metaFrom(requestFields), metaFrom(optionalFields),
                    metaFrom(responseFields));
        }

        @Override
        public void produce(final DataFieldRowSet requestRowSet,
                final DataFieldRowSet responseRowSet,
                final DataConfiguration config) throws DataProviderException {
            for (int i = 0; i < requestRowSet.size(); i++) {
                for (MetaField metaField : getMandatoryRequestFields()
                        .getMetaFields()) {
                    requestRowSet.getValueAsText(metaField, i);
                }
                for (MetaField metaField : getOptionalRequestFields()
                        .getMetaFields()) {
                    requestRowSet.getValueAsText(metaField, i);
                }
                Collection<MetaField> responseFields = new ArrayList<>(
                        (responseRowSet.getMetaFields().getMetaFields()));
                for (MetaField metaField : responseFields) {
                    if (getResponseFields().contains(metaField)) {
                        responseRowSet.addDataField(metaField,
                                metaField.getName() + "-value", i);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "TestProvider(" + getMandatoryRequestFields() + "/"
                    + getOptionalRequestFields() + ") => "
                    + getResponseFields();
        }

    }

    public static class TestProvider extends BaseProvider {
        public TestProvider(String[] requestFields, String[] optionalFields,
                String... responseFields) {
            super(metaFrom(requestFields), metaFrom(optionalFields),
                    metaFrom(responseFields));
        }

        @Override
        public void produce(final DataFieldRowSet requestRowSet,
                final DataFieldRowSet responseRowSet,
                final DataConfiguration config) throws DataProviderException {
            for (int i = 0; i < requestRowSet.size(); i++) {
                for (MetaField metaField : getMandatoryRequestFields()
                        .getMetaFields()) {
                    requestRowSet.getValueAsText(metaField, i);
                }
                Collection<MetaField> responseFields = new ArrayList<>(
                        (responseRowSet.getMetaFields().getMetaFields()));
                for (MetaField metaField : responseFields) {
                    if (getResponseFields().contains(metaField)) {
                        responseRowSet.addDataField(metaField,
                                metaField.getName() + "-value", i);
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "TestProvider(" + getMandatoryRequestFields() + "/"
                    + getOptionalRequestFields() + ") => "
                    + getResponseFields();
        }

    }

    public static class TestArrayProvider extends BaseProvider {
        public TestArrayProvider(String[] requestFields,
                String[] optionalFields, String... responseFields) {
            super(metaArrayFrom(requestFields), metaArrayFrom(optionalFields),
                    metaArrayFrom(responseFields));
        }

        @Override
        public void produce(final DataFieldRowSet requestRowSet,
                final DataFieldRowSet responseRowSet,
                final DataConfiguration config) throws DataProviderException {
            for (int i = 0; i < requestRowSet.size(); i++) {
                for (MetaField metaField : getMandatoryRequestFields()
                        .getMetaFields()) {
                    requestRowSet.getValueAsTextArray(metaField, i);
                }
                Collection<MetaField> responseFields = new ArrayList<>(
                        (responseRowSet.getMetaFields().getMetaFields()));
                for (MetaField metaField : responseFields) {
                    if (getResponseFields().contains(metaField)) {
                        responseRowSet
                                .addDataField(
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

        @Override
        public String toString() {
            return "TestProvider(" + getMandatoryRequestFields() + "/"
                    + getOptionalRequestFields() + ") => "
                    + getResponseFields();
        }

    }

    private final DataProvidersImpl dataProviders = new DataProvidersImpl();
    private final DataConfiguration config = new DataConfiguration();

    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
        LogManager.getRootLogger().setLevel(Level.INFO);
        MetaFieldFactory.reset();
        dataProviders.register(new TestProvider(new String[] { "X" },
                new String[] {}, "B", "F"));
        dataProviders.register(new TestProvider(new String[] { "A" },
                new String[] {}, "B", "C", "D"));
        dataProviders.register(new TestProvider(new String[] { "B", "D" },
                new String[] {}, "E", "F", "G"));
        dataProviders.register(new TestProvider(new String[] { "B", "F" },
                new String[] {}, "H", "I", "J"));
        dataProviders.register(new TestProvider(new String[] { "L", "M" },
                new String[] {}, "N", "O", "P"));
        dataProviders.register(new TestProvider(new String[] { "B", "F", "L" },
                new String[] {}, "Q", "R", "S"));
        dataProviders.register(new TestProvider(new String[] { "H", "N" },
                new String[] {}, "V", "W", "X"));
        dataProviders.register(new TestProvider(new String[] { "L", "H" },
                new String[] {}, "V", "W"));
        dataProviders.register(new TestProvider(new String[] { "F" },
                new String[] {}, "G"));
        dataProviders.register(new TestProvider(new String[] { "V", "W" },
                new String[] {}, "Z"));
    }

    @Test
    public void testGetDataProviders() {
        String[] datapoints = { "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "O", "P", "Q", "R", "S", "V", "W", "X", "Z" };
        for (String dp : datapoints) {
            Collection<DataProvider> providers = dataProviders
                    .getDataProviders(metaFrom("A", "K", "L", "M"),
                            metaFrom(dp));
            assertTrue(providers.size() > 0);
        }
    }

    @Test(expected = DataProviderException.class)
    public void testGetUnknownDataProviders() {
        dataProviders.getDataProviders(metaFrom("A", "K", "L", "M"),
                metaFrom("Y"));
    }

    @Test
    public void testGetMissingMetaFields() {
        MetaFields first = metaFrom("A", "B", "C");
        MetaFields second = metaFrom("A", "B", "C");
        MetaFields third = metaFrom("A", "B", "C", "D");
        MetaFields fourth = metaFrom("X", "Y");

        assert first.containsAll(second);
        assert first.contains(second.getMetaFields().iterator().next());
        assert first.getMissingCount(second) == 0;
        assert first.getMissingCount(third) == 1;
        assert first.getMissingCount(fourth) == 2;
        assert second.getMissingCount(third) == 1;

        assertEquals(0, first.getMissingMetaFields(second).size());
        assertEquals(1, first.getMissingMetaFields(third).size());
        assertEquals(2, first.getMissingMetaFields(fourth).size());
        assertEquals(1, second.getMissingMetaFields(third).size());
    }

    @Test
    public void testProduceOptional() {
        dataProviders.register(new OptionalProvider(new String[] { "input1" },
                new String[] {}, "output1a", "output1b", "optional1"));
        dataProviders.register(new OptionalProvider(
                new String[] { "output1b" }, new String[] { "optional1" },
                "output2a", "output2b"));

        DataFieldRowSet request = rowsetFrom(true, "input1");
        DataFieldRowSet response = rowsetFrom(false, "output2a");
        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(0, errors.size());
        assertEquals(1, response.size());
        assertEquals("output2a-value",
                response.getValueAsText(MetaFieldFactory.lookup("output2a"), 0));
        assertEquals("optional1-value", response.getValueAsText(
                MetaFieldFactory.lookup("optional1"), 0));
    }

    @Test
    public void testProduceOptionalNotAvailable() {
        dataProviders.register(new OptionalProvider(new String[] { "input1" },
                new String[] {}, "output1a", "output1b"));
        dataProviders.register(new TestProvider(new String[] { "output1b" },
                new String[] { "optional1" }, "output2a", "output2b"));

        DataFieldRowSet request = rowsetFrom(true, "input1");
        DataFieldRowSet response = rowsetFrom(false, "output2a");
        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(0, errors.size());
        assertEquals(1, response.size());
        assertEquals("output2a-value",
                response.getValueAsText(MetaFieldFactory.lookup("output2a"), 0));
        assertFalse(response.hasFieldValue(
                MetaFieldFactory.lookup("optional1"), 0));
    }

    @Test
    public void testProduce() {
        long started = System.currentTimeMillis();
        DataFieldRowSet request = rowsetFrom(true, "A");
        DataFieldRowSet response = rowsetFrom(false, "E", "F", "H");
        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(0, errors.size());
        assertEquals(1, response.size());
        assertEquals("E-value",
                response.getValueAsText(MetaFieldFactory.lookup("E"), 0));
        assertEquals("F-value",
                response.getValueAsText(MetaFieldFactory.lookup("F"), 0));
        assertEquals("H-value",
                response.getValueAsText(MetaFieldFactory.lookup("H"), 0));
        long elapsed = System.currentTimeMillis() - started;
        assertTrue(elapsed < 100);
    }

    @Test
    public void testProduceArray() {
        dataProviders.register(new TestArrayProvider(new String[] { "uid" },
                new String[] {}, "uname", "acctids"));
        dataProviders.register(new TestArrayProvider(
                new String[] { "acctids" }, new String[] {}, "actname",
                "accttype"));
        dataProviders.register(new TestArrayProvider(new String[] { "search" },
                new String[] {}, "symbol", "company", "instrumentId"));
        dataProviders.register(new TestArrayProvider(new String[] {
                "instrumentId", "symbol" }, new String[] {}, "positionCount",
                "orderCount"));

        long started = System.currentTimeMillis();
        DataFieldRowSet request = rowsetArrayFrom(true, "uid", "search");
        DataFieldRowSet response = rowsetArrayFrom(false, "uname", "symbol",
                "positionCount");

        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(0, errors.size());
        assertEquals(1, response.size());
        assertEquals("uname-array-value1", response.getValueAsTextArray(
                MetaFieldFactory.lookup("uname"), 0)[0]);
        assertEquals("uname-array-value2", response.getValueAsTextArray(
                MetaFieldFactory.lookup("uname"), 0)[1]);
        assertEquals("symbol-array-value1", response.getValueAsTextArray(
                MetaFieldFactory.lookup("symbol"), 0)[0]);
        assertEquals("symbol-array-value2", response.getValueAsTextArray(
                MetaFieldFactory.lookup("symbol"), 0)[1]);
        assertEquals(
                "positionCount-array-value1",
                response.getValueAsTextArray(
                        MetaFieldFactory.lookup("positionCount"), 0)[0]);
        assertEquals(
                "positionCount-array-value2",
                response.getValueAsTextArray(
                        MetaFieldFactory.lookup("positionCount"), 0)[1]);
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
        Collection<DataProvider> providers = dataProviders.getDataProviders(
                metaFrom("A"), metaFrom("E", "F", "H"));
        assertEquals(3, providers.size());
    }

    @Test
    public void testSingleProvider() {
        TestProvider provider1 = new TestProvider(new String[] { "a" },
                new String[] {}, "b");
        provider1.setTaskGranularity(TaskGranularity.FINE);
        dataProviders.register(provider1);

        DataFieldRowSet request = rowsetFrom(true, "a");
        DataFieldRowSet response = rowsetFrom(false, "b");

        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(0, errors.size());

        assertEquals(1, response.size());
        assertEquals("b-value",
                response.getValueAsText(MetaFieldFactory.lookup("b"), 0));
    }

    @Test
    public void testUnregister() {
        DataProvider provider1 = new TestProvider(new String[] { "search" },
                new String[] {}, "symbols");
        DataProvider provider2 = new TestProvider(new String[] { "symbols" },
                new String[] {}, "quotes");
        DataProvider provider3 = new TestProvider(new String[] { "symbols" },
                new String[] {}, "research");
        //
        dataProviders.unregister(provider1);

        dataProviders.register(provider1);
        dataProviders.register(provider2);
        dataProviders.register(provider3);

        DataFieldRowSet request = rowsetFrom(true, "search");
        DataFieldRowSet response = rowsetFrom(false, "quotes", "research");
        config.setQueryTimeoutMillis(10);

        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(0, errors.size());
        assertEquals(1, response.size());
        assertEquals("quotes-value",
                response.getValueAsText(MetaFieldFactory.lookup("quotes"), 0));
        assertEquals("research-value",
                response.getValueAsText(MetaFieldFactory.lookup("research"), 0));

        request = rowsetFrom(true, "search");
        response = rowsetFrom(false, "quotes", "research");
        try {
            dataProviders.unregister(provider2);
            dataProviders.unregister(provider3);
            dataProviders.produce(request, response, config);
            fail("Should have failed");
        } catch (DataProviderException e) {

        }
    }

    @Test(expected = DataProviderException.class)
    public void testTimeoutFine() {
        TimeoutProvider provider = new TimeoutProvider(200, "timeout");
        provider.setTaskGranularity(TaskGranularity.FINE);
        dataProviders.register(provider);
        dataProviders.register(new TestProvider(new String[] { "a" },
                new String[] {}, "b"));
        dataProviders.register(new TestProvider(new String[] { "b" },
                new String[] {}, "d", "e"));

        DataFieldRowSet request = rowsetFrom(true, "timeout", "a");
        DataFieldRowSet response = rowsetFrom(false, "d", "e", "timeout-result");
        config.setQueryTimeoutMillis(10);

        dataProviders.produce(request, response, config);
    }

    @Test(expected = DataProviderException.class)
    public void testTimeoutCoarse() {
        TimeoutProvider provider = new TimeoutProvider(200, "timeout");
        provider.setTaskGranularity(TaskGranularity.COARSE);
        dataProviders.register(provider);
        dataProviders.register(new TestProvider(new String[] { "a" },
                new String[] {}, "b"));
        dataProviders.register(new TestProvider(new String[] { "b" },
                new String[] {}, "d", "e"));

        DataFieldRowSet request = rowsetFrom(true, "timeout", "a");
        DataFieldRowSet response = rowsetFrom(false, "d", "e", "timeout-result");
        config.setQueryTimeoutMillis(10);
        final ExecutorService defaultExecutor = Executors.newFixedThreadPool(5);
        dataProviders.setDefaultExecutor(defaultExecutor);
        assertEquals(defaultExecutor, dataProviders.getDefaultExecutor());
        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(0, errors.size());
    }

    @Test
    public void testError() {
        FailingProvider provider = new FailingProvider("error",
                new RuntimeException("failed"));
        provider.setTaskGranularity(TaskGranularity.COARSE);
        dataProviders.register(provider);
        dataProviders.register(new TestProvider(new String[] { "query" },
                new String[] {}, "lookupResults"));
        dataProviders.register(new TestProvider(
                new String[] { "lookupResults" }, new String[] {},
                "detailsData"));

        DataFieldRowSet request = rowsetFrom(true, "error", "query");
        DataFieldRowSet response = rowsetFrom(false, "lookupResults",
                "detailsData", "error-result");

        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(1, errors.size());
    }

    @Test
    public void testErrorWithAbort() {
        FailingProvider provider = new FailingProvider("error",
                new RuntimeException("failed"));
        provider.setTaskGranularity(TaskGranularity.COARSE);
        dataProviders.register(provider);
        dataProviders.register(new TestProvider(new String[] { "query" },
                new String[] {}, "lookupResults"));
        dataProviders.register(new TestProvider(
                new String[] { "lookupResults" }, new String[] {},
                "detailsData"));

        DataFieldRowSet request = rowsetFrom(true, "error", "query");
        DataFieldRowSet response = rowsetFrom(false, "lookupResults",
                "detailsData", "error-result");
        config.setAbortUponPartialFailure(true);
        Map<DataProvider, Throwable> errors = dataProviders.produce(request,
                response, config);
        assertEquals(1, errors.size());
    }

    static MetaFields metaFrom(String... args) {
        MetaFields metaFields = new MetaFields();
        for (String arg : args) {
            metaFields.addMetaField(MetaFieldFactory.create(arg,
                    MetaFieldType.TEXT));
        }
        return metaFields;
    }

    static MetaFields metaArrayFrom(String... args) {
        MetaFields metaFields = new MetaFields();
        for (String arg : args) {
            metaFields.addMetaField(MetaFieldFactory.create(arg,
                    MetaFieldType.ARRAY_TEXT));
        }
        return metaFields;
    }

    static DataFieldRowSet rowsetFrom(boolean addData, String... args) {
        MetaFields metaFields = metaFrom(args);
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        if (addData) {
            DataFieldRow row = new DataFieldRow();
            for (String arg : args) {
                row.addField(MetaFieldFactory.create(arg, MetaFieldType.TEXT),
                        arg + "-input");
            }
            rowset.addRow(row);
        }
        return rowset;
    }

    static DataFieldRowSet rowsetArrayFrom(boolean addData, String... args) {
        MetaFields metaFields = metaArrayFrom(args);
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        if (addData) {
            for (int i = 0; i < args.length; i++) {
                rowset.addDataField(MetaFieldFactory.create(args[i],
                        MetaFieldType.ARRAY_TEXT), Collections
                        .singleton(args[i]), 0);
            }
        }
        return rowset;
    }
}
