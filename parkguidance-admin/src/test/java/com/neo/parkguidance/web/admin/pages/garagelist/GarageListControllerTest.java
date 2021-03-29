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

    @Test
    void clearFilterVerifyModel() {
        //Arrange
        //Act
        subject.clear();

        //Assert

        verify(model).setFilter(null);
    }

    @Test
    void clearFilterVerifyFacade() {
        //Arrange
        //Act
        subject.clear();

        //Assert

        verify(facade).newFilter();
    }

    @Test
    void deleteVerifyModel() {
        when(facade.delete(any())).thenReturn(1);

        subject.delete();

        //Assert

        verify(model).getSelected().clear();
    }

    @Test
    void deleteVerifyFacade() {
        when(facade.delete(any())).thenReturn(1);

        subject.delete();

        //Assert

        verify(facade).delete(any());
    }
}
