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

        List<ParkingGarage> list = new ArrayList<>();
        list.add(new ParkingGarage());
        list.add(new ParkingGarage());
        list.add(new ParkingGarage());

        int expected = 3;
        //Act
        int result = subject.delete(list);

        //Assert

        assertEquals(expected,result);
    }

    @Test
    void deleteVerifyDao() {
        //Arrange

        List<ParkingGarage> list = new ArrayList<>();
        list.add(new ParkingGarage());

        //Act
        subject.delete(list);

        //Assert

        verify(garageService).remove(list.get(0));
    }
}
