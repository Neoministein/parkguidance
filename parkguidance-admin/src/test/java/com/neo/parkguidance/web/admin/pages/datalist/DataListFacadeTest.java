package com.neo.parkguidance.web.admin.pages.datalist;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.impl.dao.ParkingDataEntityManager;
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
    ParkingDataEntityManager parkingDataEntityManager;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(ParkingDataListFacade.class);

        parkingDataEntityManager = mock(ParkingDataEntityManager.class);

        subject.parkingDataDao = parkingDataEntityManager;
    }

    /**
     * Verifies if the facade returns the right amount
     */
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

    /**
     * Verifies if the dao gets called to delete a object
     */
    @Test
    void deleteVerifyDao() {
        //Arrange

        List<ParkingData> parkingGarageList = new ArrayList<>();
        parkingGarageList.add(new ParkingData());

        //Act
        subject.delete(parkingGarageList);

        //Assert

        verify(parkingDataEntityManager).remove(parkingGarageList.get(0));
    }
}
