package com.neo.parkguidance.web.impl.pages.form;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.DataBaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public abstract class AbstractFormFacade<T extends DataBaseEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormFacade.class);

    @Inject
    protected EntityDao<T> dao;

    public abstract T newEntity();

    public T findEntityById(Object key) {
        return  dao.find(key);
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

    protected EntityDao<T> getDao() {
        return dao;
    }
}
