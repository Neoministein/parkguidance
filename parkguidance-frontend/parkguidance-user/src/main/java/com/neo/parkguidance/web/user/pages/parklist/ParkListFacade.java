package com.neo.parkguidance.web.user.pages.parklist;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.ParkingGarage;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

/**
 * The screen facade for the ParkList screen
 */
@Stateless
public class ParkListFacade {

    private static final long TIME_BETWEEN_UPDATES = 5000; // 5 sec

    @Inject
    EntityDao<ParkingGarage> parkingGarageManager;

    public void initDataModel(ParkListModel model) {
        if(model.getLastUpdate().getTime() + TIME_BETWEEN_UPDATES < System.currentTimeMillis()) {
            model.setLastUpdate(new Date());

            model.setParkingGarageList(parkingGarageManager.findAll(ParkingGarage.C_NAME, true));
        }
    }
}
