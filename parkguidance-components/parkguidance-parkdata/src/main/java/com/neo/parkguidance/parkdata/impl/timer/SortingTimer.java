package com.neo.parkguidance.parkdata.impl.timer;

import com.neo.parkguidance.core.impl.timer.AbstractApplicationTimer;
import com.neo.parkguidance.parkdata.api.service.ParkDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Local;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 * This timer is responsible for sorting and then deleting the old park data on a daily basis
 */
@Local
@Singleton
public class SortingTimer extends AbstractApplicationTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SortingTimer.class);

    @Inject
    ParkDataService parkDataSorter;

    public SortingTimer() {
        super(new ScheduleExpression().hour("*/24"));
    }

    @Override
    public void poll() {
        LOGGER.info("Sorting ParkData...");
        parkDataSorter.sortParkData();
        LOGGER.info("Finished sorting");
        LOGGER.info("Deleting raw sortedParkData...");
        parkDataSorter.deleteRawSortedData();
        LOGGER.info("Finished deleting...");
    }
}
