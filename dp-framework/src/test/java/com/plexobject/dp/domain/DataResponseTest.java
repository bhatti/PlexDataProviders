package com.plexobject.dp.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class DataResponseTest {

    @Test
    public void testCreateDefault() {
        DataResponse resp = new DataResponse();
        assertNull(resp.getFields());
    }

    @Test
    public void testCreate() {
        MetaField field1 = MetaFieldFactory.create("obj1", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        DataRowSet fields = new DataRowSet(Metadata.from(field1));
        Set<String> providers = new HashSet<>();
        Map<String, Throwable> errors = new HashMap<>();
        DataResponse resp = new DataResponse(fields, providers, errors);
        assertEquals(fields, resp.getFields());
    }

    @Test
    public void testGetSetFields() {
        DataResponse resp = new DataResponse();
        MetaField field1 = MetaFieldFactory.create("obj1", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        DataRowSet fields = new DataRowSet(Metadata.from(field1));
        resp.setFields(fields);
        assertEquals(fields, resp.getFields());
    }

    @Test
    public void testGetSetErrorsByProviderName() {
        DataResponse resp = new DataResponse();
        Map<String, Throwable> errors = new HashMap<>();
        resp.setErrorsByProviderName(errors);
        assertEquals(errors, resp.getErrorsByProviderName());
    }

    @Test
    public void testGetSetProviders() {
        DataResponse resp = new DataResponse();
        Set<String> providers = new HashSet<>();
        resp.setProviders(providers);
        assertEquals(providers, resp.getProviders());
    }
}
