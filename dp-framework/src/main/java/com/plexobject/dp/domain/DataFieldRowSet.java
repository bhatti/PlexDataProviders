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
public class DataFieldRowSet {
    private final Metadata metadata;
    private final List<DataFieldRow> rows = new ArrayList<>();
    private final transient Map<String, MetaField> metaFieldsByName = new HashMap<>();

    public DataFieldRowSet(Metadata metadata) {
        this(metadata, Arrays.<DataFieldRow> asList());
    }

    public DataFieldRowSet(Metadata metadata, DataFieldRow... rows) {
        this(metadata, Arrays.asList(rows));
    }

    public DataFieldRowSet(Metadata metadata, Collection<DataFieldRow> rows) {
        Objects.requireNonNull(metadata, "metadata cannot be null");
        Objects.requireNonNull(rows, "rows cannot be null");
        this.metadata = metadata;
        for (MetaField metaField : metadata.getMetaFields()) {
            metaFieldsByName.put(metaField.getName(), metaField);
        }
        for (DataFieldRow row : rows) {
            addRow(row);
        }
    }

    public synchronized void addDataField(MetaField metaField, Object value,
            int rowNumber) {
        DataFieldRow row = getOrCreateRow(rowNumber);
        row.addField(metaField, value);
        addMetaField(metaField);
    }

    public synchronized DataFieldRow getOrCreateRow(int row) {
        while (row >= rows.size()) {
            addRow(new DataFieldRow());
        }
        return rows.get(row);
    }

    public synchronized void addRow(DataFieldRow row) {
        Objects.requireNonNull(row, "row cannot be null");
        rows.add(row);
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public synchronized int size() {
        return rows.size();
    }

    public synchronized List<DataFieldRow> getRows() {
        return rows;
    }

    public synchronized boolean hasFieldValue(MetaField metaField, int row) {
        assert(metadata.contains(metaField));
        if (row >= rows.size()) {
            return false;
        }
        return rows.get(row).hasFieldValue(metaField);
    }

    public synchronized Object getFieldValue(MetaField metaField, int rowNumber) {
        assert(metadata.contains(metaField));
        validateRow(rowNumber);
        DataFieldRow row = rows.get(rowNumber);
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
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsText(metaField);
    }

    public long getValueAsLong(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsLong(metaField);
    }

    public double getValueAsDecimal(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDecimal(metaField);
    }

    public byte[] getValueAsBinary(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsBinary(metaField);
    }

    public Date getValueAsDate(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDate(metaField);
    }

    public long[] getValueAsLongVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsLongVector(metaField);
    }

    public double[] getValueAsDecimalVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDecimalVector(metaField);
    }

    public Date[] getValueAsDateVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDateVector(metaField);
    }

    public String[] getValueAsTextVector(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metadata.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsTextVector(metaField);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            sb.append("\n\t\trow-" + i + ": " + rows.get(i));
        }
        return "DataFieldRowSet [metadata=" + metadata + ", rows=" + sb
                + "]";
    }

    private synchronized void addMetaField(MetaField metaField) {
        if (!metaFieldsByName.containsKey(metaField.getName())) {
            metadata.addMetaField(metaField);
            metaFieldsByName.put(metaField.getName(), metaField);
        }
    }

}
