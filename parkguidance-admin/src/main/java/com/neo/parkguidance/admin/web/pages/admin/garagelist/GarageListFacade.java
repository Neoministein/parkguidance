package com.neo.parkguidance.admin.web.pages.admin.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.admin.infra.entity.ParkingGarageEntityService;
import com.neo.parkguidance.admin.infra.table.Filter;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.neo.parkguidance.admin.web.utils.Utils.addDetailMessage;

@Stateless
public class GarageListFacade {

    @Inject
    private ParkingGarageEntityService garageService;

    public void initDataModel(GarageListModel model) {
        model.setData(new LazyDataModel<ParkingGarage>() {

            @Override
            public List<ParkingGarage> load(int first, int pageSize,
                    String sortField, SortOrder sortOrder,
                    Map<String, FilterMeta> filters) {
                com.neo.parkguidance.admin.infra.table.SortOrder order = null;
                if (sortOrder != null) {
                    switch (sortOrder) {
                    case UNSORTED:
                        order = com.neo.parkguidance.admin.infra.table.SortOrder.UNSORTED;
                        break;
                    case ASCENDING:
                        order = com.neo.parkguidance.admin.infra.table.SortOrder.ASCENDING;
                        break;
                    case DESCENDING:
                        order =  com.neo.parkguidance.admin.infra.table.SortOrder.DESCENDING;
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

    public void clearFilter(GarageListModel model) {
        model.setFilter(new Filter<>(new ParkingGarage()));
    }

    public void delete(GarageListModel model) {
        int numCars = 0;
        List<ParkingGarage> list = model.getSelected();
        if(list != null) {
            for (ParkingGarage selectedCar : list) {
                numCars++;
                garageService.remove(selectedCar);

            }
            model.getSelected().clear();
            addDetailMessage(numCars + "ParkingGarage deleted successfully!");
        }
    }
}
