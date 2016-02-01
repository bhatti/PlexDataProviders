package com.plexobject.dp.domain;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.plexobject.dp.util.ObjectConversionUtils;

/**
 * This class defines collection of data fields that would be returned or
 * consumed by data provider
 * 
 * @author shahzad bhatti
 *
 */
public class DataRow {
    private final Map<MetaField, Object> fields = new ConcurrentHashMap<>();

    public DataRow() {
    }

    public synchronized void addField(MetaField metaField, Object value) {
        Objects.requireNonNull(metaField, "metaField is required");
        if (value == null) {
            value = NullObject.instance;
        }
        if (!fields.containsKey(metaField)) {
            fields.put(metaField, value);
        }
    }

    public synchronized int size() {
        return fields.size();
    }

    public synchronized Map<MetaField, Object> getFields() {
        return fields;
    }

    public synchronized boolean hasFieldValue(MetaField metaField) {
        Objects.requireNonNull(metaField, "metaField is required");

        Object value = fields.get(metaField);
        if (value == null || value instanceof NullObject
                || value instanceof InitialValue || value instanceof Exception) {
            return false;
        }
        return true;
    }

    public synchronized Object getValue(MetaField metaField) {
        Objects.requireNonNull(metaField, "metaField is required");

        Object value = fields.get(metaField);
        if (value == null || value instanceof NullObject
                || value instanceof InitialValue) {
            throw new IllegalStateException("metaField " + metaField
                    + " doesn't exist");
        }
        if (value instanceof RuntimeException) {
            throw (RuntimeException) value;
        } else if (value instanceof Exception) {
            throw new RuntimeException("Error found retrieving " + metaField,
                    (Exception) value);
        }
        return value;
    }

    public String getValueAsText(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsText(value);
    }

    public long getValueAsLong(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsLong(value);
    }

    public boolean getValueAsBoolean(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsBoolean(value);
    }

    public double getValueAsDecimal(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsDecimal(value);
    }

    public byte[] getValueAsBinary(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsBinary(value);
    }

    public Date getValueAsDate(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsDate(value);
    }

    public long[] getValueAsLongVector(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsLongVector(value);
    }

    public boolean[] getValueAsBooleanVector(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsBooleanVector(value);
    }

    public double[] getValueAsDecimalVector(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsDecimalVector(value);
    }

    public Date[] getValueAsDateVector(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsDateVector(value);
    }

    public String[] getValueAsTextVector(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsTextVector(value);
    }

    /**
     * The objects should be paired by MetaField and value
     * 
     * @param objects
     * @return
     */
    public static DataRow from(Object... objects) {
        DataRow row = new DataRow();
        for (int i = 0; i < objects.length; i += 2) {
            MetaField field = (MetaField) objects[i];
            Object value = objects[i + 1];
            row.addField(field, value);
        }
        return row;
    }

    @Override
    public String toString() {
        return "[fields=" + fields + "]";
    }

}
