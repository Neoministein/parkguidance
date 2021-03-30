package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.admin.pages.dataform.DataFormController;
import com.neo.parkguidance.web.admin.pages.dataform.DataFormFacade;
import com.neo.parkguidance.web.admin.pages.dataform.DataFormModel;
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

    @Test
    void deleteVerifyFacade() throws IOException {
        when(facade.remove(any())).thenReturn(false);

        subject.remove();

        //Assert

        verify(facade).remove(any());
    }

    @Test
    void createItem() {
        ParkingGarage item = new ParkingGarage();
        item.setName("Test");

        when(model.getItem()).thenReturn(item);

        //Assert
        try {
            subject.save();
        } catch (NullPointerException e) {}

        verify(facade).create(any());
    }

    @Test
    void editItem() {
        ParkingGarage item = new ParkingGarage();
        item.setName("Test");
        item.setId(1L);

        when(model.getItem()).thenReturn(item);

        //Assert
        try {
            subject.save();
        } catch (NullPointerException e) {}

        verify(facade).edit(any());
    }
}
