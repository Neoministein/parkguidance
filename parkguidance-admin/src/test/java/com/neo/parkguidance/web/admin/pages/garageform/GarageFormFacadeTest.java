package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AddressEntityManager;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class GarageFormFacadeTest {

    GarageFormFacade subject;

    @Mock
    AddressEntityManager addressEntityManager;

    @Mock
    ParkingGarageEntityService parkingGarageService;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(GarageFormFacade.class);

        parkingGarageService = mock(ParkingGarageEntityService.class);
        subject.garageService = parkingGarageService;

        addressEntityManager = mock(AddressEntityManager.class);
        subject.addressManager = addressEntityManager;
    }

    @Test
    void removeVerifyDao() {
        //Arrange
        ParkingGarage item = new ParkingGarage();
        item.setId(0L);

        //Act
        subject.remove(item);

        //Assert

        verify(parkingGarageService).remove(any());
    }

    @Test
    void removeFailedVerifyDao() {
        //Arrange
        ParkingGarage item = new ParkingGarage();

        //Act
        subject.remove(item);

        //Assert

        verify(parkingGarageService, never()).remove(any());
    }

    @Test
    void editVerifyDao() {
        //Arrange
        ParkingGarage item = new ParkingGarage();

        //Act
        subject.edit(item);

        //Assert

        verify(parkingGarageService).edit(any());
    }

    @Test
    void createVerifyDao() {
        //Arrange
        ParkingGarage item = new ParkingGarage();

        //Act
        subject.create(item);

        //Assert

        verify(parkingGarageService).create(any());
    }
}
