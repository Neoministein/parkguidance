package com.neo.parkguidance.web.admin.pages.sheetform;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AddressEntityManager;
import com.neo.parkguidance.web.admin.pages.garageform.GarageFormFacade;
import com.neo.parkguidance.web.infra.entity.DataSheetEntityService;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class DataSheetFormFacadeTest {

    DataSheetFormFacade subject;

    @Mock
    DataSheetEntityService dataSheetService;

    @Mock
    ParkingGarageEntityService parkingGarageService;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(DataSheetFormFacade.class);

        parkingGarageService = mock(ParkingGarageEntityService.class);
        subject.parkingGarageManager = parkingGarageService;

        dataSheetService = mock(DataSheetEntityService.class);
        subject.dataSheetService = dataSheetService;
    }

    /**
     * Verifies if the dao gets called to remove an item
     */
    @Test
    void removeVerifyDao() {
        //Arrange
        DataSheet item = new DataSheet();
        item.setId(0L);

        //Act
        subject.remove(item);

        //Assert

        verify(dataSheetService).remove(any());
    }

    /**
     * Verifies if the dao gets doesn't called to remove an item
     */
    @Test
    void removeFailedVerifyDao() {
        //Arrange
        DataSheet item = new DataSheet();

        //Act
        subject.remove(item);

        //Assert

        verify(dataSheetService, never()).remove(any());
    }

    /**
     * Verifies if the dao gets called to edit an item
     */
    @Test
    void editVerifyDao() {
        //Arrange
        DataSheet item = new DataSheet();

        //Act
        subject.edit(item);

        //Assert

        verify(dataSheetService).edit(any());
    }

    /**
     * Verifies if the dao gets called to create an item
     */
    @Test
    void createVerifyDao() {
        //Arrange
        DataSheet item = new DataSheet();

        //Act
        subject.create(item);

        //Assert

        verify(dataSheetService).create(any());
    }
}
