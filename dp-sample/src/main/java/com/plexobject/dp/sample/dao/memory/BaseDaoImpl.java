package com.plexobject.dp.sample.dao.memory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.plexobject.dp.sample.dao.BaseDao;
import com.plexobject.dp.sample.dao.Filter;
import com.plexobject.dp.sample.domain.Idable;

public abstract class BaseDaoImpl<Id, Type extends Idable<Id>> implements
        BaseDao<Id, Type> {
    protected Map<Id, Type> store = new HashMap<>();

    @Override
    public void save(Type value) {
        store.put(value.getId(), value);
    }

    @Override
    public Type getById(Id key) {
        return store.get(key);
    }

    @Override
    public Collection<Type> getAll() {
        return store.values();
    }

    @Override
    public Collection<Type> getByIds(Id[] keys) {
        Collection<Type> result = new HashSet<>();
        for (Id key : keys) {
            Type value = store.get(key);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    @Override
    public Collection<Type> getByIdList(Collection<Id> keys) {
        Collection<Type> result = new HashSet<>();
        for (Id key : keys) {
            Type value = store.get(key);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    @Override
    public Collection<Type> filter(Filter<Type> filter) {
        Collection<Type> result = new HashSet<>();
        for (Map.Entry<Id, Type> e : store.entrySet()) {
            if (filter.accept(e.getValue())) {
                result.add(e.getValue());
            }
        }
        return result;
    }

    @Override
    public Collection<Type> query(Map<String, Object> criteria) {
        Collection<Type> result = new HashSet<>();
        for (Map.Entry<Id, Type> e : store.entrySet()) {
            int matched = 0;
            for (Map.Entry<String, Object> c : criteria.entrySet()) {
                try {
                    Field f = e.getValue().getClass()
                            .getDeclaredField(c.getKey());
                    f.setAccessible(true);
                    Object value = f.get(e.getValue());
                    if (value != null && value.equals(c.getValue())) {
                        matched++;
                    }
                } catch (Exception ex) {
                }
            }
            if (matched == criteria.size()) {
                result.add(e.getValue());
            }
        }
        return result;
    }

    @Override
    public boolean remove(Id key) {
        return store.remove(key) != null;
    }

    @Override
    public void clear() {
        store.clear();
    }

}
