package com.neo.parkguidance.web.user.pages.search;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.infra.entity.LazyEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.primefaces.model.LazyDataModel;

import javax.ejb.Stateless;
import javax.inject.Inject;

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
