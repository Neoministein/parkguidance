package com.neo.parkguidance.web.admin.pages.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.infra.entity.LazyEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.primefaces.model.LazyDataModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.function.Predicate;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class GarageListFacade {

    @Inject
    AbstractEntityDao<ParkingGarage> entityDao;

    public LazyDataModel<ParkingGarage> initDataModel(Filter<ParkingGarage> filter) {
        LazyEntityService<ParkingGarage> lazyEntityService = new LazyEntityService<>(entityDao, filter);
        lazyEntityService.setConfigFilter(configFilter -> {
            List<Predicate<ParkingGarage>> predicates = lazyEntityService.predicates();

            if (has(configFilter.getEntity())) {
                ParkingGarage filterEntity = configFilter.getEntity();
                if (has(filterEntity.getName())) {
                    Predicate<ParkingGarage> namePredicate = (ParkingGarage c) -> c.getName().toLowerCase().contains(filterEntity.getName().toLowerCase());
                    predicates.add(namePredicate);
                }
            }
            return predicates;
        });
        return lazyEntityService;
    }

    public Filter<ParkingGarage> newFilter() {
         return new Filter<>(new ParkingGarage());
    }

    public int delete(List<ParkingGarage> selected) {
        int numCars = 0;
        if(selected != null) {
            for (ParkingGarage selectedCar : selected) {
                numCars++;
                entityDao.remove(selectedCar);

            }
        }
        return numCars;
    }
}
