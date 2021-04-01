package com.neo.parkguidance.parkdata.sorter.impl;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AddressEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.Date;

@Stateless
public class SortParkingDataIT {

    public static final int HALF_HOURS_IN_DAY = 48;
    public static final int MILLI_PER_FIVE_MINUTES = 300000;
    public static final int MILLI_PER_HALF_HOUR = 1800000;



    ParkingGarage parkingGarage;

    @Inject
    SortParkingDataImpl subject;

    @Inject
    ParkingDataEntityManager parkingDataEntityManager;

    @Inject
    ParkingGarageEntityManager parkingGarageEntityManager;

    @Inject AddressEntityManager addressEntityManager;


    public void sortParkingDataTest() {
        //Arrange
        setUpParkingGarage();
        setUpParkingData();

        subject.sortParkingData();
    }

    protected void setUpParkingGarage() {
        Address address = new Address();
        address.setCityName("1");
        address.setPlz(2);
        address.setStreet("3");
        address.setNumber(4);

        addressEntityManager.create(address);

        parkingGarage = new ParkingGarage();
        parkingGarage.setAccessKey("IntegrationTest");
        parkingGarage.setName("SortParkingDataIT");
        parkingGarage.setOccupied(0);
        parkingGarage.setSpaces(100);
        parkingGarage.setAddress(address);

        parkingGarageEntityManager.create(parkingGarage);
    }

    protected void setUpParkingData() {
        int currentCount = 1;

        for (int i = 0; i < HALF_HOURS_IN_DAY; i++) {
            ParkingData parkingData = new ParkingData();
            parkingData.setParkingGarage(parkingGarage);


            if(i < HALF_HOURS_IN_DAY/2) {
                for(int j = 0; j < 4;j++) {
                    parkingData.setDate(new Date(i* MILLI_PER_HALF_HOUR + j * MILLI_PER_FIVE_MINUTES));
                    parkingData.setOccupied(currentCount++);
                }

            } else {
                for(int j = 0; j < 4;j++) {
                    parkingData.setDate(new Date(i* MILLI_PER_HALF_HOUR + j * MILLI_PER_FIVE_MINUTES));
                    parkingData.setOccupied(currentCount--);
                }
            }
            parkingDataEntityManager.create(parkingData);
        }
    }


}
