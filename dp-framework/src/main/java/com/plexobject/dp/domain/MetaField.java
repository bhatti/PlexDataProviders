package com.plexobject.dp.domain;

import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * This class defines meta information for the data fields
 * 
 * @author sahzad bhatti
 *
 */
public class MetaField implements Comparable<MetaField> {
    private final String name;
    private final String kind;
    private final MetaFieldType type;
    private final boolean isKeyField;

    MetaField(String name, String kind, MetaFieldType type, boolean isKeyField) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(kind, "kind is required");
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.isKeyField = isKeyField;
    }

    public String getName() {
        return name;
    }

    public MetaFieldType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MetaField other = (MetaField) obj;
        return kind.equals(other.kind) && name.equals(other.name);
    }

    @Override
    public String toString() {
        return kind + ":" + name;
    }

    @Override
    public int compareTo(MetaField other) {
        int cmp = kind.compareTo(other.kind);
        if (cmp == 0) {
            cmp = name.compareTo(other.name);
        }
        return cmp;
    }

    public boolean isKeyField() {
        return isKeyField;
    }

    public String getKind() {
        return kind;
    }

    public boolean isValidValue(Object value) {
        if (value == null || value instanceof InitialValue
                || value instanceof NullObject || value instanceof Exception) {
            return true;
        }
        switch (getType()) {
        case SCALAR_TEXT:
            return value instanceof String;
        case SCALAR_INTEGER:
            return value instanceof Number || value instanceof String;
        case SCALAR_DECIMAL:
            return value instanceof Number || value instanceof String;
        case SCALAR_DATE:
            return value instanceof Number || value instanceof Date;
        case SCALAR_BOOLEAN:
            return value instanceof Number || value instanceof String
                    || value instanceof Boolean;
        case VECTOR_TEXT:
            return value instanceof String[] || value instanceof Collection;
        case VECTOR_INTEGER:
            return value instanceof long[] || value instanceof Long[]
                    || value instanceof Collection;
        case VECTOR_DECIMAL:
            return value instanceof double[] || value instanceof Double[]
                    || value instanceof Collection;
        case VECTOR_DATE:
            return value instanceof Date[] || value instanceof Collection;
        case VECTOR_BOOLEAN:
            return value instanceof boolean[] || value instanceof Boolean[]
                    || value instanceof Collection;
        case BINARY:
            return value instanceof byte[];
        case ROWSET:
            return value instanceof DataRowSet;
        default:
            return false;
        }
    }
}
