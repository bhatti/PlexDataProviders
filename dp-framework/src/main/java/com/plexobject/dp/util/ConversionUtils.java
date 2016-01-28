package com.plexobject.dp.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;

public class ConversionUtils {
    public static String getAsText(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value != null) {
            return value.toString();
        }
        throw new IllegalArgumentException("unexpected null value found ");
    }

    public static long getAsLong(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.valueOf((String) value);
        }
        throw new IllegalArgumentException("unexpected type found "
                + (value != null ? value.getClass().getSimpleName() : "null")
                + ", value " + value);
    }

    public static double getAsDecimal(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            return Double.valueOf((String) value);
        }
        throw new IllegalArgumentException("unexpected type found "
                + (value != null ? value.getClass().getSimpleName() : "null")
                + ", value " + value);
    }

    public static byte[] getAsBinary(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        } else if (value instanceof String) {
            return ((String) value).getBytes();
        }
        throw new IllegalArgumentException("unexpected type found "
                + (value != null ? value.getClass().getSimpleName() : "null")
                + ", value " + value);
    }

    public static Date getAsDate(Object value) {
        if (value instanceof Date) {
            return (Date) value;
        } else if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        } else if (value instanceof String) {
            return new Date(Long.valueOf((String) value));
        }
        throw new IllegalArgumentException("unexpected type found "
                + (value != null ? value.getClass().getSimpleName() : "null")
                + ", value " + value);
    }

    public static long[] getAsLongArray(Object value) {
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) (value);
            long[] values = new long[collection.size()];
            int i = 0;
            for (Object obj : collection) {
                values[i++] = ConversionUtils.getAsLong(obj);
            }
            return values;
        } else if (value instanceof long[]) {
            long[] array = (long[]) value;
            return array;
        } else if (value instanceof Long[]) {
            Long[] array = (Long[]) value;
            long[] values = new long[Array.getLength(array)];
            for (int i = 0; i < values.length; i++) {
                values[i] = array[i];
            }
            return values;
        }
        throw new IllegalArgumentException("unexpected type found "
                + (value != null ? value.getClass().getSimpleName() : "null")
                + ", value " + value);
    }

    public static double[] getAsDecimalArray(Object value) {
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) (value);
            double[] values = new double[collection.size()];
            int i = 0;
            for (Object obj : collection) {
                values[i++] = ConversionUtils.getAsDecimal(obj);
            }
            return values;
        } else if (value instanceof double[]) {
            double[] array = (double[]) value;
            return array;
        } else if (value instanceof Double[]) {
            Double[] array = (Double[]) value;
            double[] values = new double[Array.getLength(array)];
            for (int i = 0; i < values.length; i++) {
                values[i] = array[i];
            }
            return values;
        }
        throw new IllegalArgumentException("unexpected type found "
                + (value != null ? value.getClass().getSimpleName() : "null")
                + ", value " + value);
    }

    public static Date[] getAsDateArray(Object value) {
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) (value);
            Date[] values = new Date[collection.size()];
            int i = 0;
            for (Object obj : collection) {
                values[i++] = ConversionUtils.getAsDate(obj);
            }
            return values;
        } else if (value instanceof Date[]) {
            Date[] array = (Date[]) value;
            return array;
        }
        throw new IllegalArgumentException("unexpected type found "
                + (value != null ? value.getClass().getSimpleName() : "null")
                + ", value " + value);
    }

    public static String[] getAsTextArray(Object value) {
        if (value instanceof Collection) {
            Collection<?> collection = (Collection<?>) (value);
            String[] values = new String[collection.size()];
            int i = 0;
            for (Object obj : collection) {
                values[i++] = ConversionUtils.getAsText(obj);
            }
            return values;
        } else if (value instanceof String[]) {
            String[] array = (String[]) value;
            return array;
        }
        throw new IllegalArgumentException("unexpected type found "
                + (value != null ? value.getClass().getSimpleName() : "null")
                + ", value " + value);
    }
}
