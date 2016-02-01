package com.plexobject.dp.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class DataRowSet {
    private final transient Metadata metadata;
    private final List<DataRow> rows = new ArrayList<>();
    private final transient Map<String, MetaField> metaFieldsByName = new HashMap<>();

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
        return row.addField(metaField, value);
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

    private void validateRow(int row) {
        if (row >= rows.size()) {
            throw new IllegalArgumentException("row " + row
                    + " is higher than internal rows " + rows.size());
        }
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
        }
    }

}
