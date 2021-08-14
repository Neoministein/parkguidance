package com.neo.parkguidance.web.impl.pages.form;

import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

import static com.github.adminfaces.template.util.Assert.has;

public abstract class AbstractFormFacade<T extends DataBaseEntity<T>> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractFormFacade.class);

    @Inject
    AbstractEntityDao<T> dao;

    public abstract T newEntity();

    public T findGarageById(Object key) {
        return dao.find(key);
    }

    public boolean remove(T entity) {
        if (has(entity) && has(entity.getPrimaryKey())) {
            dao.remove(entity);
            LOGGER.info("Deleting {} [{}]", entity.getClass().getName() ,entity.getPrimaryKey());
            return true;
        } else {
            return false;
        }
    }

    public void edit(T entity) {
        dao.edit(entity);
        LOGGER.info("Updated {} [{}]", entity.getClass().getName() ,entity.getPrimaryKey());
    }

    public void create(T entity) {
        dao.create(entity);
        LOGGER.info("Created {} [{}]" ,entity.getClass().getName() ,entity.getPrimaryKey());
    }

    protected AbstractEntityDao<T> getDao() {
        return dao;
    }
}