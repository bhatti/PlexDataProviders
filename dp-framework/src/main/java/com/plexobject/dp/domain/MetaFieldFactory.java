package com.plexobject.dp.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaFieldFactory {
    private static Map<String, MetaField> fieldsCache = new ConcurrentHashMap<>();

    public static MetaField lookup(String name) {
        return fieldsCache.get(name);
    }

    public static synchronized MetaField createRowset(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.ROWSET, isKeyField);
    }

    public static synchronized MetaField createText(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.SCALAR_TEXT, isKeyField);
    }

    public static synchronized MetaField createInteger(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.SCALAR_INTEGER, isKeyField);
    }

    public static synchronized MetaField createDecimal(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.SCALAR_DECIMAL, isKeyField);
    }

    public static synchronized MetaField createDate(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.SCALAR_DATE, isKeyField);
    }

    public static synchronized MetaField createBoolean(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.SCALAR_BOOLEAN, isKeyField);
    }

    public static synchronized MetaField createBinary(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.BINARY, isKeyField);
    }

    public static synchronized MetaField createVectorText(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.VECTOR_TEXT, isKeyField);
    }

    public static synchronized MetaField createVectorInteger(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.VECTOR_INTEGER, isKeyField);
    }

    public static synchronized MetaField createVectorDecimal(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.VECTOR_DECIMAL, isKeyField);
    }

    public static synchronized MetaField createVectorDate(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.VECTOR_DATE, isKeyField);
    }

    public static synchronized MetaField createVectorBoolean(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.VECTOR_BOOLEAN, isKeyField);
    }

    public static synchronized MetaField createVectorRowSet(final String name, final String category, boolean isKeyField) {
        return create(name, category, MetaFieldType.ROWSET, isKeyField);
    }

    public static synchronized MetaField create(final String name, final String category,
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
        field = new MetaField(name, category, type, isKeyField);
        fieldsCache.put(name, field);
        return field;
    }

    public static void reset() {
        fieldsCache.clear();
    }
}
