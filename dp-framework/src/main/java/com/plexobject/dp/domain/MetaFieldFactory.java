package com.plexobject.dp.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaFieldFactory {
    private static Map<String, MetaField> fieldsCache = new ConcurrentHashMap<>();

    public static MetaField lookup(String name) {
        return fieldsCache.get(name);
    }

    public static synchronized MetaField createRowset(final String name) {
        return create(name, MetaFieldType.ROWSET, false);
    }

    public static synchronized MetaField createText(final String name) {
        return create(name, MetaFieldType.SCALAR_TEXT, false);
    }

    public static synchronized MetaField createInteger(final String name) {
        return create(name, MetaFieldType.SCALAR_INTEGER, false);
    }

    public static synchronized MetaField createDecimal(final String name) {
        return create(name, MetaFieldType.SCALAR_DECIMAL, false);
    }

    public static synchronized MetaField createDate(final String name) {
        return create(name, MetaFieldType.SCALAR_DATE, false);
    }

    public static synchronized MetaField createBoolean(final String name) {
        return create(name, MetaFieldType.SCALAR_BOOLEAN, false);
    }

    public static synchronized MetaField createBinary(final String name) {
        return create(name, MetaFieldType.BINARY, false);
    }

    public static synchronized MetaField createVectorText(final String name) {
        return create(name, MetaFieldType.VECTOR_TEXT, false);
    }

    public static synchronized MetaField createVectorInteger(final String name) {
        return create(name, MetaFieldType.VECTOR_INTEGER, false);
    }

    public static synchronized MetaField createVectorDecimal(final String name) {
        return create(name, MetaFieldType.VECTOR_DECIMAL, false);
    }

    public static synchronized MetaField createVectorDate(final String name) {
        return create(name, MetaFieldType.VECTOR_DATE, false);
    }

    public static synchronized MetaField createVectorBoolean(final String name) {
        return create(name, MetaFieldType.VECTOR_BOOLEAN, false);
    }

    public static synchronized MetaField createVectorRowSet(final String name) {
        return create(name, MetaFieldType.ROWSET, false);
    }

    public static synchronized MetaField create(final String name,
            final MetaFieldType type, final boolean isKeyField) {
        MetaField field = fieldsCache.get(name);
        if (field != null) {
            if (field.getType() == type) {
                return field;
            }
            throw new IllegalArgumentException(
                    "Duplicate meta field with different type found " + field
                            + ", new type " + type);
        }
        field = new MetaField(name, type, isKeyField);
        fieldsCache.put(name, field);
        return field;
    }

    public static void reset() {
        fieldsCache.clear();
    }
}
