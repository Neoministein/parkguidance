package com.neo.parkguidance.web.admin.timer;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.timer.AbstractApplicationTimer;
import com.neo.parkguidance.core.impl.utils.TimeUtils;
import com.neo.parkguidance.parkdata.api.service.ParkDataService;
import com.neo.parkguidance.parkdata.impl.data.ParkDataObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Local
@Singleton
public class GarageNoReponseTimer extends AbstractApplicationTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarageNoReponseTimer.class);

    private static final int TIME_TO_DEACTIVATE = 20 * 1000;

    @Inject
    ParkDataService parkDataSorter;

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    public GarageNoReponseTimer() {
        super(new ScheduleExpression().hour("*").minute("*/20"));
    }

    @Override
    public void poll() {
        LOGGER.info("Checking if parking garages haven't been updated");
        List<ParkingGarage> garageList = new ArrayList<>();
        Date timeToDeactivate = new Date(System.currentTimeMillis() - TIME_TO_DEACTIVATE);
        for (ParkingGarage parkingGarage: parkingGarageDao.findAll()) {
            ParkDataObject parkDataObject = parkDataSorter.getMetaData(parkingGarage);
            LOGGER.debug("Garage {} has been last updated at {}", parkingGarage.getKey(),
                    parkDataObject.getLastUpdate() != null ? parkDataObject.getLastUpdate() : "Unknown");
            if (TimeUtils.after(parkDataObject.getLastUpdate(), timeToDeactivate)) {
                garageList.add(parkingGarage);
                parkingGarage.setOccupied(-1);
                parkingGarageDao.edit(parkingGarage);
            }
        }
        LOGGER.info("These parking garage haven't been updated {}", garageList);
    }
}
