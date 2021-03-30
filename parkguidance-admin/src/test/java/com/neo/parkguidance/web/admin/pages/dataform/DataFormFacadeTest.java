package com.neo.parkguidance.web.admin.pages.dataform;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.web.infra.entity.ParkingDataEntityService;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class DataFormFacadeTest {

    DataFormFacade subject;

    @Mock
    ParkingDataEntityService parkingDataService;

    @Mock
    ParkingGarageEntityService parkingGarageService;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(DataFormFacade.class);

        parkingGarageService = mock(ParkingGarageEntityService.class);
        subject.parkingGarageManager = parkingGarageService;

        parkingDataService = mock(ParkingDataEntityService.class);
        subject.dataService = parkingDataService;
    }

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

    @Test
    void removeFailedVerifyDao() {
        //Arrange
        ParkingData parkingData = new ParkingData();

        //Act
        subject.remove(parkingData);

        //Assert

        verify(parkingDataService, never()).remove(any());
    }

    @Test
    void editVerifyDao() {
        //Arrange
        ParkingData parkingData = new ParkingData();

        //Act
        subject.edit(parkingData);

        //Assert

        verify(parkingDataService).edit(any());
    }

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