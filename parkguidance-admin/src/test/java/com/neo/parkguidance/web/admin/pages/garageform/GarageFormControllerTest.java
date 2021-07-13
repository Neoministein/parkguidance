package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.ParkingGarage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

class GarageFormControllerTest {

    GarageFormController subject;

    @Mock
    GarageFormModel model;

    @Mock
    GarageFormFacade facade;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(GarageFormController.class);

        model = mock(GarageFormModel.class);
        subject.model = model;

        facade = mock(GarageFormFacade.class);
        subject.facade = facade;
    }

    /**
     * Verifies if the facade gets called to delete an item
     * @throws IOException due to redirecting to other page in native method
     */
    @Test
    void deleteVerifyFacade() throws IOException {
        when(facade.remove(any())).thenReturn(false);

        subject.remove();

        //Assert

        verify(facade).remove(any());
    }

    /**
     * Verifies if the facade gets called to create an item
     */
    @Test
    void createItem() {
        ParkingGarage item = new ParkingGarage();
        item.setName("Test");

        when(model.getItem()).thenReturn(item);

        //Assert
        try {
            subject.save();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {}

        verify(facade).create(any());
    }

    /**
     * Verifies if the facade gets called to edit an item
     */
    @Test
    void editItem() {
        ParkingGarage item = new ParkingGarage();
        item.setName("Test");
        item.setKey("Test");

        when(model.getKey()).thenReturn("Test");
        when(model.getItem()).thenReturn(item);

        //Assert
        try {
            subject.save();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {}

        verify(facade).edit(any());
    }

    @Test
    void verifyResetAccessKey() {

        try {
            subject.resetAccessKey();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {}

        verify(facade).setAccessKey(any());
    }
}
