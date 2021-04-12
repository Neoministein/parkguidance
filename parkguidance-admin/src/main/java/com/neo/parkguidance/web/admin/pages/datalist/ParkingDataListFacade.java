package com.neo.parkguidance.web.admin.pages.datalist;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
import com.neo.parkguidance.web.infra.entity.LazyEntityService;
import com.neo.parkguidance.web.infra.table.Filter;
import org.primefaces.model.LazyDataModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;
import java.util.List;

@Stateless
public class ParkingDataListFacade {

    public static final long TWO_WEEKS = 1209600000;

    @Inject
    ParkingDataEntityManager parkingDataDao;

    public LazyDataModel initDataModel(Filter<ParkingData> filter) {
        return new LazyEntityService<>(parkingDataDao, filter);
    }

    public Filter<ParkingData> newFilter() {
         return new Filter<>(new ParkingData());
    }

    public ParkingData findById(int id) {
        return parkingDataDao.find(Long.valueOf(id));
    }

    public int delete(List<ParkingData> list) {
        int data = 0;
        if(list != null) {
            for (ParkingData selectedCar : list) {
                data++;
                parkingDataDao.remove(selectedCar);

            }

        }
        return data;
    }

    public int deleteOld() {
        int deleted = 0;
        List<ParkingData> list = parkingDataDao.getBeforeDate(new Date(System.currentTimeMillis() - TWO_WEEKS));

        if (list != null) {
            for(ParkingData parkingData: list) {
                if (Boolean.TRUE.equals(parkingData.getSorted())) {
                    deleted++;
                    parkingDataDao.remove(parkingData);
                }
            }
        }
        return deleted;
    }
}
