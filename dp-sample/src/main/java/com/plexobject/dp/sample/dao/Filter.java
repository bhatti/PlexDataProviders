package com.plexobject.dp.sample.dao;

public interface Filter<T> {
    boolean accept(T object);
}
