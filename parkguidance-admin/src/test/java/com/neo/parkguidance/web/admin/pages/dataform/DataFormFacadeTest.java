package com.neo.parkguidance.web.admin.pages.dataform;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class DataFormFacadeTest {

    DataFormFacade subject;

    @Mock
    AbstractEntityDao<ParkingData> parkingDataService;

    @Mock
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(DataFormFacade.class);

        parkingGarageDao = mock(ParkingGarageEntityManager.class);
        subject.parkingGarageDao = parkingGarageDao;

        parkingDataService = mock(ParkingDataEntityManager.class);
        subject.parkingDataDao = parkingDataService;
    }

    /**
     * Verifies if the dao gets the call to remove a item
     */
    @Test
    void removeVerifyDao() {
        //Arrange
        ParkingData parkingData = new ParkingData();
        parkingData.setId(0L);

        //Act
        subject.remove(parkingData);

        //Assert

        verify(parkingDataService).remove(any());
    }

    /**
     * Verifies if the dao doesn't get called
     */
    @Test
    void removeFailedVerifyDao() {
        //Arrange
        ParkingData parkingData = new ParkingData();

        //Act
        subject.remove(parkingData);

        //Assert

        verify(parkingDataService, never()).remove(any());
    }

    /**
     * Verifies if the dao gets called to edit an item
     */
    @Test
    void editVerifyDao() {
        //Arrange
        ParkingData parkingData = new ParkingData();

        //Act
        subject.edit(parkingData);

        //Assert

        verify(parkingDataService).edit(any());
    }

    /**
     * Verifies if the dao gets called to create an item
     */
    @Test
    void createVerifyDao() {
        //Arrange
        ParkingData parkingData = new ParkingData();

        //Act
        subject.create(parkingData);

        //Assert

        verify(parkingDataService).create(any());
    }
}
