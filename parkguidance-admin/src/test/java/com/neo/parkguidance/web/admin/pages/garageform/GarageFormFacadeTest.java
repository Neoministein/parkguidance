package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.api.external.google.maps.GeoCoding;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.core.impl.dao.AddressEntityManager;
import com.neo.parkguidance.core.impl.dao.ParkingGarageEntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class GarageFormFacadeTest {

    GarageFormFacade subject;

    @Mock
    AbstractEntityDao<Address> addressDao;

    @Mock
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    @Mock
    GeoCoding geoCoding;

    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(GarageFormFacade.class);

        parkingGarageDao = mock(ParkingGarageEntityManager.class);
        subject.garageDao = parkingGarageDao;

        addressDao = mock(AddressEntityManager.class);
        subject.addressDao = addressDao;

        geoCoding = mock(GeoCoding.class);
        subject.geoCoding = geoCoding;
    }

    /**
     * Verifies if the dao gets called to remove an item
     */
    @Test
    void removeVerifyDao() {
        //Arrange
        ParkingGarage item = new ParkingGarage();
        item.setId(0L);

        //Act
        subject.remove(item);

        //Assert

        verify(parkingGarageDao).remove(any());
    }

    /**
     *  Verifies if the dao doesn't get called
     */
    @Test
    void removeFailedVerifyDao() {
        //Arrange
        ParkingGarage item = new ParkingGarage();

        //Act
        subject.remove(item);

        //Assert

        verify(parkingGarageDao, never()).remove(any());
    }

    /**
     * Verifies if the dao gets called to edit an item
     */
    @Test
    void editVerifyDao() {
        //Arrange
        ParkingGarage item = new ParkingGarage();

        //Act
        subject.edit(item);

        //Assert

        verify(parkingGarageDao).edit(any());
    }

    /**
     * Verifies if the dao gets called to create an item
     */
    @Test
    void createVerifyDao() {
        //Arrange
        ParkingGarage item = new ParkingGarage();

        //Act
        subject.create(item);

        //Assert

        verify(parkingGarageDao).create(any());
    }
}
