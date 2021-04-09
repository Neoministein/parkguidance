package com.neo.parkguidance.web.admin.pages.datalist;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.web.infra.entity.ParkingDataEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;

@Stateless
public class ParkingDataListFacade {

    public static final long TWO_WEEKS = 1209600000;

    @Inject
    ParkingDataEntityService parkingDataService;

    public void initDataModel(ParkingDataListModel model) {
        model.setData(new LazyDataModel<ParkingData>() {
            @Override
            public List<ParkingData> load(int first, int pageSize,
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
                List<ParkingData> list = parkingDataService.paginate(model.getFilter());
                setRowCount((int) parkingDataService.count(model.getFilter()));

                return list;
            }

            @Override
            public ParkingData getRowData(String key) {
                return parkingDataService.find(Long.valueOf(key));
            }
        });
    }

    public Filter<ParkingData> newFilter() {
         return new Filter<>(new ParkingData());
    }

    public ParkingData findById(int id) {
        return parkingDataService.find(Long.valueOf(id));
    }

    public int delete(List<ParkingData> list) {
        int data = 0;
        if(list != null) {
            for (ParkingData selectedCar : list) {
                data++;
                parkingDataService.remove(selectedCar);

            }

        }
        return data;
    }

    public int deleteOld() {
        int deleted = 0;
        List<ParkingData> list = parkingDataService.getBeforeDate(new Date(System.currentTimeMillis() - TWO_WEEKS));

        if (list != null) {
            for(ParkingData parkingData: list) {
                deleted++;
                parkingDataService.remove(parkingData);
            }
        }
        return deleted;
    }
}
