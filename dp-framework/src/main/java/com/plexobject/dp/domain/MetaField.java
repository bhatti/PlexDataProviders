package com.plexobject.dp.domain;

public class MetaField implements Comparable<MetaField> {
    private final String name;
    private final MetaFieldType type;
    private final boolean isKeyField;

    MetaField(String name, MetaFieldType type, boolean isKeyField) {
        this.name = name;
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
        return name.equals(other.name);
    }

    @Override
    public String toString() {
        return name + "/" + type + "/" + isKeyField;
    }

    @Override
    public int compareTo(MetaField other) {
        return name.compareTo(other.name);
    }

    public boolean isKeyField() {
        return isKeyField;
    }
}
