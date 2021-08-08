package com.neo.parkguidance.web.admin.pages.storedvalueform;

import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import static com.github.adminfaces.template.util.Assert.has;

/**
 * The screen facade for the StoredValueForm screen
 */
@Stateless
public class StoredValueFormFacade {

    private static final Logger LOGGER = LogManager.getLogger(StoredValueFormFacade.class);

    @Inject
    AbstractEntityDao<StoredValue> dao;

    public StoredValue find(String key) {
        return dao.find(key);
    }

    public void save(StoredValue entity) {
        dao.create(entity);
    }

    public boolean remove(StoredValue storedValue) {
        if (has(storedValue) && has(storedValue.getKey())) {
            dao.remove(storedValue);
            LOGGER.info("Deleting StoredValue [{}]", storedValue.getKey());
            return true;
        } else {
            return false;
        }
    }

    public void edit(StoredValue storedValue, String valueHidden) {
        if (Boolean.TRUE.equals(storedValue.getHidden())) {
            storedValue.setValue(valueHidden);
        }

        dao.edit(storedValue);
        LOGGER.info("Updated StoredValue [{}]", storedValue.getKey());
    }

    public void create(StoredValue storedValue) {
        if (storedValue.getHidden() == null) {
            storedValue.setHidden(Boolean.FALSE);
        }

        dao.create(storedValue);
        LOGGER.info("Created StoredValue [{}]", storedValue.getKey());
    }
}
