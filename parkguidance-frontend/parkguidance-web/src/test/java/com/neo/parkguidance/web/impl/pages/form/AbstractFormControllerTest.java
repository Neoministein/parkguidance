package com.neo.parkguidance.web.impl.pages.form;

import com.neo.parkguidance.core.entity.ParkingGarage;
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

class AbstractFormControllerTest {

    AbstractFormController controller;

    @Mock
    AbstractFormModel model;

    @Mock
    AbstractFormFacade facade;

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
        controller = Mockito.spy(AbstractFormController.class);

        model = Mockito.mock(AbstractFormModel.class);
        when(controller.getModel()).thenReturn(model);


        facade = Mockito.mock(AbstractFormFacade.class);
        when(controller.getFacade()).thenReturn(facade);

        utilsMock.clearInvocations();
    }



    /**
     * Verifies if the filter value is null
    */
    @Test
    void clearFilterVerifyModel() {
        //Arrange
        when(facade.newEntity()).thenReturn(null);

        //Act
        controller.clear();

        //Assert
        Mockito.verify(model).setPrimaryKey(null);
    }

    /**
     * Verifies if the facade gets called to clear the filter
     */
    @Test
    void clearFilterVerifyFacade() {
        //Arrange
        when(facade.newEntity()).thenReturn(null);

        //Act
        controller.clear();

        //Assert

        Mockito.verify(model).setEntity(any());
    }

    /**
     * Verifies if the facade gets called to delete an item
     */
    @Test
    void deleteVerifyFacade() {
        //Arrange
        doReturn(false).when(facade).remove(any());
        doReturn(new ParkingGarage()).when(model).getEntity();
        doReturn("null").when(controller).getRedirectLocation();

        //Act
        controller.remove();

        //Assert

        Mockito.verify(facade).remove(any());
    }


    /**
     * Verifies if the facade gets called to delete an item
     */
    @Test
    void createVerifyFacade() {
        //Arrange
        doReturn(new ParkingGarage()).when(model).getEntity();
        doNothing().when(facade).create(any());

        //Act
        controller.save();

        //Assert

        Mockito.verify(facade).create(any());
    }


    /**
     * Verifies if the facade gets called to delete an item
     */
    @Test
    void editVerifyFacade() {
        //Arrange
        when(model.getPrimaryKey()).thenReturn("");
        when(model.getEntity()).thenReturn(new ParkingGarage());
        doReturn(false).when(facade).remove(any());

        //Act
        controller.save();

        //Assert

        Mockito.verify(facade).edit(any());
    }
}
