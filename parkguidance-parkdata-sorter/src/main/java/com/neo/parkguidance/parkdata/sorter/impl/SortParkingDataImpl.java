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

    public static final int MILLISECONDS_IN_HOUR = 3600000;
    public static final int MILLISECONDS_IN_HALF_AN_HOUR = MILLISECONDS_IN_HOUR / 2;

    @Inject
    ParkingDataEntityManager parkingDataManager;

    @Inject
    ParkingGarageEntityManager parkingGarageManager;

    @Inject
    DataSheetEntityManager dataSheetManager;

    public void sortParkingData() {
        List<ParkingData> parkingDataList = parkingDataManager.findUnsorted();
        List<ParkingGarage> allParkingGarages = parkingGarageManager.findAll();

        if(parkingDataList.isEmpty()) {
           return;
        }
        Date startDate = roundDown(parkingDataList.get(0).getDate());
        Date endDate = roundDown(parkingDataList.get(parkingDataList.size()-1).getDate());

        long interval = (endDate.getTime() - startDate.getTime()) / MILLISECONDS_IN_HALF_AN_HOUR;

        for(int i = 0; i <= interval; i++) {
            Date entryStart = new Date(startDate.getTime() + MILLISECONDS_IN_HALF_AN_HOUR * i);
            Date entryEnd = new Date(startDate.getTime() + MILLISECONDS_IN_HALF_AN_HOUR * (i + 1));

            for(ParkingGarage entryGarage: allParkingGarages) {
                DataSheet entrySheet = dataSheetFromDate(entryStart);
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
                } else {
                    entrySheet.setOccupied(getAverageOccupation(entryDataList));
                    entrySheet.setWaitingTime(calculateWaitTime());
                }
                dataSheetManager.create(entrySheet);
            }
        }

        for(ParkingData parkingData :parkingDataList) {
            parkingData.setSorted(true);
            parkingDataManager.edit(parkingData);
        }
    }

    public int calculateWaitTime() {
        return 0;
    }

    public int getAverageOccupation(List<ParkingData> dataList) {
        int avg = 0;
        for(ParkingData parkingData: dataList) {
            avg += parkingData.getOccupied();
        }
        return avg / dataList.size() + 1;
    }

    public DataSheet dataSheetFromDate(Date date) {
        DataSheet dataSheet = new DataSheet();
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        dataSheet.setYear(cl.get(Calendar.YEAR));
        dataSheet.setWeek(cl.get(Calendar.WEEK_OF_YEAR));
        dataSheet.setDay(cl.get(Calendar.DAY_OF_WEEK));
        dataSheet.setHalfHour(halfHourOfDate(date));

        return dataSheet;
    }

    public int halfHourOfDate(Date date) {
        int halfHour;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        halfHour = cal.get(Calendar.HOUR_OF_DAY) * 2;

        cal.set(Calendar.YEAR, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        int half = cal.get(Calendar.MINUTE);
        if(half >= 30) {
            halfHour++;
        }

        return halfHour;
    }

    public Date roundDown(Date date) {
        Date roundedDate = DateUtils.round(date, Calendar.HOUR);

        if(date.getTime() < roundedDate.getTime()) {
            return new Date(roundedDate.getTime()-MILLISECONDS_IN_HOUR);
        }
        return roundedDate;
    }

    public Date roundUp(Date date) {
        Date roundedDate = DateUtils.round(date, Calendar.HOUR);

        if(date.getTime() > roundedDate.getTime()) {
            return new Date(roundedDate.getTime() + MILLISECONDS_IN_HOUR);
        }
        return roundedDate;
    }
}