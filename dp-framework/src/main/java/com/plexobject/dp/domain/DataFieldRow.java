package com.plexobject.dp.domain;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.plexobject.dp.util.ConversionUtils;

/**
 * This class defines collection of data fields that would be returned or
 * consumed by data provider
 * 
 * @author shahzad bhatti
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DataFieldRow {
    private final Map<MetaField, Object> fields = new ConcurrentHashMap<>();

    public DataFieldRow() {
    }

    public synchronized void addField(MetaField metaField, Object value) {
        Objects.requireNonNull(metaField, "metaField is required");
        if (value == null) {
            value = NullObject.instance;
        }
        fields.put(metaField, value);
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
            throw new IllegalStateException("DataField " + metaField
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
        return ConversionUtils.getAsText(value);
    }

    public long getValueAsLong(MetaField field) {
        Object value = getValue(field);
        return ConversionUtils.getAsLong(value);
    }

    public double getValueAsDecimal(MetaField field) {
        Object value = getValue(field);
        return ConversionUtils.getAsDecimal(value);
    }

    public byte[] getValueAsBinary(MetaField field) {
        Object value = getValue(field);
        return ConversionUtils.getAsBinary(value);
    }

    public Date getValueAsDate(MetaField field) {
        Object value = getValue(field);
        return ConversionUtils.getAsDate(value);
    }

    public long[] getValueAsLongVector(MetaField field) {
        Object value = getValue(field);
        return ConversionUtils.getAsLongVector(value);
    }

    public double[] getValueAsDecimalVector(MetaField field) {
        Object value = getValue(field);
        return ConversionUtils.getAsDecimalVector(value);
    }

    public Date[] getValueAsDateVector(MetaField field) {
        Object value = getValue(field);
        return ConversionUtils.getAsDateVector(value);
    }

    public String[] getValueAsTextVector(MetaField field) {
        Object value = getValue(field);
        return ConversionUtils.getAsTextVector(value);
    }

    /**
     * The objects should be paired by MetaField and value
     * 
     * @param objects
     * @return
     */
    public static DataFieldRow from(Object... objects) {
        DataFieldRow row = new DataFieldRow();
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
