package com.plexobject.dp.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * This class abstracts set of rows that would represent collection of objects.
 * Note that you can have rowset within a row to create nested structures
 * 
 * @author shahzad bhatti
 *
 */
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DataRowSet {
    private final transient Metadata metadata;
    private final transient Map<String, MetaField> metaFieldsByName = new ConcurrentHashMap<>();
    private transient Map<MetaField, Map<Object, Integer>> rowsByKey;
    private final List<DataRow> rows = new ArrayList<>();
    private boolean nestedRowSet;

    DataRowSet() {
        metadata = Metadata.from(); // For serialization
    }

    public DataRowSet(Metadata metadata) {
        this(metadata, Arrays.<DataRow> asList());
    }

    public DataRowSet(Metadata metadata, DataRow... rows) {
        this(metadata, Arrays.asList(rows));
    }

    public DataRowSet(Metadata metadata, Collection<DataRow> rows) {
        Objects.requireNonNull(metadata, "metadata cannot be null");
        Objects.requireNonNull(rows, "rows cannot be null");
        this.metadata = metadata;
        for (MetaField metaField : metadata.getMetaFields()) {
            metaFieldsByName.put(metaField.getName(), metaField);
        }
        for (DataRow row : rows) {
            addRow(row);
        }
    }

    public synchronized boolean addValueAtRow(MetaField metaField,
            Object value, int rowNumber) {
        DataRow row = getOrCreateRow(rowNumber);
        addMetaField(metaField);
        if (metaField.isKeyField()) {
            addKeyToMap(metaField, value, rowNumber);
        }
        return row.addField(metaField, value);
    }

    public synchronized boolean hasFieldValue(MetaField metaField, int row) {
        assert (metadata.contains(metaField));
        if (row >= rows.size()) {
            return false;
        }
        return rows.get(row).hasFieldValue(metaField);
    }

    public synchronized Object getValue(MetaField metaField, int rowNumber) {
        assert (metadata.contains(metaField));
        validateRow(rowNumber);
        DataRow row = rows.get(rowNumber);
        return row.getValue(metaField);
    }

    public synchronized DataRow getRowForKeyField(MetaField metaField,
            Object key) {
        assert (metadata.contains(metaField));
        if (!metaField.isKeyField()) {
            throw new IllegalArgumentException(metaField + " is not key field");
        }
        int rowNumber = getRowNumberByKey(metaField, key);
        if (rowNumber == -1) {
            throw new IllegalArgumentException("Row with " + metaField
                    + " and value " + key + " not found");
        }
        validateRow(rowNumber);
        return rows.get(rowNumber);
    }

    public synchronized DataRow getOrCreateRow(int row) {
        while (row >= rows.size()) {
            addRow(new DataRow());
        }
        return rows.get(row);
    }

    public synchronized void merge(DataRowSet rows) {
        Objects.requireNonNull(rows, "rowset cannot be null");
        for (MetaField field : rows.getMetadata().getMetaFields()) {
            addMetaField(field);
        }
        for (DataRow row : rows.getRows()) {
            addRow(row);
        }
    }

    public synchronized void addRow(DataRow row) {
        Objects.requireNonNull(row, "row cannot be null");
        for (MetaField field : row.getFields().keySet()) {
            addMetaField(field);
        }
        rows.add(row);
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public synchronized int size() {
        return rows.size();
    }

    public synchronized List<DataRow> getRows() {
        return rows;
    }

    public int getRowNumberByKey(MetaField metaField, Object key) {
        return getKeyToMap(metaField, key);
    }

    private void validateRow(int row) {
        if (row >= rows.size()) {
            throw new IllegalArgumentException("row " + row
                    + " is higher than internal rows " + rows.size());
        }
    }

    public DataRowSet getValueAsRowSet(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsRowSet(metaField);
    }

    public String getValueAsText(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsText(metaField);
    }

    public long getValueAsLong(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsLong(metaField);
    }

    public boolean getValueAsBoolean(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsBoolean(metaField);
    }

    public double getValueAsDecimal(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDecimal(metaField);
    }

    public byte[] getValueAsBinary(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsBinary(metaField);
    }

    public Date getValueAsDate(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDate(metaField);
    }

    public long[] getValueAsLongVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsLongVector(metaField);
    }

    public boolean[] getValueAsBooleanVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsBooleanVector(metaField);
    }

    public double[] getValueAsDecimalVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDecimalVector(metaField);
    }

    public Date[] getValueAsDateVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDateVector(metaField);
    }

    public String[] getValueAsTextVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert (metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsTextVector(metaField);
    }

    public boolean isNestedRowSet() {
        return nestedRowSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            sb.append("\n\t\trow-" + i + ": " + rows.get(i));
        }
        return "DataRowSet [metadata=" + metadata + ", rows=" + sb + "]";
    }

    private synchronized void addMetaField(MetaField metaField) {
        if (!metaFieldsByName.containsKey(metaField.getName())) {
            metadata.addMetaField(metaField);
            metaFieldsByName.put(metaField.getName(), metaField);
            if (metaField.getType() == MetaFieldType.ROWSET) {
                nestedRowSet = true;
            }
        }
    }

    private synchronized Map<Object, Integer> getKeyMap(MetaField field) {
        if (rowsByKey == null) {
            rowsByKey = new HashMap<MetaField, Map<Object, Integer>>();
        }
        Map<Object, Integer> keyMap = rowsByKey.get(field);
        if (keyMap == null) {
            keyMap = new HashMap<Object, Integer>();
            rowsByKey.put(field, keyMap);
        }
        return keyMap;
    }

    private synchronized void addKeyToMap(MetaField field, Object key,
            int rowNumber) {
        getKeyMap(field).put(key, rowNumber);
    }

    private synchronized int getKeyToMap(MetaField field, Object key) {
        Integer rowNumber = getKeyMap(field).get(key);
        if (rowNumber == null) {
            return -1;
        }
        return rowNumber;
    }

}
