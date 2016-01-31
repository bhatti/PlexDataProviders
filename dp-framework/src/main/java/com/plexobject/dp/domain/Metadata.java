package com.plexobject.dp.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Metadata {
    private final Set<MetaField> metaFields = new HashSet<>();

    public Metadata() {
    }

    public Metadata(MetaField... metaFields) {
        this(Arrays.asList(metaFields));
    }

    public Metadata(Collection<MetaField> metaFields) {
        for (MetaField metaField : metaFields) {
            addMetaField(metaField);
        }
    }

    public synchronized void addMetaField(MetaField metaField) {
        if (!metaFields.contains(metaField)) {
            metaFields.add(metaField);
        }
    }

    public synchronized void addMetadata(Metadata metadata) {
        for (MetaField metaField : metadata.getMetaFields()) {
            addMetaField(metaField);
        }
    }

    public synchronized void removeMetadata(Metadata metadata) {
        for (MetaField metaField : metadata.getMetaFields()) {
            removeMetaField(metaField);
        }
    }

    public synchronized void removeMetaField(MetaField metaField) {
        metaFields.remove(metaField);
    }

    public synchronized boolean containsAll(Metadata other) {
        return getMissingCount(other) == 0;
    }

    public synchronized boolean contains(MetaField other) {
        return getMissingCount(Metadata.from(other)) == 0;
    }

    public synchronized int getMissingCount(Metadata other) {
        int count = 0;
        for (MetaField field : other.metaFields) {
            if (!metaFields.contains(field)) {
                count++;
            }
        }
        return count;
    }

    public synchronized Metadata getMissingMetadata(Metadata other) {
        Set<MetaField> missingMetaFields = new HashSet<MetaField>();
        for (MetaField field : other.metaFields) {
            if (!metaFields.contains(field)) {
                missingMetaFields.add(field);
            }
        }
        return new Metadata(missingMetaFields);
    }

    public synchronized Collection<MetaField> getMetaFields() {
        return metaFields;
    }

    public synchronized int size() {
        return metaFields.size();
    }

    public static Metadata from(MetaField... args) {
        final Metadata metaFields = new Metadata();
        for (MetaField arg : args) {
            metaFields.addMetaField(arg);
        }
        return metaFields;
    }

    public static Metadata fromRaw(Object... args) {
        final Metadata metaFields = new Metadata();
        for (int i = 0; i < args.length; i += 2) {
            MetaField field = MetaFieldFactory.create((String) args[i],
                    MetaFieldType.valueOf(args[i + 1].toString()));
            metaFields.addMetaField(field);
        }
        return metaFields;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (MetaField field : metaFields) {
            result = prime * result + field.hashCode();
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Metadata other = (Metadata) obj;
        if (metaFields.size() != other.metaFields.size()) {
            return false;
        }
        for (MetaField field : other.metaFields) {
            if (!metaFields.contains(field)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return metaFields.toString();
    }

}
