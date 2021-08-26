package com.neo.parkguidance.web.impl.pages.lazy;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractLazyFacadeTest {

    AbstractLazyFacade facade;

    @Mock
    AbstractEntityDao entityDao;


    @BeforeEach
    public void setUp() {
        facade = Mockito.spy(AbstractLazyFacade.class);


        entityDao = Mockito.mock(AbstractEntityDao.class);
        facade.entityDao = entityDao;
    }

    /**
     * Verifies if the facade gets called to delete an item
     */
    @Test
    void deleteNone() {
        //Arrange
        int expected = 0;
        int result;

        //Act
        result = facade.delete(new ArrayList());

        //Assert

        assertEquals(expected, result);
    }


    /**
     * Verifies if the facade gets called to delete an item
     */
    @Test
    void deleteMultiple() {
        //Arrange
        List<ParkingGarage> list = Arrays.asList(new ParkingGarage(), new ParkingGarage());

        int expected = 2;
        int result;

        //Act
        result = facade.delete(list);

        //Assert

        assertEquals(expected, result);
    }
}
