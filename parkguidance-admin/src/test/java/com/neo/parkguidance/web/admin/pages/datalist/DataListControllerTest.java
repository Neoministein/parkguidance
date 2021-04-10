package com.neo.parkguidance.web.admin.pages.datalist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class DataListControllerTest {

    ParkingDataListController subject;

    @Mock
    ParkingDataListModel model;

    @Mock
    ParkingDataListFacade facade;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(ParkingDataListController.class);

        model = mock(ParkingDataListModel.class);
        subject.model = model;

        facade = mock(ParkingDataListFacade.class);
        subject.facade = facade;
    }

    /**
     * Verifies if the filter value is null
     */
    @Test
    void clearFilterVerifyModel() {
        //Arrange
        //Act
        subject.clear();

        //Assert

        verify(model).setFilter(null);
    }

    /**
     * Verifies if the facade receives the clear call
     */
    @Test
    void clearFilterVerifyFacade() {
        //Arrange
        //Act
        subject.clear();

        //Assert

        verify(facade).newFilter();
    }

    /**
     * Verifies if the facade receives the delete call
     */
    @Test
    void deleteVerifyFacade() {
        when(facade.delete(any())).thenReturn(0);

        subject.delete();

        //Assert

        verify(facade).delete(any());
    }
}
