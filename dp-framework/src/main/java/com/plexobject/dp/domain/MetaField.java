package com.plexobject.dp.domain;


public class MetaField implements Comparable<MetaField> {
    private final String name;
    private final MetaFieldType type;

    MetaField(String name, MetaFieldType type) {
        this.name = name;
        this.type = type;
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
        return "[" + name + "/" + type + "]";
    }

    @Override
    public int compareTo(MetaField other) {
        return name.compareTo(other.name);
    }
}
