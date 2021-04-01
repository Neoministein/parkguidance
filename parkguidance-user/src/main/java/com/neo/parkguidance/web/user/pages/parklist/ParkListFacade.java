package com.neo.parkguidance.web.user.pages.parklist;

import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Date;

@Stateless
public class ParkListFacade {

    private static final long TIME_BETWEEN_UPDATES = 5000;

    @Inject
    private ParkingGarageEntityService parkingGarageManager;

    public void initDataModel(ParkListModel model) {
        if(model.getLastUpdate().getTime() + TIME_BETWEEN_UPDATES < System.currentTimeMillis()) {
            model.setLastUpdate(new Date());

            model.setParkingGarageList(parkingGarageManager.findAll());
        }
    }
}
