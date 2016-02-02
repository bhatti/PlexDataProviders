package com.plexobject.dp.domain;

import java.util.Objects;

public class MetaField implements Comparable<MetaField> {
    private final String name;
    private final String category;
    private final MetaFieldType type;
    private final boolean isKeyField;

    MetaField(String name, String category, MetaFieldType type,
            boolean isKeyField) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(category, "category is required");
        this.name = name;
        this.category = category;
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
        return category.equals(other.category) && name.equals(other.name);
    }

    @Override
    public String toString() {
        return category + ":" + name;
    }

    @Override
    public int compareTo(MetaField other) {
        int cmp = category.compareTo(other.category);
        if (cmp == 0) {
            cmp = name.compareTo(other.name);
        }
        return cmp;
    }

    public boolean isKeyField() {
        return isKeyField;
    }

    public String getCategory() {
        return category;
    }
}
