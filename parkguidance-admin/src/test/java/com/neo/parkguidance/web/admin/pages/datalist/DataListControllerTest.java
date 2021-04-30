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
        //Arrange
        when(facade.delete(any())).thenReturn(0);

        //Act
        subject.delete();

        //Assert
        verify(facade).delete(any());
    }

    /**
     * Verifies if the facade receives the delete old call
     */
    @Test
    void deleteOldVerifyFacade(){
        //Arrange
        when(facade.deleteOld()).thenReturn(0);

        //Act
        try {
            subject.deleteOld();
        }catch (ExceptionInInitializerError | NoClassDefFoundError e) {}

        //Assert
        verify(facade).deleteOld();
    }
}
