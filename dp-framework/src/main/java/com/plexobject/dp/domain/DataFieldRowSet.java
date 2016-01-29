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
    private final MetaFields metaFields;
    private final List<DataFieldRow> rows = new ArrayList<>();
    private final transient Map<String, MetaField> metaFieldsByName = new HashMap<>();

    public DataFieldRowSet(MetaFields metaFields) {
        this(metaFields, Arrays.<DataFieldRow> asList());
    }

    public DataFieldRowSet(MetaFields metaFields, DataFieldRow... rows) {
        this(metaFields, Arrays.asList(rows));
    }

    public DataFieldRowSet(MetaFields metaFields, Collection<DataFieldRow> rows) {
        Objects.requireNonNull(metaFields, "metaFields cannot be null");
        Objects.requireNonNull(rows, "rows cannot be null");
        this.metaFields = metaFields;
        for (MetaField metaField : metaFields.getMetaFields()) {
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

    public MetaFields getMetaFields() {
        return metaFields;
    }

    public synchronized int size() {
        return rows.size();
    }

    public synchronized List<DataFieldRow> getRows() {
        return rows;
    }

    public synchronized boolean hasFieldValue(MetaField metaField, int row) {
        assert(metaFields.contains(metaField));
        if (row >= rows.size()) {
            return false;
        }
        return rows.get(row).hasFieldValue(metaField);
    }

    public synchronized Object getFieldValue(MetaField metaField, int rowNumber) {
        assert(metaFields.contains(metaField));
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
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsText(metaField);
    }

    public long getValueAsLong(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsLong(metaField);
    }

    public double getValueAsDecimal(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDecimal(metaField);
    }

    public byte[] getValueAsBinary(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsBinary(metaField);
    }

    public Date getValueAsDate(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDate(metaField);
    }

    public long[] getValueAsLongArray(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsLongArray(metaField);
    }

    public double[] getValueAsDecimalArray(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDecimalArray(metaField);
    }

    public Date[] getValueAsDateArray(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsDateArray(metaField);
    }

    public String[] getValueAsTextArray(MetaField metaField, int row) {
        Objects.requireNonNull(metaField, "metaField cannot be null");
        assert(metaFields.contains(metaField));
        validateRow(row);
        return rows.get(row).getValueAsTextArray(metaField);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows.size(); i++) {
            sb.append("\n\t\trow-" + i + ": " + rows.get(i));
        }
        return "DataFieldRowSet [metaFields=" + metaFields + ", rows=" + sb
                + "]";
    }

    private synchronized void addMetaField(MetaField metaField) {
        if (!metaFieldsByName.containsKey(metaField.getName())) {
            metaFields.addMetaField(metaField);
            metaFieldsByName.put(metaField.getName(), metaField);
        }
    }

}
