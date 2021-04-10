package com.neo.parkguidance.web.admin.pages.sheetlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class SheetListControllerTest {

    DataSheetListController subject;

    @Mock DataSheetListModel model;

    @Mock DataSheetListFacade facade;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(DataSheetListController.class);

        model = mock(DataSheetListModel.class);
        subject.model = model;

        facade = mock(DataSheetListFacade.class);
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
     * Verifies if the facade gets called to clear the filter
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
