package com.neo.parkguidance.web.admin.pages.garagelist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class GarageListControllerTest {

    GarageListController subject;

    @Mock
    GarageListModel model;

    @Mock
    GarageListFacade facade;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(GarageListController.class);

        model = mock(GarageListModel.class);
        subject.model = model;

        facade = mock(GarageListFacade.class);
        subject.facade = facade;
    }

    /**
     * Verifies if the filter value is null
     */
    @Test
    void clearFilterVerifyModel() {
        //Arrange
        //Act
        subject.clearFilter();

        //Assert

        verify(model).setFilter(null);
    }

    /**
     * Verifies if the facade gets called to clear the filter
     */
    @Test
    void clearFilterVerifyFacade() {
        //Arrange
        //Act
        subject.clearFilter();

        //Assert

        verify(facade).newFilter();
    }

    /**
     * Verifies if the facade gets called to delete an item
     */
    @Test
    void deleteVerifyFacade() {
        when(facade.delete(any())).thenReturn(0);

        subject.delete();

        //Assert

        verify(facade).delete(any());
    }
}
