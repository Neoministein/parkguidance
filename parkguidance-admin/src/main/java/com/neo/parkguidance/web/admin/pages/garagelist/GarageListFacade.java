package com.neo.parkguidance.web.admin.pages.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Stateless
public class GarageListFacade {

    @Inject
    ParkingGarageEntityService garageService;

    public void initDataModel(GarageListModel model) {
        model.setData(new LazyDataModel<ParkingGarage>() {

            @Override
            public List<ParkingGarage> load(int first, int pageSize,
                    String sortField, SortOrder sortOrder,
                    Map<String, FilterMeta> filters) {
                com.neo.parkguidance.web.infra.table.SortOrder order = null;
                if (sortOrder != null) {
                    switch (sortOrder) {
                    case UNSORTED:
                        order = com.neo.parkguidance.web.infra.table.SortOrder.UNSORTED;
                        break;
                    case ASCENDING:
                        order = com.neo.parkguidance.web.infra.table.SortOrder.ASCENDING;
                        break;
                    case DESCENDING:
                        order =  com.neo.parkguidance.web.infra.table.SortOrder.DESCENDING;
                        break;
                    }
                }
                model.getFilter().setFirst(first).setPageSize(pageSize)
                        .setSortField(sortField).setSortOrder(order)
                        .setParams(filters);
                List<ParkingGarage> list = garageService.paginate(model.getFilter());
                setRowCount((int) garageService.count(model.getFilter()));

                return list;
            }

            @Override
            public ParkingGarage getRowData(String key) {
                return garageService.findById(Integer.valueOf(key));
            }
        });
    }

    public Filter<ParkingGarage> newFilter() {
         return new Filter<>(new ParkingGarage());
    }

    public int delete(List<ParkingGarage> selected) {
        int numCars = 0;
        if(selected != null) {
            for (ParkingGarage selectedCar : selected) {
                numCars++;
                garageService.remove(selectedCar);

            }
        }
        return numCars;
    }
}
