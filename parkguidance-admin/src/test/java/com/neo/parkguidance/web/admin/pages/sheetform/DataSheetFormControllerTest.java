package com.neo.parkguidance.web.admin.pages.sheetform;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.admin.pages.garageform.GarageFormController;
import com.neo.parkguidance.web.admin.pages.garageform.GarageFormFacade;
import com.neo.parkguidance.web.admin.pages.garageform.GarageFormModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;

class DataSheetFormControllerTest {

    DataSheetFormController subject;

    @Mock
    DataSheetFormModel model;

    @Mock
    DataSheetFormFacade facade;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(DataSheetFormController.class);

        model = mock(DataSheetFormModel.class);
        subject.model = model;

        facade = mock(DataSheetFormFacade.class);
        subject.facade = facade;
    }

    /**
     * Verifies if the facade gets called to delete an item
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
     * Verifies if the facade gets called to create an item
     */
    @Test
    void createItem() {
        ParkingGarage parkingGarage = new ParkingGarage();
        parkingGarage.setName("Test");

        DataSheet item = new DataSheet();
        item.setParkingGarage(parkingGarage);

        when(model.getItem()).thenReturn(item);

        //Assert
        try {
            subject.save();
        } catch (NullPointerException e) {}

        verify(facade).create(any());
    }

    /**
     * Verifies if the facade gets called to edit an item
     */
    @Test
    void editItem() {
        ParkingGarage parkingGarage = new ParkingGarage();
        parkingGarage.setName("Test");

        DataSheet item = new DataSheet();
        item.setParkingGarage(parkingGarage);
        item.setId(1L);

        when(model.getItem()).thenReturn(item);

        //Assert
        try {
            subject.save();
        } catch (NullPointerException e) {}

        verify(facade).edit(any());
    }
}
