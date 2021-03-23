package com.neo.parkguidance.user.web.pages.parklist;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class ParkListFacade {

    private static final long TIME_BETWEEN_UPDATES = 5000;

    @Inject
    private ParkingDataEntityManager parkingDataManager;

    @Inject
    private ParkingGarageEntityManager parkingGarageManager;

    public void initDataModel(ParkListModel model) {
        if(model.getLastUpdate().getTime() + TIME_BETWEEN_UPDATES < System.currentTimeMillis()) {
            Date now = new Date();
            model.setLastUpdate(new Date());

            List<ParkingData> parkingData = new ArrayList<>();
            for (ParkingGarage parkingGarage: parkingGarageManager.findAll()) {

                List<ParkingData> daoList = parkingDataManager.getBeforeDate(now, parkingGarage);

                if(daoList.isEmpty()) {
                    parkingData.add(new ParkingData(parkingGarage,null,0));
                } else {
                    parkingData.add(daoList.get(0));
                }
            }
            model.setParkingData(parkingData);
        }
    }
}
