package com.plexobject.dp.domain;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.plexobject.dp.util.ObjectConversionUtils;

/**
 * This class defines collection of data fields where each data field is
 * identified by meta-field
 * 
 * @author shahzad bhatti
 *
 */
public class DataRow {
    private final Map<MetaField, Object> fields = new ConcurrentHashMap<>();

    public DataRow() {
    }

    public synchronized boolean addField(MetaField metaField, Object value) {
        Objects.requireNonNull(metaField, "metaField is required");
        boolean added = true;
        if (value == null) {
            value = NullObject.instance;
            added = false;
        } else if (!metaField.isValidValue(value)) {
            throw new IllegalArgumentException("The value " + value + " for "
                    + metaField.getName() + " doesn't match type " + metaField);
        }
        if (!fields.containsKey(metaField)) {
            fields.put(metaField, value);
        } else {
            added = false;
        }
        return added;
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
            throw new NullPointerException("The value for " + metaField
                    + " is not found");
        }
        if (value instanceof RuntimeException) {
            throw (RuntimeException) value;
        } else if (value instanceof Exception) {
            throw new RuntimeException("Error found retrieving " + metaField,
                    (Exception) value);
        }
        return value;
    }

    public DataRowSet getValueAsRowSet(MetaField metaField) {
        if (metaField.getType() != MetaFieldType.ROWSET) {
            throw new IllegalStateException("metaField " + metaField
                    + " is not rowset typet");
        }
        Object value = getValue(metaField);
        return (DataRowSet) value;
    }

    public String getValueAsText(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsText(value);
    }

    public long getValueAsLong(MetaField field) {
        Object value = getValue(field);
        try {
            return ObjectConversionUtils.getAsLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse long for "
                    + field + ", value " + value);
        }
    }

    public boolean getValueAsBoolean(MetaField field) {
        Object value = getValue(field);
        return ObjectConversionUtils.getAsBoolean(value);
    }

    public double getValueAsDecimal(MetaField field) {
        Object value = getValue(field);
        try {
            return ObjectConversionUtils.getAsDecimal(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse decimal for "
                    + field + ", value " + value);
        }
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
