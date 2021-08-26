package com.neo.parkguidance.web.user.pages.search;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.api.dao.AbstractEntityDao;
import com.neo.parkguidance.web.impl.entity.LazyEntityService;
import com.neo.parkguidance.web.impl.table.Filter;
import org.primefaces.model.LazyDataModel;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * The screen facade for the search screen
 */
@Stateless
public class SearchFacade {

    @Inject
    AbstractEntityDao<ParkingGarage> entityDao;

    public LazyDataModel<ParkingGarage> initDataModel(Filter<ParkingGarage> filter) {
        return new LazyEntityService<>(entityDao, filter);
    }

    public Filter<ParkingGarage> newFilter() {
        return new Filter<>(new ParkingGarage());
    }
}
