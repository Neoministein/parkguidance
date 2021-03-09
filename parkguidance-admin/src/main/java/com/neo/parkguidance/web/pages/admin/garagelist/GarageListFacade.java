package com.neo.parkguidance.web.pages.admin.garagelist;

import com.github.adminfaces.template.exception.BusinessException;
import com.neo.parkguidance.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;

@Stateless
public class GarageListFacade {

    @Inject
    private ParkingGarageEntityService garageService;

    public void initDataModel(GarageListModel model) {
        model.setParkingGarages(new LazyDataModel<ParkingGarage>() {

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

    public void clearFilter(GarageListModel model) {
        model.setFilter(new Filter<>(new ParkingGarage()));
    }

    public void findGarageById(Integer id, GarageListModel model) {
        if (id == null) {
            throw new BusinessException("Provide Car ID to load");
        }
        model.getSelectedGarages().add(garageService.findById(id));
    }

    public void delete(GarageListModel model) {
        int numCars = 0;
        List<ParkingGarage> list = model.getSelectedGarages();
        if(list != null) {
            for (ParkingGarage selectedCar : list) {
                numCars++;
                garageService.remove(selectedCar);

            }
            model.getSelectedGarages().clear();
            addDetailMessage(numCars + "ParkingGarage deleted successfully!");
        }
    }
}