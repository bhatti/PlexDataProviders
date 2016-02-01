package com.plexobject.dp.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

public class ObjectConversionUtilsTest {
    @Test
    public void testGetAsText() {
        assertEquals("xx", ObjectConversionUtils.getAsText("xx"));
        assertEquals("1", ObjectConversionUtils.getAsText(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsTextNull() {
        ObjectConversionUtils.getAsText(null);
    }

    @Test
    public void testGetAsTextVector() {
        Object reflectionArr = Array.newInstance(String.class, 3);
        Array.set(reflectionArr, 0, "one");
        Array.set(reflectionArr, 1, "two");
        Array.set(reflectionArr, 2, "three");
        String[] arr = { "one", "two", "three" };
        assertArrayEquals(arr,
                ObjectConversionUtils.getAsTextVector(Arrays.asList(arr)));
        assertArrayEquals(arr, ObjectConversionUtils.getAsTextVector(arr));
        assertArrayEquals(arr,
                ObjectConversionUtils.getAsTextVector(reflectionArr));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsTextArrayNull() {
        ObjectConversionUtils.getAsTextVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsTextArrayBad() {
        ObjectConversionUtils.getAsTextVector(1);
    }

    @Test
    public void testGetAsBoolean() {
        assertTrue(ObjectConversionUtils.getAsBoolean("true"));
        assertTrue(ObjectConversionUtils.getAsBoolean(1.1));
        assertFalse(ObjectConversionUtils.getAsBoolean(0));
        assertTrue(ObjectConversionUtils.getAsBoolean(true));
        assertTrue(ObjectConversionUtils.getAsBoolean(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsBooleanNull() {
        ObjectConversionUtils.getAsBoolean(null);
    }

    @Test
    public void testGetAsLong() {
        assertEquals(1L, ObjectConversionUtils.getAsLong("1"));
        assertEquals(1L, ObjectConversionUtils.getAsLong(1.1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsLongNull() {
        ObjectConversionUtils.getAsLong(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsLongBad() {
        ObjectConversionUtils.getAsLong(new Date());
    }

    @Test
    public void testGetAsLongVector() {
        Object reflectionArr = Array.newInstance(Long.class, 3);
        Array.set(reflectionArr, 0, 1L);
        Array.set(reflectionArr, 1, 2L);
        Array.set(reflectionArr, 2, 3L);
        long[] arr = { 1L, 2L, 3L };
        assertEquals(Arrays.toString(arr),
                Arrays.toString(ObjectConversionUtils.getAsLongVector(Arrays
                        .asList(1L, 2L, 3L))));
        assertEquals(arr, ObjectConversionUtils.getAsLongVector(arr));
        assertEquals(Arrays.toString(arr),
                Arrays.toString(ObjectConversionUtils
                        .getAsLongVector(reflectionArr)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsLongArrayNull() {
        ObjectConversionUtils.getAsLongVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsLongArrayBad() {
        ObjectConversionUtils.getAsLongVector(new Date());
    }

    @Test
    public void testGetAsBooleanVector() {
        Object reflectionArr = Array.newInstance(Boolean.class, 3);
        Array.set(reflectionArr, 0, true);
        Array.set(reflectionArr, 1, false);
        Array.set(reflectionArr, 2, true);
        boolean[] arr = { true, false, true };
        assertEquals(Arrays.toString(arr),
                Arrays.toString(ObjectConversionUtils.getAsBooleanVector(Arrays
                        .asList(true, false, true))));
        assertEquals(arr, ObjectConversionUtils.getAsBooleanVector(arr));
        assertEquals(Arrays.toString(arr),
                Arrays.toString(ObjectConversionUtils
                        .getAsBooleanVector(reflectionArr)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsBooleanArrayNull() {
        ObjectConversionUtils.getAsBooleanVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsBooleanArrayBad() {
        ObjectConversionUtils.getAsBooleanVector(new Date());
    }

    @Test
    public void testGetAsDouble() {
        assertEquals(1.1, ObjectConversionUtils.getAsDecimal("1.1"), 0.0001);
        assertEquals(1.1, ObjectConversionUtils.getAsDecimal(1.1), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDoubleNull() {
        ObjectConversionUtils.getAsDecimal(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDoubleBad() {
        ObjectConversionUtils.getAsDecimal(new Date());
    }

    @Test
    public void testGetAsDoubleVector() {
        Object reflectionArr = Array.newInstance(Double.class, 3);
        Array.set(reflectionArr, 0, 1.1);
        Array.set(reflectionArr, 1, 2.1);
        Array.set(reflectionArr, 2, 3.1);
        double[] arr = { 1.1, 2.1, 3.1 };
        assertEquals(Arrays.toString(arr),
                Arrays.toString(ObjectConversionUtils.getAsDecimalVector(Arrays
                        .asList(1.1, 2.1, 3.1))));
        assertEquals(arr, ObjectConversionUtils.getAsDecimalVector(arr));
        assertEquals(Arrays.toString(arr),
                Arrays.toString(ObjectConversionUtils
                        .getAsDecimalVector(reflectionArr)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDoubleArrayNull() {
        ObjectConversionUtils.getAsDecimalVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDoubleArrayBad() {
        ObjectConversionUtils.getAsDecimalVector(new Date());
    }

    @Test
    public void testGetAsBinary() {
        String text = "hello";
        assertEquals(text, new String(ObjectConversionUtils.getAsBinary(text)));
        assertEquals(text,
                new String(ObjectConversionUtils.getAsBinary(text.getBytes())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsBinaryNull() {
        ObjectConversionUtils.getAsBinary(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsBinaryBad() {
        ObjectConversionUtils.getAsBinary(new Date());
    }

    @Test
    public void testGetAsDate() {
        Date date = new Date();
        assertEquals(date, ObjectConversionUtils.getAsDate(date));
        assertEquals(date, ObjectConversionUtils.getAsDate(date.getTime()));
        assertEquals(date,
                ObjectConversionUtils.getAsDate(String.valueOf(date.getTime())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDateNull() {
        ObjectConversionUtils.getAsDate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDateBad() {
        ObjectConversionUtils.getAsDate(new StringBuilder());
    }

    @Test
    public void testGetAsDateVector() {
        Date date1 = new Date(0);
        Date date2 = new Date(1);
        Date date3 = new Date();

        Object reflectionArr = Array.newInstance(Date.class, 3);
        Array.set(reflectionArr, 0, date1);
        Array.set(reflectionArr, 1, date2);
        Array.set(reflectionArr, 2, date3);
        Date[] arr = { date1, date2, date3 };
        assertArrayEquals(arr,
                ObjectConversionUtils.getAsDateVector(Arrays.asList(arr)));
        assertArrayEquals(arr, ObjectConversionUtils.getAsDateVector(arr));
        assertArrayEquals(arr,
                ObjectConversionUtils.getAsDateVector(reflectionArr));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDateArrayNull() {
        ObjectConversionUtils.getAsDateVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDateArrayBad() {
        ObjectConversionUtils.getAsDateVector(new Date());
    }

}
