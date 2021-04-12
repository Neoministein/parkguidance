package com.neo.parkguidance.web.admin.pages.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;
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
    AbstractEntityDao<ParkingGarage> garageService;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(GarageListFacade.class);

        garageService = mock(ParkingGarageEntityManager.class);

        subject.entityDao = garageService;
    }

    /**
     * Verifies if the facade returns the right amount
     */
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

    /**
     * Verifies if the dao gets called to delete a object
     */
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
