package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.AbstractEntityDao;
import com.neo.parkguidance.core.entity.StoredValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class StoredValueEntityManager extends AbstractEntityDao<StoredValue> {

    private static final Logger LOGGER = LogManager.getLogger(StoredValueEntityManager.class);

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
        StoredValue storedValues = find(value);

        if (storedValues != null) {
            return storedValues;
        }

        LOGGER.error("Unable to find the stored value {} in the database" , value);
        throw new IllegalArgumentException(getClass().getName() + " has no entry for the key " + value);
    }
}
