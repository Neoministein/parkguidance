package com.neo.parkguidance.web.admin.pages.storedvaluelist;

import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.infra.entity.LazyEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.LazyDataModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * The screen facade for the StoredValueList screen
 */
@Stateless
public class StoredValueListFacade {

    private static final Logger LOGGER = LogManager.getLogger(StoredValueListFacade.class);

    @Inject
    AbstractEntityDao<StoredValue> dao;

    public LazyDataModel<StoredValue> initDataModel(Filter<StoredValue> filter) {
        return new LazyEntityService<>(dao, filter);
    }

    public Filter<StoredValue> newFilter() {
        return new Filter<>(new StoredValue());
    }

    public int delete(List<StoredValue> selected) {
        int num = 0;
        if(selected != null) {
            for (StoredValue selectedValue : selected) {
                num++;
                dao.remove(selectedValue);
                LOGGER.info("Deleting ParkingGarage [{}]", selectedValue.getKey());
            }
        }
        return num;
    }
}
