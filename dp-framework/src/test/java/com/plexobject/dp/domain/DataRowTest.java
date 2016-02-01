package com.plexobject.dp.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DataRowTest {
    private MetaField text;
    private MetaField integer;

    @Before
    public void setup() {
        MetaFieldFactory.reset();
        text = MetaFieldFactory.create("text", MetaFieldType.SCALAR_TEXT);
        integer = MetaFieldFactory.create("integer",
                MetaFieldType.SCALAR_INTEGER);
    }

    @Test
    public void testCreate() {
        DataRow row = new DataRow();
        row.addField(text, null);
        assertEquals(1, row.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueNull() {
        DataRow row = new DataRow();
        row.addField(text, null);
        row.getValue(text);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueInitial() {
        DataRow row = new DataRow();
        row.addField(text, InitialValue.instance);
        row.getValue(text);
    }

    @Test(expected = RuntimeException.class)
    public void testGetValueRuntimeException() {
        DataRow row = new DataRow();
        row.addField(text, new RuntimeException());
        row.getValue(text);
    }

    @Test(expected = RuntimeException.class)
    public void testGetValueException() {
        DataRow row = new DataRow();
        row.addField(text, new Exception());
        row.getValue(text);
    }

    @Test
    public void testHasFieldValueNull() {
        DataRow row = new DataRow();
        row.addField(text, null);
        assertFalse(row.hasFieldValue(text));
    }

    @Test
    public void testHasFieldValueInitial() {
        DataRow row = new DataRow();
        row.addField(text, InitialValue.instance);
        assertFalse(row.hasFieldValue(text));
    }

    @Test
    public void testHasFieldValueRuntimeException() {
        DataRow row = new DataRow();
        row.addField(text, new RuntimeException());
        assertFalse(row.hasFieldValue(text));
    }

    @Test
    public void testHasFieldValueException() {
        DataRow row = new DataRow();
        row.addField(text, new Exception());
        assertFalse(row.hasFieldValue(text));
    }

    @Test
    public void testAddField() {
        DataRow row = new DataRow();
        row.addField(text, "value1");
        row.addField(integer, 1);
        assertEquals(2, row.size());
    }

    @Test
    public void testGetFields() {
        DataRow row = new DataRow();
        row.addField(text, "value1");
        row.addField(integer, 1);
        assertEquals(2, row.size());
        assertEquals("value1", row.getFields().get(text));
        assertEquals(1, row.getFields().get(integer));
    }

    @Test
    public void testHasFieldValue() {
        DataRow row = new DataRow();
        row.addField(text, "value1");
        assertEquals(1, row.size());
        assertTrue(row.hasFieldValue(text));
    }

    @Test
    public void testGetValue() {
        DataRow row = new DataRow();
        row.addField(text, "value1");
        assertEquals("value1", row.getValue(text));
    }

    @Test
    public void testGetValueAsText() {
        DataRow row = new DataRow();
        row.addField(text, "value1");
        assertEquals("value1", row.getValueAsText(text));
    }

    @Test
    public void testGetValueAsLong() {
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.SCALAR_INTEGER);
        row.addField(field, 1);
        assertEquals(1L, row.getValueAsLong(field));
    }

    @Test
    public void testGetValueAsBoolean() {
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.SCALAR_BOOLEAN);
        row.addField(field, 1);
        assertTrue(row.getValueAsBoolean(field));
    }

    @Test
    public void testGetValueAsDecimal() {
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.SCALAR_DECIMAL);
        row.addField(field, 1.1);
        assertEquals(1.1, row.getValueAsDecimal(field), 0.0001);
    }

    @Test
    public void testGetValueAsBinary() {
        String hello = "hello";
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.BINARY);
        row.addField(field, hello.getBytes());
        assertEquals(hello, new String(row.getValueAsBinary(field)));
    }

    @Test
    public void testGetValueAsDate() {
        Date date = new Date();
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.SCALAR_DATE);
        row.addField(field, date);
        assertEquals(date, row.getValueAsDate(field));
    }

    @Test
    public void testGetValueAsLongVector() {
        long[] numbers = { 1, 2, 3 };
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_INTEGER);
        row.addField(field, numbers);
        assertEquals(numbers, row.getValueAsLongVector(field));
    }

    @Test
    public void testGetValueAsBooleanVector() {
        boolean[] values = { true, false };
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_BOOLEAN);
        row.addField(field, values);
        assertEquals(values, row.getValueAsBooleanVector(field));
    }

    @Test
    public void testGetValueAsDecimalVector() {
        double[] numbers = { 1.1, 2.1, 3.1 };
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_DECIMAL);
        row.addField(field, numbers);
        assertEquals(Arrays.toString(numbers),
                Arrays.toString(row.getValueAsDecimalVector(field)));
    }

    @Test
    public void testGetValueAsDateVector() {
        Date[] dates = { new Date(0), new Date(1) };
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_DATE);
        row.addField(field, dates);
        assertArrayEquals(dates, row.getValueAsDateVector(field));
    }

    @Test
    public void testGetValueAsTextVector() {
        String[] values = { "value1", "value2" };
        DataRow row = new DataRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_TEXT);
        row.addField(field, values);
        assertArrayEquals(values, row.getValueAsTextVector(field));
    }

    @Test
    public void testFrom() {
        DataRow row = DataRow.from(text, "text1", integer, 1L);
        assertEquals(2, row.size());
        assertTrue(row.toString().contains("text1"));
    }

}
