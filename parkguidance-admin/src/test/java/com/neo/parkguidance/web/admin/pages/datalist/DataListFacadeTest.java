package com.neo.parkguidance.web.admin.pages.datalist;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.entity.ParkingDataEntityService;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DataListFacadeTest {

    ParkingDataListFacade subject;

    @Mock
    ParkingDataEntityService parkingDataService;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(ParkingDataListFacade.class);

        parkingDataService = mock(ParkingDataEntityService.class);

        subject.parkingDataService = parkingDataService;
    }

    @Test
    void deleteAmount() {
        //Arrange

        List<ParkingData> parkingGarageList = new ArrayList<>();
        parkingGarageList.add(new ParkingData());
        parkingGarageList.add(new ParkingData());
        parkingGarageList.add(new ParkingData());

        int expected = 3;
        //Act
        int result = subject.delete(parkingGarageList);

        //Assert

        assertEquals(expected,result);
    }

    @Test
    void deleteVerifyDao() {
        //Arrange

        List<ParkingData> parkingGarageList = new ArrayList<>();
        parkingGarageList.add(new ParkingData());

        //Act
        subject.delete(parkingGarageList);

        //Assert

        verify(parkingDataService).remove(parkingGarageList.get(0));
    }
}
