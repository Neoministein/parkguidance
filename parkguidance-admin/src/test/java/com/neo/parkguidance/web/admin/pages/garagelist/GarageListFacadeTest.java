package com.neo.parkguidance.web.admin.pages.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GarageListFacadeTest {

    GarageListFacade subject;

    @Mock
    ParkingGarageEntityService garageService;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(GarageListFacade.class);

        garageService = mock(ParkingGarageEntityService.class);

        subject.garageService = garageService;
    }

    @Test
    void deleteAmount() {
        //Arrange

        List<ParkingGarage> parkingGarageList = new ArrayList<>();
        parkingGarageList.add(new ParkingGarage());
        parkingGarageList.add(new ParkingGarage());
        parkingGarageList.add(new ParkingGarage());

        int expected = 3;
        //Act
        int result = subject.delete(parkingGarageList);

        //Assert

        assertEquals(expected,result);
    }

    @Test
    void deleteVerifyDao() {
        //Arrange

        List<ParkingGarage> parkingGarageList = new ArrayList<>();
        parkingGarageList.add(new ParkingGarage());

        //Act
        subject.delete(parkingGarageList);

        //Assert

        verify(garageService).remove(new ParkingGarage());
    }
}
