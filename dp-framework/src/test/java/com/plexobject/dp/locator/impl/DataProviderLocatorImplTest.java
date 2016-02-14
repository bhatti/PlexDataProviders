package com.plexobject.dp.locator.impl;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;

import com.plexobject.dp.domain.DataRowSet;
import com.plexobject.dp.domain.MetaFieldFactory;
import com.plexobject.dp.domain.Metadata;
import com.plexobject.dp.domain.QueryConfiguration;
import com.plexobject.dp.provider.BaseProvider;
import com.plexobject.dp.provider.DataProvider;
import com.plexobject.dp.provider.DataProviderException;

public class DataProviderLocatorImplTest {
    private DataProviderLocatorImpl locator = new DataProviderLocatorImpl();

    private static class TestProvider extends BaseProvider {
        public TestProvider(String name, Metadata mandatoryRequestMetadata,
                Metadata optionalRequestMetadata, Metadata responseMetadata) {
            super(name, mandatoryRequestMetadata, optionalRequestMetadata,
                    responseMetadata);
        }

        @Override
        public void produce(DataRowSet requestMetadata,
                DataRowSet responseMetadata, QueryConfiguration config)
                throws DataProviderException {
        }

    }

    @Test
    public void testRegisterUnregister() {
        TestProvider provider1 = new TestProvider("provider1", metaFrom("req1",
                "kind1"), metaFrom("opt1", "kind1"), metaFrom("resp1", "resp"));
        TestProvider provider2 = new TestProvider("provider2", metaFrom("req2",
                "kind2"), metaFrom("opt2", "kind2"), metaFrom("resp2", "resp"));
        TestProvider provider3 = new TestProvider("provider3", metaFrom("req3",
                "kind3"), metaFrom("opt3", "kind3"), metaFrom("resp3", "resp"));
        locator.register(provider1);
        locator.register(provider1);
        locator.register(provider2);
        locator.register(provider3);
        assertNotNull(locator.getByName("provider1"));
        assertNotNull(locator.getByName("provider2"));
        assertNotNull(locator.getByName("provider3"));
        assertNull(locator.getByName("provider4"));
        //
        assertEquals(2, locator.getAllWithKinds("kind1", "kind2").size());
        assertEquals(3, locator.getAll().size());
        //
        locator.unregister(provider1);
        assertNull(locator.getByName("provider1"));
        assertNotNull(locator.getByName("provider2"));
        assertNotNull(locator.getByName("provider3"));
        //
        assertEquals(1, locator.getAllWithKinds("kind1", "kind2").size());
        assertEquals(2, locator.getAll().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegisterAgain() {
        TestProvider provider1 = new TestProvider("provider", metaFrom("req1",
                "kind1"), metaFrom("opt1", "kind1"), metaFrom("resp1", "resp"));
        TestProvider provider2 = new TestProvider("provider", metaFrom("req2",
                "kind2"), metaFrom("opt2", "kind2"), metaFrom("resp2", "resp"));
        locator.register(provider1);
        locator.register(provider2);
    }

    @Test
    public void testLocateWithRanking() {
        TestProvider provider1 = new TestProvider("provider1", metaFrom("req1",
                "kind1"), metaFrom("opt1", "kind1"), metaFrom("resp1", "resp"));
        TestProvider provider2 = new TestProvider("provider2", metaFrom("req2",
                "kind2"), metaFrom("opt2", "kind2"), metaFrom("resp2", "resp"));
        TestProvider provider3 = new TestProvider("provider3", metaFrom("req3",
                "kind3"), metaFrom("opt3", "kind3"), metaFrom("resp3", "resp"));
        TestProvider provider4 = new TestProvider("provider4", metaFrom("req3",
                "kind3"), metaFrom("opt3", "kind3"), metaFrom("resp3", "resp"));
        provider4.setRank(10);
        locator.register(provider1);
        locator.register(provider2);
        locator.register(provider3);
        locator.register(provider4);
        Metadata reqMeta = metaFrom("req1", "kind1");
        reqMeta.addMetaField(MetaFieldFactory.lookup("req2"));
        reqMeta.addMetaField(MetaFieldFactory.lookup("req2"));
        reqMeta.addMetaField(MetaFieldFactory.lookup("req3"));
        Metadata resMeta = metaFrom("resp1", "resp");
        resMeta.addMetaField(MetaFieldFactory.lookup("resp2"));
        resMeta.addMetaField(MetaFieldFactory.lookup("resp3"));
        Collection<DataProvider> providers = locator.locate(reqMeta, resMeta);
        assertEquals(3, providers.size());
        assertTrue(providers.contains(provider1));
        assertTrue(providers.contains(provider2));
        assertTrue(providers.contains(provider4));
    }

    @Test(expected = DataProviderException.class)
    public void testLocateNonexistant() {
        TestProvider provider1 = new TestProvider("provider1", metaFrom("req1",
                "input"), metaFrom("opt1", "opt"), metaFrom("resp1", "resp"));
        locator.register(provider1);
        Metadata reqMeta = metaFrom("req1", "input");
        Metadata resMeta = metaFrom("resp2", "resp");
        locator.locate(reqMeta, resMeta);
    }

    @Test
    public void testLocateSingleDependency() {
        TestProvider provider1 = new TestProvider("provider1", metaFrom("req1",
                "input"), metaFrom(), metaFrom("resp1a", "resp", "resp1b",
                "resp"));
        TestProvider provider2 = new TestProvider("provider2", metaFrom(
                "resp1a", "input"), metaFrom(), metaFrom("resp2a", "resp",
                "resp2b", "resp"));
        TestProvider provider3 = new TestProvider("provider3", metaFrom(
                "resp2a", "input"), metaFrom(), metaFrom("resp3", "resp"));
        locator.register(provider1);
        locator.register(provider2);
        locator.register(provider3);
        Metadata reqMeta = metaFrom("req1", "input");
        Metadata resMeta = metaFrom("resp2a", "resp", "resp1b", "resp",
                "resp2b", "resp");
        Collection<DataProvider> providers = locator.locate(reqMeta, resMeta);
        assertEquals(2, providers.size());
        assertTrue(providers.contains(provider1));
        assertTrue(providers.contains(provider2));
    }

    @Test
    public void testLocateMultipleDependencies() {
        TestProvider provider1 = new TestProvider("provider1", metaFrom("req1",
                "input"), metaFrom("opt1", "opt"), metaFrom("resp1", "resp"));
        TestProvider provider2 = new TestProvider("provider2", metaFrom(
                "resp1", "input"), metaFrom("opt2", "opt"), metaFrom("resp2",
                "resp"));
        TestProvider provider3 = new TestProvider("provider3", metaFrom(
                "resp2", "input"), metaFrom("opt3", "opt"), metaFrom("resp3",
                "resp"));
        locator.register(provider1);
        locator.register(provider2);
        locator.register(provider3);
        Metadata reqMeta = metaFrom("req1", "input");
        Metadata resMeta = metaFrom("resp3", "resp");
        Collection<DataProvider> providers = locator.locate(reqMeta, resMeta);
        assertEquals(3, providers.size());
        assertTrue(providers.contains(provider1));
        assertTrue(providers.contains(provider2));
        assertTrue(providers.contains(provider3));
    }

    static Metadata metaFrom(String... args) {
        Metadata metaFields = new Metadata();
        for (int i = 0; i < args.length; i += 2) {
            metaFields.addMetaField(MetaFieldFactory.createText(args[i],
                    args[i + 1], false));
        }
        return metaFields;
    }

}
