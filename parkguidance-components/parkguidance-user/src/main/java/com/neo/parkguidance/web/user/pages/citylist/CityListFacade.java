package com.neo.parkguidance.web.user.pages.citylist;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.ParkingGarage;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

/**
 * The screen facade for the ParkList screen
 */
@Stateless
public class CityListFacade {

    private static final int TIME_BETWEEN_UPDATES = 5 * 1000; // 5 sec

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    public void initDataModel(CityListModel model) {
        if(model.getLastUpdate().getTime() + TIME_BETWEEN_UPDATES < System.currentTimeMillis()) {
            model.setLastUpdate(new Date());

            model.setGarageData(getGarageData());
        }
    }

    public Map<String, List<ParkingGarage>> getGarageData(){
        Map<String, List<ParkingGarage>> data = new HashMap<>();

        for (ParkingGarage parkingGarage: parkingGarageDao.findAll(ParkingGarage.C_NAME, true)) {
            String cityId = parkingGarage.getAddress().getCityName();

            List<ParkingGarage> garagesInCity = data.computeIfAbsent(cityId, k -> new ArrayList<>());
            garagesInCity.add(parkingGarage);
        }
        return data;
    }
}
