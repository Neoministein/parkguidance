package com.neo.parkguidance.web.impl.pages.lazy;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.DataBaseEntity;
import com.neo.parkguidance.web.impl.entity.LazyEntityService;
import com.neo.parkguidance.web.impl.table.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.LazyDataModel;

import javax.inject.Inject;
import java.util.List;

public abstract class AbstractLazyFacade<T extends DataBaseEntity> {

    private static final Logger LOGGER = LogManager.getLogger(AbstractLazyFacade.class);

    @Inject
    EntityDao<T> entityDao;

    public LazyDataModel<T> initDataModel(Filter<T> filter) {
        return new LazyEntityService<>(entityDao, filter);
    }

    public abstract Filter<T> newFilter();

    public int delete(List<T> selection) {
        int num = 0;
        if(selection != null) {
            for (T selected : selection) {
                num++;
                entityDao.remove(selected);
                LOGGER.info("Deleting {} [{}]", selected.getClass().getName(), selected.getPrimaryKey());
            }
        }
        return num;
    }
}
