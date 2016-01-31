package com.plexobject.dp.sample.dao;

import java.util.Collection;
import java.util.Map;

import com.plexobject.dp.sample.domain.Idable;

public interface BaseDao<Id, Type extends Idable<Id>> {
    void save(Type type);

    Type getById(Id key);

    Collection<Type> getByIdList(Collection<Id> keys);

    Collection<Type> getByIds(Id[] keys);

    Collection<Type> getAll();

    Collection<Type> query(Map<String, Object> criteria);

    Collection<Type> filter(Filter<Type> filter);

    boolean remove(Id key);

    void clear();

}
