package com.plexobject.dp.domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaFieldFactory {
    private static Map<String, MetaField> fieldsCache = new ConcurrentHashMap<>();

    public static MetaField lookup(String name) {
        return fieldsCache.get(name);
    }

    public static synchronized MetaField create(String name, MetaFieldType type) {
        MetaField field = fieldsCache.get(name);
        if (field != null) {
            if (field.getType() == type) {
                return field;
            }
            throw new IllegalArgumentException(
                    "Duplicate meta field with different type found " + field
                            + ", new type " + type);
        }
        field = new MetaField(name, type);
        fieldsCache.put(name, field);
        return field;
    }

    public static void reset() {
        fieldsCache.clear();
    }
}
