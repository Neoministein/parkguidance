package com.neo.parkguidance.web.user.pages.parklist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.api.dao.AbstractEntityDao;

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
    AbstractEntityDao<ParkingGarage> parkingGarageManager;

    public void initDataModel(ParkListModel model) {
        if(model.getLastUpdate().getTime() + TIME_BETWEEN_UPDATES < System.currentTimeMillis()) {
            model.setLastUpdate(new Date());

            model.setParkingGarageList(parkingGarageManager.findAll(ParkingGarage.C_NAME, true));
        }
    }

    public String getStyleColorOfOccupied(int occupied, int spaces) {
       double percent = 100d / spaces * occupied;

        if (percent > 20) {
            return "color: #32CD32;"; //LimeGreen
        }


        if (percent > 5) {
            return "color: orange;";
        }
        return "color: red;";
    }
}
