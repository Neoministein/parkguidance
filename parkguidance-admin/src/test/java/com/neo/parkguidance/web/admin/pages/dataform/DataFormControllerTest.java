package com.neo.parkguidance.web.admin.pages.dataform;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

class DataFormControllerTest {

    DataFormController subject;

    @Mock
    DataFormModel model;

    @Mock
    DataFormFacade facade;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(DataFormController.class);

        model = mock(DataFormModel.class);
        subject.model = model;

        facade = mock(DataFormFacade.class);
        subject.facade = facade;
    }

    /**
     * Verifies if the facade gets called to remove
     * @throws IOException
     */
    @Test
    void deleteVerifyFacade() throws IOException {
        when(facade.remove(any())).thenReturn(false);

        subject.remove();

        //Assert

        verify(facade).remove(any());
    }

    /**
     * Verifies if the facade gets to create a item
     */
    @Test
    void createItem() {
        ParkingGarage garage = new ParkingGarage();
        garage.setName("Test");

        ParkingData item = new ParkingData();
        item.setParkingGarage(garage);

        when(model.getItem()).thenReturn(item);

        //Assert
        try {
            subject.save();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {}

        verify(facade).create(any());
    }

    /**
     * Verifies if the facade gets to edit a item
     */
    @Test
    void editItem() {
        ParkingGarage garage = new ParkingGarage();
        garage.setName("Test");

        ParkingData item = new ParkingData();
        item.setId(1L);
        item.setParkingGarage(garage);

        when(model.getItem()).thenReturn(item);

        //Assert
        try {
            subject.save();
        } catch (ExceptionInInitializerError | NoClassDefFoundError e) {}

        verify(facade).edit(any());
    }
}
