package com.plexobject.dp.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

public class DataConfigurationTest {
    private DataConfiguration config = new DataConfiguration();

    @Test
    public void testGetSetPage() {
        config.setPage(1);
        assertEquals(1, config.getPage());
    }

    @Test
    public void testGetSetLimit() {
        config.setLimit(100);
        assertEquals(100, config.getLimit());
    }

    @Test
    public void testGetSetetQueryTimeoutMillis() {
        config.setQueryTimeoutMillis(100);
        assertEquals(100, config.getQueryTimeoutMillis());
    }

    @Test
    public void testGetSetOrderBy() {
        config.setOrderBy("name");
        assertEquals("name", config.getOrderBy());
    }

    @Test
    public void testGetSetGroupBy() {
        config.setGroupBy("name");
        assertEquals("name", config.getGroupBy());
    }

    @Test
    public void testGetSetMaxThreads() {
        config.setMaxThreads(100);
        assertEquals(100, config.getMaxThreads());
    }

    @Test
    public void testGetSetFilterCriteria() {
        config.addFilterCriteria("name", "jake");
        assertEquals("jake", config.getFilterCriteria().get("name"));
        config.setFilterCriteria(new HashMap<String, Object>());
        assertEquals(0, config.getFilterCriteria().size());
    }

    @Test
    public void testGetSetAbortUponPartialFailure() {
        config.setAbortUponPartialFailure(true);
        assertTrue(config.isAbortUponPartialFailure());
    }
}
