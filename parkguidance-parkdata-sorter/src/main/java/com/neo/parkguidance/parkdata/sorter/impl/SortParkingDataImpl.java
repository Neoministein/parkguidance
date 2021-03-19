package com.neo.parkguidance.parkdata.sorter.impl;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.DataSheetEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;
import org.apache.commons.lang3.time.DateUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class SortParkingDataImpl {

    private static final int MILLISECONDS_IN_HOUR = 3600000;
    private static final int MILLISECONDS_IN_HALF_AN_HOUR = MILLISECONDS_IN_HOUR / 2;

    @Inject
    private ParkingDataEntityManager parkingDataManager;

    @Inject ParkingGarageEntityManager parkingGarageManager;

    @Inject
    private DataSheetEntityManager dataSheetManager;

    public void sortParkingData() {
        List<ParkingData> parkingDataList = parkingDataManager.findAll();
        List<ParkingGarage> allParkingGarages = parkingGarageManager.findAll();

        if(parkingDataList.isEmpty()) {
           return;
        }
        Date startDate = getStartDate(parkingDataList);
        Date endDate = getEndDate(parkingDataList);

        long interval = (endDate.getTime() - startDate.getTime()) / MILLISECONDS_IN_HALF_AN_HOUR;

        for(int i = 0; i <= interval; i++) {
            Date entryStart = new Date(startDate.getTime() + MILLISECONDS_IN_HALF_AN_HOUR * i);
            Date entryEnd = new Date(startDate.getTime() + MILLISECONDS_IN_HALF_AN_HOUR * (i + 1));

            for(ParkingGarage entryGarage: allParkingGarages) {
                DataSheet entrySheet = createDataSheetWithDate(entryStart);
                entrySheet.setParkingGarage(entryGarage);

                List<ParkingData> entryDataList = parkingDataManager.getBetweenDate(entryStart, entryEnd, entryGarage);
                if(entryDataList.isEmpty()) {
                    List<ParkingData> before = parkingDataManager.getBeforeDate(entryStart, entryGarage);
                    if(before.isEmpty()) {
                        entrySheet.setOccupied(0);
                        entrySheet.setWaitingTime(0);
                    } else {
                        ParkingData entryData = before.get(0);
                        entrySheet.setOccupied(entryData.getOccupied());
                        entrySheet.setWaitingTime(calculateWaitTime());
                    }
                    break;
                }
                int avgOccupied = 0;
                for(ParkingData parkingData: entryDataList) {
                    avgOccupied += parkingData.getOccupied();
                }
                avgOccupied = avgOccupied / entryDataList.size() + 1;
                entrySheet.setOccupied(avgOccupied);
                entrySheet.setWaitingTime(calculateWaitTime());

                dataSheetManager.create(entrySheet);
            }
        }
    }

    protected int calculateWaitTime() {
        return 0;
    }

    protected DataSheet createDataSheetWithDate(Date date) {
        DataSheet dataSheet = new DataSheet();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        dataSheet.setYear(cl.get(Calendar.YEAR));
        dataSheet.setWeek(cl.get(Calendar.WEEK_OF_YEAR));
        dataSheet.setDay(cl.get(Calendar.DAY_OF_WEEK));
        dataSheet.setHalfHour(cl.get(Calendar.HOUR_OF_DAY) * 2);

        return dataSheet;
    }

    protected Date getStartDate(List<ParkingData> list) {
        Date dataDate = list.get(0).getDate();

        Date roundedDate = DateUtils.round(dataDate, Calendar.HOUR);

        if(dataDate.getTime() < roundedDate.getTime()) {
            return new Date(roundedDate.getTime()-MILLISECONDS_IN_HOUR);
        }
        return roundedDate;
    }

    protected Date getEndDate(List<ParkingData> list) {
        Date dataDate = list.get(list.size()-1).getDate();

        Date roundedDate = DateUtils.round(dataDate, Calendar.HOUR);

        if(roundedDate.getTime() < dataDate.getTime()) {
            return new Date(roundedDate.getTime() + MILLISECONDS_IN_HOUR);
        }
        return roundedDate;
    }
}