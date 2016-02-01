package com.plexobject.dp.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DataRowSetTest {
    private Metadata metaFields;

    @Before
    public void setup() {
        MetaFieldFactory.reset();
        metaFields = Metadata.fromRaw("text", "SCALAR_TEXT", "name",
                "SCALAR_TEXT");
    }

    @Test
    public void testCreate() {
        DataRowSet rowset = new DataRowSet(metaFields);
        assertEquals(0, rowset.size());
    }

    @Test
    public void testCreateRowSets() {
        DataRow row = DataRow.from(MetaFieldFactory.lookup("text"),
                "hello there", MetaFieldFactory.lookup("name"), "Jake");
        DataRowSet rowset = new DataRowSet(metaFields, row);
        assertEquals(1, rowset.size());
        assertTrue(rowset.toString().contains("Jake"));
    }

    @Test
    public void testCreateRowSetsCollection() {
        DataRow row = DataRow.from(MetaFieldFactory.lookup("text"),
                "hello there", MetaFieldFactory.lookup("name"), "Jake");
        DataRowSet rowset = new DataRowSet(metaFields, Arrays.asList(row));
        assertEquals(1, rowset.size());
        assertEquals(metaFields, rowset.getMetadata());
    }

    @Test
    public void testAddValueAtRow() {
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake2", 1);
        assertEquals(2, rowset.size());
        assertEquals(2, rowset.getRows().size());
    }

    @Test
    public void testHasFieldValue() {
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake2", 1);
        assertTrue(rowset.hasFieldValue(MetaFieldFactory.lookup("name"), 0));
        assertFalse(rowset.hasFieldValue(MetaFieldFactory.lookup("name"), 2));
        assertFalse(rowset.hasFieldValue(MetaFieldFactory.lookup("text"), 0));
    }

    @Test
    public void testGetFieldValue() {
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake2", 1);
        assertEquals("jake1",
                rowset.getValue(MetaFieldFactory.lookup("name"), 0));
        assertEquals("jake2",
                rowset.getValue(MetaFieldFactory.lookup("name"), 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldValueError() {
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake1", 0);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake2", 1);
        rowset.getValue(MetaFieldFactory.lookup("name"), 2);
    }

    @Test
    public void testGetValueAsText() {
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("name"), "jake1", 0);
        assertEquals("jake1",
                rowset.getValueAsText(MetaFieldFactory.lookup("name"), 0));
    }

    @Test
    public void testGetValueAsLong() {
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.SCALAR_INTEGER));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), 100L, 0);
        assertEquals(100L,
                rowset.getValueAsLong(MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsBoolean() {
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.SCALAR_BOOLEAN));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), true, 0);
        assertTrue(rowset.getValueAsBoolean(MetaFieldFactory.lookup("object"),
                0));
    }

    @Test
    public void testGetValueAsDecimal() {
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.SCALAR_DECIMAL));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), 1.1, 0);
        assertEquals(1.1,
                rowset.getValueAsDecimal(MetaFieldFactory.lookup("object"), 0),
                0.0001);
    }

    @Test
    public void testGetValueAsBinary() {
        String hello = "hello";
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.SCALAR_TEXT));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"),
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
                MetaFieldType.SCALAR_DATE));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), date, 0);
        assertEquals(date,
                rowset.getValueAsDate(MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsBooleanVector() {
        boolean[] values = { true, false };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.VECTOR_BOOLEAN));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertEquals(
                Arrays.toString(values),
                Arrays.toString(rowset.getValueAsBooleanVector(
                        MetaFieldFactory.lookup("object"), 0)));
    }

    @Test
    public void testGetValueAsLongVector() {
        long[] values = { 1, 2, 3 };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.VECTOR_INTEGER));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertEquals(
                Arrays.toString(values),
                Arrays.toString(rowset.getValueAsLongVector(
                        MetaFieldFactory.lookup("object"), 0)));
    }

    @Test
    public void testGetValueAsDecimalVector() {
        double[] values = { 1.1, 2.1, 3.1 };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.VECTOR_DECIMAL));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertEquals(
                Arrays.toString(values),
                Arrays.toString(rowset.getValueAsDecimalVector(
                        MetaFieldFactory.lookup("object"), 0)));
    }

    @Test
    public void testGetValueAsDateVector() {
        Date[] values = { new Date(0), new Date(1) };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.VECTOR_DATE));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertArrayEquals(values, rowset.getValueAsDateVector(
                MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsTextVector() {
        String[] values = { "one", "two" };
        metaFields.addMetaField(MetaFieldFactory.create("object",
                MetaFieldType.VECTOR_DATE));
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertArrayEquals(values, rowset.getValueAsTextVector(
                MetaFieldFactory.lookup("object"), 0));
    }

}
