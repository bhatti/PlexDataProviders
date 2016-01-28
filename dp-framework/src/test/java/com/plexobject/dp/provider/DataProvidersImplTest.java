package com.plexobject.dp.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.provider.DataProviderException;
import com.plexobject.dp.provider.DataProvidersImpl;

public class DataProvidersImplTest {
    public static class TestProvider extends BaseProvider {
        private long sleepMillis;

        public TestProvider(String[] requestFields, String[] optionalFields,
                String... responseFields) {
            this(0, requestFields, optionalFields, responseFields);
        }

        public TestProvider(long sleepMillis, String[] requestFields,
                String[] optionalFields, String... responseFields) {
            super(metaFrom(requestFields), metaFrom(optionalFields),
                    metaFrom(responseFields));
            this.sleepMillis = sleepMillis;
        }

        @Override
        public void produce(final DataFieldRowSet requestRowSet,
                final DataFieldRowSet responseRowSet,
                final DataConfiguration config) throws DataProviderException {
            for (int i = 0; i < requestRowSet.size(); i++) {
                for (MetaField metaField : getMandatoryRequestFields()
                        .getMetaFields()) {
                    requestRowSet.getValueAsText(metaField.getName(), i);
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
            if (sleepMillis > 0) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    Thread.interrupted();
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
                    requestRowSet.getValueAsTextArray(metaField.getName(), i);
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
    public void testProduce() {
        long started = System.currentTimeMillis();
        DataFieldRowSet request = rowsetFrom(true, "A");
        DataFieldRowSet response = rowsetFrom(false, "E", "F", "H");
        dataProviders.produce(request, response, config);
        assertEquals(1, response.size());
        assertEquals("E-value", response.getValueAsText("E", 0));
        assertEquals("F-value", response.getValueAsText("F", 0));
        assertEquals("H-value", response.getValueAsText("H", 0));
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

        dataProviders.produce(request, response, config);
        assertEquals(1, response.size());
        assertEquals("uname-array-value1",
                response.getValueAsTextArray("uname", 0)[0]);
        assertEquals("uname-array-value2",
                response.getValueAsTextArray("uname", 0)[1]);
        assertEquals("symbol-array-value1",
                response.getValueAsTextArray("symbol", 0)[0]);
        assertEquals("symbol-array-value2",
                response.getValueAsTextArray("symbol", 0)[1]);
        assertEquals("positionCount-array-value1",
                response.getValueAsTextArray("positionCount", 0)[0]);
        assertEquals("positionCount-array-value2",
                response.getValueAsTextArray("positionCount", 0)[1]);
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

    @Test(expected = DataProviderException.class)
    public void testTimeout() {
        dataProviders.register(new TestProvider(200, new String[] { "a" },
                new String[] {}, "b", "c"));
        dataProviders.register(new TestProvider(100, new String[] { "b" },
                new String[] {}, "d", "e"));

        DataFieldRowSet request = rowsetFrom(true, "a");
        DataFieldRowSet response = rowsetFrom(false, "c", "d", "e");
        config.setQueryTimeoutMillis(10);

        dataProviders.produce(request, response, config);
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