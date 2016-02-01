package com.plexobject.dp.sample.dao.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.plexobject.dp.sample.dao.BaseDao;
import com.plexobject.dp.sample.dao.Filter;
import com.plexobject.dp.sample.domain.Idable;

public abstract class BaseDaoImpl<Id, Type extends Idable<Id>> implements
        BaseDao<Id, Type> {
    private final Logger logger = Logger.getLogger(getClass());

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
                    Object value = PropertyUtils.getProperty(e.getValue(),
                            c.getKey());
                    if (value != null && value.equals(c.getValue())) {
                        matched++;
                    }
                } catch (NoSuchMethodException ex) {
                } catch (Exception ex) {
                    ex.printStackTrace();
                    logger.error("Failed to access " + c.getKey(), ex);
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
