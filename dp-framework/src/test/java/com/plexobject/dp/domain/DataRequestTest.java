package com.plexobject.dp.domain;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class DataRequestTest {
    @Test
    public void testCreate() {
        DataRequest req = create();
        assertEquals(1, req.getParameters().size());
    }

    @Test
    public void testGetSetParameters() {
        MetaField field1 = MetaFieldFactory.createText("name", "Test", false);
        MetaField field2 = MetaFieldFactory.createText("phone", "Test", false);
        Metadata fields = new Metadata(Arrays.asList(field1, field2));
        DataRequest req = create();
        DataRowSet rowset = new DataRowSet(fields);
        req.setParameters(rowset);
        assertEquals(rowset, req.getParameters());
        assertEquals(2, req.getFields().size());
    }

    @Test
    public void testGetSetFields() {
        MetaField field1 = MetaFieldFactory.createText("name", "Test", false);
        MetaField field2 = MetaFieldFactory.createText("phone", "Test", false);
        Metadata fields = new Metadata(Arrays.asList(field1, field2));
        DataRequest req = create();
        req.setFields(fields);
        assertEquals(fields, req.getFields());
    }

    @Test
    public void testGetSetConfig() {
        QueryConfiguration config = new QueryConfiguration();
        DataRequest req = new DataRequest();
        req.setConfig(config);
        assertEquals(config, req.getConfig());
    }

    private DataRequest create() {
        MetaFieldFactory.createText("name", "Test", false);
        MetaFieldFactory.createText("field1", "Test", false);
        MetaFieldFactory.createText("field2", "Test", false);
        Map<String, Object> args = new HashMap<>();
        args.put(DataRequest.RESPONSE_FIELDS, "field1,field2");
        args.put("name", "value1");
        args.put("unknown", "value1");
        DataRequest req = DataRequest.from(args);
        return req;
    }
}
