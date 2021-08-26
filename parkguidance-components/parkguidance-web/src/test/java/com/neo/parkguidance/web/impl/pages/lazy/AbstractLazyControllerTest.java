package com.neo.parkguidance.web.impl.pages.lazy;

import com.neo.parkguidance.web.impl.table.Filter;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AbstractLazyControllerTest {

    AbstractLazyController controller;

    @Mock AbstractLazyModel model;

    @Mock AbstractLazyFacade facade;

    static MockedStatic <Utils> utilsMock;

    @BeforeAll
    public static void setUpClass() {
        utilsMock = Mockito.mockStatic(Utils.class);
        utilsMock.when(() -> Utils.addDetailMessage(Mockito.any(String.class)))
                .thenAnswer(invocation -> null);
    }

    @AfterAll
    public static void close() {
        utilsMock.close();
    }

    @BeforeEach
    public void setUp() {
        controller = Mockito.spy(AbstractLazyController.class);

        model = Mockito.mock(AbstractLazyModel.class);
        when(controller.getModel()).thenReturn(model);


        facade = Mockito.mock(AbstractLazyFacade.class);
        when(controller.getFacade()).thenReturn(facade);

        utilsMock.clearInvocations();
    }



    /**
     * Verifies if the filter value is null
    */
    @Test
    void clearFilterVerifyModel() {
        //Arrange
        doReturn(new Filter<>()).when(facade).newFilter();

        //Act
        controller.clearFilter();

        //Assert
        Mockito.verify(model).setFilter(any());
    }

    /**
     * Verifies if the facade gets called to clear the filter
     */
    @Test
    void clearFilterVerifyFacade() {
        //Arrange
        doReturn(new Filter<>()).when(facade).newFilter();

        //Act
        controller.clearFilter();

        //Assert

        Mockito.verify(facade).newFilter();
    }

    /**
     * Verifies if the facade gets called to delete an item
     */
    @Test
    void deleteNone() {
        //Arrange
        doReturn(0).when(facade).delete(anyList());

        //Act
        controller.delete();

        //Assert
        utilsMock.verify(() -> Utils.addDetailMessage(anyString()), never());
    }


    /**
     * Verifies if the facade gets called to delete an item
     */
    @Test
    void deleteMultiple() {
        //Arrange
        doReturn(5).when(facade).delete(anyList());

        //Act
        controller.delete();

        //Assert
        utilsMock.verify(() -> Utils.addDetailMessage(anyString()));
    }
}
