package com.plexobject.dp.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DataFieldRowSetTest {
    private MetaFields metaFields;

    @Before
    public void setup() {
        MetaFieldFactory.reset();
        metaFields = MetaFields.fromRaw("text", "TEXT", "name", "TEXT");
    }

    @Test
    public void testCreate() {
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        assertEquals(0, rowset.size());
    }

    @Test
    public void testCreateRowSets() {
        DataFieldRow row = DataFieldRow.from(MetaFieldFactory.lookup("text"),
                "hello there", MetaFieldFactory.lookup("name"), "Jake");
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields, row);
        assertEquals(1, rowset.size());
        assertTrue(rowset.toString().contains("Jake"));
    }

    @Test
    public void testCreateRowSetsCollection() {
        DataFieldRow row = DataFieldRow.from(MetaFieldFactory.lookup("text"),
                "hello there", MetaFieldFactory.lookup("name"), "Jake");
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields,
                Arrays.asList(row));
        assertEquals(1, rowset.size());
        assertEquals(metaFields, rowset.getMetaFields());
    }

    @Test
    public void testAddDataField() {
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake2", 1);
        assertEquals(2, rowset.size());
        assertEquals(2, rowset.getRows().size());
    }

    @Test
    public void testHasFieldValue() {
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake2", 1);
        assertTrue(rowset.hasFieldValue(MetaFieldFactory.lookup("name"), 0));
        assertFalse(rowset.hasFieldValue(MetaFieldFactory.lookup("name"), 2));
        assertFalse(rowset.hasFieldValue(MetaFieldFactory.lookup("text"), 0));
    }

    @Test
    public void testGetFieldValue() {
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake2", 1);
        assertEquals("jake1",
                rowset.getFieldValue(MetaFieldFactory.lookup("name"), 0));
        assertEquals("jake2",
                rowset.getFieldValue(MetaFieldFactory.lookup("name"), 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldValueError() {
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake2", 1);
        rowset.getFieldValue(MetaFieldFactory.lookup("name"), 2);
    }

    @Test
    public void testGetValueAsText() {
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("name"), "jake1", 0);
        assertEquals("jake1",
                rowset.getValueAsText(MetaFieldFactory.lookup("name"), 0));
    }

    @Test
    public void testGetValueAsLong() {
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.INTEGER));
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("object"), 100L, 0);
        assertEquals(100L,
                rowset.getValueAsLong(MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsDecimal() {
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.DECIMAL));
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("object"), 1.1, 0);
        assertEquals(1.1,
                rowset.getValueAsDecimal(MetaFieldFactory.lookup("object"), 0),
                0.0001);
    }

    @Test
    public void testGetValueAsBinary() {
        String hello = "hello";
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.TEXT));
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("object"),
                hello.getBytes(), 0);
        assertEquals(
                hello,
                new String(rowset.getValueAsBinary(
                        MetaFieldFactory.lookup("object"), 0)));
    }

    @Test
    public void testGetValueAsDate() {
        Date date = new Date();
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.DATE));
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("object"), date, 0);
        assertEquals(date,
                rowset.getValueAsDate(MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsLongArray() {
        long[] values = { 1, 2, 3 };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.ARRAY_INTEGER));
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("object"), values, 0);
        assertEquals(
                Arrays.toString(values),
                Arrays.toString(rowset.getValueAsLongArray(
                        MetaFieldFactory.lookup("object"), 0)));
    }

    @Test
    public void testGetValueAsDecimalArray() {
        double[] values = { 1.1, 2.1, 3.1 };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.ARRAY_DECIMAL));
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("object"), values, 0);
        assertEquals(
                Arrays.toString(values),
                Arrays.toString(rowset.getValueAsDecimalArray(
                        MetaFieldFactory.lookup("object"), 0)));
    }

    @Test
    public void testGetValueAsDateArray() {
        Date[] values = { new Date(0), new Date(1) };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.ARRAY_DATE));
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("object"), values, 0);
        assertArrayEquals(values, rowset.getValueAsDateArray(
                MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsTextArray() {
        String[] values = { "one", "two" };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.ARRAY_DATE));
        DataFieldRowSet rowset = new DataFieldRowSet(metaFields);
        rowset.addDataField(MetaFieldFactory.lookup("object"), values, 0);
        assertArrayEquals(values, rowset.getValueAsTextArray(
                MetaFieldFactory.lookup("object"), 0));
    }

}
