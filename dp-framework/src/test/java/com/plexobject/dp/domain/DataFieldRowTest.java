package com.plexobject.dp.domain;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class DataFieldRowTest {
    private MetaField text;
    private MetaField integer;

    @Before
    public void setup() {
        MetaFieldFactory.reset();
        text = MetaFieldFactory.create("text", MetaFieldType.SCALAR_TEXT);
        integer = MetaFieldFactory.create("integer", MetaFieldType.SCALAR_INTEGER);
    }

    @Test
    public void testCreate() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, null);
        assertEquals(1, row.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueNull() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, null);
        row.getValue(text);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetValueInitial() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, InitialValue.instance);
        row.getValue(text);
    }

    @Test(expected = RuntimeException.class)
    public void testGetValueRuntimeException() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, new RuntimeException());
        row.getValue(text);
    }

    @Test(expected = RuntimeException.class)
    public void testGetValueException() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, new Exception());
        row.getValue(text);
    }

    @Test
    public void testHasFieldValueNull() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, null);
        assertFalse(row.hasFieldValue(text));
    }

    @Test
    public void testHasFieldValueInitial() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, InitialValue.instance);
        assertFalse(row.hasFieldValue(text));
    }

    @Test
    public void testHasFieldValueRuntimeException() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, new RuntimeException());
        assertFalse(row.hasFieldValue(text));
    }

    @Test
    public void testHasFieldValueException() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, new Exception());
        assertFalse(row.hasFieldValue(text));
    }

    @Test
    public void testAddField() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, "value1");
        row.addField(integer, 1);
        assertEquals(2, row.size());
    }

    @Test
    public void testGetFields() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, "value1");
        row.addField(integer, 1);
        assertEquals(2, row.size());
        assertEquals("value1", row.getFields().get(text));
        assertEquals(1, row.getFields().get(integer));
    }

    @Test
    public void testHasFieldValue() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, "value1");
        assertEquals(1, row.size());
        assertTrue(row.hasFieldValue(text));
    }

    @Test
    public void testGetValue() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, "value1");
        assertEquals("value1", row.getValue(text));
    }

    @Test
    public void testGetValueAsText() {
        DataFieldRow row = new DataFieldRow();
        row.addField(text, "value1");
        assertEquals("value1", row.getValueAsText(text));
    }

    @Test
    public void testGetValueAsLong() {
        DataFieldRow row = new DataFieldRow();
        MetaField field = new MetaField("object", MetaFieldType.SCALAR_INTEGER);
        row.addField(field, 1);
        assertEquals(1L, row.getValueAsLong(field));
    }

    @Test
    public void testGetValueAsDecimal() {
        DataFieldRow row = new DataFieldRow();
        MetaField field = new MetaField("object", MetaFieldType.SCALAR_DECIMAL);
        row.addField(field, 1.1);
        assertEquals(1.1, row.getValueAsDecimal(field), 0.0001);
    }

    @Test
    public void testGetValueAsBinary() {
        String hello = "hello";
        DataFieldRow row = new DataFieldRow();
        MetaField field = new MetaField("object", MetaFieldType.BINARY);
        row.addField(field, hello.getBytes());
        assertEquals(hello, new String(row.getValueAsBinary(field)));
    }

    @Test
    public void testGetValueAsDate() {
        Date date = new Date();
        DataFieldRow row = new DataFieldRow();
        MetaField field = new MetaField("object", MetaFieldType.SCALAR_DATE);
        row.addField(field, date);
        assertEquals(date, row.getValueAsDate(field));
    }

    @Test
    public void testGetValueAsLongVector() {
        long[] numbers = { 1, 2, 3 };
        DataFieldRow row = new DataFieldRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_INTEGER);
        row.addField(field, numbers);
        assertEquals(numbers, row.getValueAsLongVector(field));
    }

    @Test
    public void testGetValueAsDecimalVector() {
        double[] numbers = { 1.1, 2.1, 3.1 };
        DataFieldRow row = new DataFieldRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_DECIMAL);
        row.addField(field, numbers);
        assertEquals(Arrays.toString(numbers),
                Arrays.toString(row.getValueAsDecimalVector(field)));
    }

    @Test
    public void testGetValueAsDateVector() {
        Date[] dates = { new Date(0), new Date(1) };
        DataFieldRow row = new DataFieldRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_DATE);
        row.addField(field, dates);
        assertArrayEquals(dates, row.getValueAsDateVector(field));
    }

    @Test
    public void testGetValueAsTextVector() {
        String[] values = { "value1", "value2" };
        DataFieldRow row = new DataFieldRow();
        MetaField field = new MetaField("object", MetaFieldType.VECTOR_TEXT);
        row.addField(field, values);
        assertArrayEquals(values, row.getValueAsTextVector(field));
    }

    @Test
    public void testFrom() {
        DataFieldRow row = DataFieldRow.from(text, "text1", integer, 1L);
        assertEquals(2, row.size());
        assertTrue(row.toString().contains("text1"));
    }

}
