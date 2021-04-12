package com.neo.parkguidance.web.user.pages.parklist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class ParkListFacade {

    private static final long TIME_BETWEEN_UPDATES = 5000;

    @Inject
    private AbstractEntityDao<ParkingGarage> parkingGarageManager;

    public void initDataModel(ParkListModel model) {
        if(model.getLastUpdate().getTime() + TIME_BETWEEN_UPDATES < System.currentTimeMillis()) {
            model.setLastUpdate(new Date());

            model.setParkingGarageList(parkingGarageManager.findAll());
        }
    }
}
