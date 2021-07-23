package com.neo.parkguidance.web.admin.pages.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
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
 * The screen facade for the GarageList screen
 */
@Stateless
public class GarageListFacade {

    private static final Logger LOGGER = LogManager.getLogger(GarageListFacade.class);

    @Inject
    AbstractEntityDao<ParkingGarage> entityDao;

    public LazyDataModel<ParkingGarage> initDataModel(Filter<ParkingGarage> filter) {
        return new LazyEntityService<>(entityDao, filter);
    }

    public Filter<ParkingGarage> newFilter() {
        return new Filter<>(new ParkingGarage());
    }

    public int delete(List<ParkingGarage> selected) {
        int num = 0;
        if(selected != null) {
            for (ParkingGarage selectedGarage : selected) {
                num++;
                entityDao.remove(selectedGarage);
                LOGGER.info("Deleting ParkingGarage [{}]", selectedGarage.getKey());
            }
        }
        return num;
    }
}
