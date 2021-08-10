package com.neo.parkguidance.web.user.impl.data;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.parkdata.impl.service.ParkDataService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.primefaces.model.charts.line.LineChartDataSet;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class generates {@link LineChartDataSet} for the data screen
 */
@Stateless
public class DataChartService {

    private static final Logger LOGGER = LogManager.getLogger(DataChartService.class);

    private static final int HALF_HOURS_IN_DAY = 48;

    @Inject
    AbstractEntityDao<ParkingGarage> dao;

    @Inject
    ParkDataService parkDataService;

    public Map<String, LineChartDataSet> loadDataSet() {
        LOGGER.info("Loading Chart Dataset");
        Map<String, LineChartDataSet> dataSetMap = new HashMap<>();

        for(ParkingGarage parkingGarage: dao.findAll()) {
            LOGGER.info("Loading [{}]", parkingGarage.getKey());
            List<Object> averageOccupied = new ArrayList<>(parkDataService.getParkData(parkingGarage.getKey()));
            LineChartDataSet dataSet = new LineChartDataSet();
            dataSet.setData(averageOccupied);
            dataSet.setLabel("Free Spaces");
            dataSet.setYaxisID("left-y-axis");
            dataSet.setBorderColor("rgb(150,204,57)");
            dataSet.setBackgroundColor("rgb(167,224,116, 0.2)");

            dataSetMap.put(parkingGarage.getKey(), dataSet);
        }

        return dataSetMap;
    }


    public List<String> createChartLabel() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {
            labels.add(i/2 + ":" + (( i % 2 == 0 ) ? "00" : "30"));
        }
        return labels;
    }
}
