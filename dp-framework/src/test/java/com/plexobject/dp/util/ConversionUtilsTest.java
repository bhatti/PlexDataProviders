package com.plexobject.dp.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

public class ConversionUtilsTest {
    @Test
    public void testGetAsText() {
        assertEquals("xx", ConversionUtils.getAsText("xx"));
        assertEquals("1", ConversionUtils.getAsText(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsTextNull() {
        ConversionUtils.getAsText(null);
    }

    @Test
    public void testGetAsTextVector() {
        Object reflectionArr = Array.newInstance(String.class, 3);
        Array.set(reflectionArr, 0, "one");
        Array.set(reflectionArr, 1, "two");
        Array.set(reflectionArr, 2, "three");
        String[] arr = { "one", "two", "three" };
        assertArrayEquals(arr,
                ConversionUtils.getAsTextVector(Arrays.asList(arr)));
        assertArrayEquals(arr, ConversionUtils.getAsTextVector(arr));
        assertArrayEquals(arr, ConversionUtils.getAsTextVector(reflectionArr));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsTextArrayNull() {
        ConversionUtils.getAsTextVector(null);
    }

    @Test
    public void testGetAsObjectVector() {
        Object reflectionArr = Array.newInstance(String.class, 3);
        Array.set(reflectionArr, 0, "one");
        Array.set(reflectionArr, 1, "two");
        Array.set(reflectionArr, 2, "three");
        String[] arr = { "one", "two", "three" };
        assertArrayEquals(arr,
                ConversionUtils.getAsObjectVector(Arrays.asList(arr)));
        assertArrayEquals(arr, ConversionUtils.getAsObjectVector(arr));
        assertArrayEquals(arr, ConversionUtils.getAsObjectVector(reflectionArr));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsObjectArrayNull() {
        ConversionUtils.getAsObjectVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsTextArrayBad() {
        ConversionUtils.getAsTextVector(1);
    }

    @Test
    public void testGetAsLong() {
        assertEquals(1L, ConversionUtils.getAsLong("1"));
        assertEquals(1L, ConversionUtils.getAsLong(1.1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsLongNull() {
        ConversionUtils.getAsLong(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsLongBad() {
        ConversionUtils.getAsLong(new Date());
    }

    @Test
    public void testGetAsLongVector() {
        Object reflectionArr = Array.newInstance(Long.class, 3);
        Array.set(reflectionArr, 0, 1L);
        Array.set(reflectionArr, 1, 2L);
        Array.set(reflectionArr, 2, 3L);
        long[] arr = { 1L, 2L, 3L };
        assertEquals(Arrays.toString(arr), Arrays.toString(ConversionUtils
                .getAsLongVector(Arrays.asList(1L, 2L, 3L))));
        assertEquals(arr, ConversionUtils.getAsLongVector(arr));
        assertEquals(Arrays.toString(arr),
                Arrays.toString(ConversionUtils.getAsLongVector(reflectionArr)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsLongArrayNull() {
        ConversionUtils.getAsLongVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsLongArrayBad() {
        ConversionUtils.getAsLongVector(new Date());
    }

    @Test
    public void testGetAsDouble() {
        assertEquals(1.1, ConversionUtils.getAsDecimal("1.1"), 0.0001);
        assertEquals(1.1, ConversionUtils.getAsDecimal(1.1), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDoubleNull() {
        ConversionUtils.getAsDecimal(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDoubleBad() {
        ConversionUtils.getAsDecimal(new Date());
    }

    @Test
    public void testGetAsDoubleVector() {
        Object reflectionArr = Array.newInstance(Double.class, 3);
        Array.set(reflectionArr, 0, 1.1);
        Array.set(reflectionArr, 1, 2.1);
        Array.set(reflectionArr, 2, 3.1);
        double[] arr = { 1.1, 2.1, 3.1 };
        assertEquals(Arrays.toString(arr), Arrays.toString(ConversionUtils
                .getAsDecimalVector(Arrays.asList(1.1, 2.1, 3.1))));
        assertEquals(arr, ConversionUtils.getAsDecimalVector(arr));
        assertEquals(Arrays.toString(arr), Arrays.toString(ConversionUtils
                .getAsDecimalVector(reflectionArr)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDoubleArrayNull() {
        ConversionUtils.getAsDecimalVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDoubleArrayBad() {
        ConversionUtils.getAsDecimalVector(new Date());
    }

    @Test
    public void testGetAsBinary() {
        String text = "hello";
        assertEquals(text, new String(ConversionUtils.getAsBinary(text)));
        assertEquals(text,
                new String(ConversionUtils.getAsBinary(text.getBytes())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsBinaryNull() {
        ConversionUtils.getAsBinary(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsBinaryBad() {
        ConversionUtils.getAsBinary(new Date());
    }

    @Test
    public void testGetAsDate() {
        Date date = new Date();
        assertEquals(date, ConversionUtils.getAsDate(date));
        assertEquals(date, ConversionUtils.getAsDate(date.getTime()));
        assertEquals(date,
                ConversionUtils.getAsDate(String.valueOf(date.getTime())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDateNull() {
        ConversionUtils.getAsDate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDateBad() {
        ConversionUtils.getAsDate(new StringBuilder());
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
                ConversionUtils.getAsDateVector(Arrays.asList(arr)));
        assertArrayEquals(arr, ConversionUtils.getAsDateVector(arr));
        assertArrayEquals(arr, ConversionUtils.getAsDateVector(reflectionArr));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDateArrayNull() {
        ConversionUtils.getAsDateVector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAsDateArrayBad() {
        ConversionUtils.getAsDateVector(new Date());
    }

}
