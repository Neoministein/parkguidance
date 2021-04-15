package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.entity.StoredValue;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class StoredValueEntityManager extends AbstractEntityDao<StoredValue> {

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StoredValueEntityManager() {
        super(StoredValue.class);
    }

    public StoredValue findValue(String value) {
        List<StoredValue> storedValues = findByColumn(StoredValue.C_KEY, value);

        if(!storedValues.isEmpty()) {
            return storedValues.get(0);
        }
        throw new IllegalArgumentException(getClass().getName() + " has not entry for the key " + value);
    }
}
