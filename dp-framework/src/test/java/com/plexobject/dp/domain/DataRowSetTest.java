package com.plexobject.dp.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DataRowSetTest {
    private Metadata metaFields;
    private MetaField textMeta;
    private MetaField nameMeta;

    @Before
    public void setup() {
        MetaFieldFactory.reset();
        textMeta = MetaFieldFactory.create("text", "Test",
                MetaFieldType.SCALAR_TEXT, true);
        nameMeta = MetaFieldFactory.create("name", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        metaFields = Metadata.from(textMeta, nameMeta);
    }

    @Test
    public void testCreate() {
        DataRowSet rowset = new DataRowSet(metaFields);
        assertEquals(0, rowset.size());
    }

    @Test
    public void testCreateRowSets() {
        DataRow row = DataRow.from(textMeta, "hello there", nameMeta, "Jake");
        DataRowSet rowset = new DataRowSet(metaFields, row);
        assertEquals(1, rowset.size());
        assertTrue(rowset.toString().contains("Jake"));
    }

    @Test
    public void testCreateRowSetsCollection() {
        DataRow row = DataRow.from(textMeta, "hello there", nameMeta, "Jake");
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
    public void testGetFieldValueAsRowSet() {
        MetaField field1 = MetaFieldFactory.create("obj1", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        MetaField field2 = MetaFieldFactory.create("obj2", "Test",
                MetaFieldType.ROWSET, false);
        DataRowSet rowset1 = new DataRowSet(Metadata.from(field1));
        rowset1.addValueAtRow(MetaFieldFactory.lookup("obj1"), "value", 0);
        //
        DataRowSet rowset2 = new DataRowSet(Metadata.from(field2));
        rowset2.addValueAtRow(MetaFieldFactory.lookup("obj2"), rowset1, 0);
        rowset2.getValueAsRowSet(MetaFieldFactory.lookup("obj2"), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldValueAsRowSetError() {
        MetaField field0 = MetaFieldFactory.create("obj0", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        MetaField field1 = MetaFieldFactory.create("obj1", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        MetaField field2 = MetaFieldFactory.create("obj2", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        DataRowSet rowset1 = new DataRowSet(Metadata.from(field1));
        rowset1.addValueAtRow(MetaFieldFactory.lookup("obj1"), "value", 0);
        //
        DataRowSet rowset2 = new DataRowSet(Metadata.from(field0, field2));
        rowset2.addValueAtRow(MetaFieldFactory.lookup("obj0"), "xxx", 0);
        rowset2.addValueAtRow(MetaFieldFactory.lookup("obj2"), rowset1, 0);
        rowset2.getValueAsRowSet(MetaFieldFactory.lookup("obj0"), 0);
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
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.SCALAR_INTEGER, false);

        metaFields.addMetaField(field1);
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), 100L, 0);
        assertEquals(100L,
                rowset.getValueAsLong(MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsBoolean() {
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.SCALAR_BOOLEAN, false);
        metaFields.addMetaField(field1);
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), true, 0);
        assertTrue(rowset.getValueAsBoolean(MetaFieldFactory.lookup("object"),
                0));
    }

    @Test
    public void testGetValueAsDecimal() {
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.SCALAR_DECIMAL, false);
        metaFields.addMetaField(field1);

        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), 1.1, 0);
        assertEquals(1.1,
                rowset.getValueAsDecimal(MetaFieldFactory.lookup("object"), 0),
                0.0001);
    }

    @Test
    public void testGetValueAsBinary() {
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.BINARY, false);
        metaFields.addMetaField(field1);

        String hello = "hello";
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
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.SCALAR_DATE, false);
        metaFields.addMetaField(field1);

        Date date = new Date();
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), date, 0);
        assertEquals(date,
                rowset.getValueAsDate(MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsBooleanVector() {
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.VECTOR_BOOLEAN, false);
        metaFields.addMetaField(field1);

        boolean[] values = { true, false };
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertEquals(
                Arrays.toString(values),
                Arrays.toString(rowset.getValueAsBooleanVector(
                        MetaFieldFactory.lookup("object"), 0)));
    }

    @Test
    public void testGetValueAsLongVector() {
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.VECTOR_INTEGER, false);
        metaFields.addMetaField(field1);

        long[] values = { 1, 2, 3 };
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertEquals(
                Arrays.toString(values),
                Arrays.toString(rowset.getValueAsLongVector(
                        MetaFieldFactory.lookup("object"), 0)));
    }

    @Test
    public void testGetValueAsDecimalVector() {
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.VECTOR_DECIMAL, false);
        metaFields.addMetaField(field1);

        double[] values = { 1.1, 2.1, 3.1 };
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
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.VECTOR_DATE, false);
        metaFields.addMetaField(field1);

        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertArrayEquals(values, rowset.getValueAsDateVector(
                MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsTextVector() {
        MetaField field1 = MetaFieldFactory.create("object", "Test",
                MetaFieldType.VECTOR_TEXT, false);
        metaFields.addMetaField(field1);

        String[] values = { "one", "two" };
        DataRowSet rowset = new DataRowSet(metaFields);
        rowset.addValueAtRow(MetaFieldFactory.lookup("object"), values, 0);
        assertArrayEquals(values, rowset.getValueAsTextVector(
                MetaFieldFactory.lookup("object"), 0));
    }

    @Test
    public void testGetValueAsTextKeyField() {
        DataRowSet rowset = new DataRowSet(metaFields);
        MetaField field1 = MetaFieldFactory.create("text", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        MetaField field2 = MetaFieldFactory.create("num", "Test",
                MetaFieldType.SCALAR_INTEGER, false);

        rowset.addValueAtRow(field1, "jake1", 0);
        rowset.addValueAtRow(field2, 1L, 0);
        DataRow row = rowset.getRowForKeyField(MetaFieldFactory.lookup("text"),
                "jake1");
        assertEquals("jake1",
                row.getValueAsText(MetaFieldFactory.lookup("text")));
        assertEquals(1L, row.getValueAsLong(MetaFieldFactory.lookup("num")));
    }

    @Test(expected=IllegalArgumentException.class)
    public void testGetValueAsTextKeyFieldNonexistant() {
        DataRowSet rowset = new DataRowSet(metaFields);
        MetaField field1 = MetaFieldFactory.create("text", "Test",
                MetaFieldType.SCALAR_TEXT, false);
        MetaField field2 = MetaFieldFactory.create("num", "Test",
                MetaFieldType.SCALAR_INTEGER, false);

        rowset.addValueAtRow(field1, "jake1", 0);
        rowset.addValueAtRow(field2, 1L, 0);
        assertNull(rowset.getRowForKeyField(MetaFieldFactory.lookup("text"), "jake2"));
    }

    @Test
    public void testMerge() {
        Metadata metadata1 = Metadata.from(textMeta);
        Metadata metadata2 = Metadata.from(nameMeta);
        DataRowSet rowset1 = new DataRowSet(metadata1);
        DataRowSet rowset2 = new DataRowSet(metadata2);
        rowset1.addValueAtRow(textMeta, "text1", 0);
        rowset2.addValueAtRow(nameMeta, "name1", 0);
        assertEquals(1, rowset1.getRows().size());
        assertEquals(1, rowset2.getRows().size());
        rowset1.merge(rowset2);
        assertEquals(2, rowset1.getRows().size());
        assertEquals(1, rowset2.getRows().size());
    }
}
