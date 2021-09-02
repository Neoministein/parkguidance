package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.neo.parkguidance.core.entity.DefaultTestEntity.createDefaultAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressValidatorTest {

    AddressValidator subject;

    EntityDao entityDao;


    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(AddressValidator.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;
    }

    @Test
    void nothingHasChanged() {
        //Arrange
        Address newAddress = createDefaultAddress();
        Mockito.doReturn(newAddress).when(entityDao).find(newAddress.getId());

        boolean result;
        //Act
        result = subject.compareValues(newAddress);

        //assert
        assertEquals(true, result);
    }

    @Test
    void cityHasChangedTest() {
        //Arrange
        Address newAddress = createDefaultAddress();
        newAddress.setCityName("newValue");

        Mockito.doReturn(createDefaultAddress()).when(entityDao).find(newAddress.getId());

        boolean result;
        //Act
        result = subject.compareValues(newAddress);

        //assert
        assertEquals(false, result);
    }

    @Test
    void streetHasChangedTest() {
        //Arrange
        Address newAddress = createDefaultAddress();
        newAddress.setStreet("newValue");

        Mockito.doReturn(createDefaultAddress()).when(entityDao).find(newAddress.getId());

        boolean result;
        //Act
        result = subject.compareValues(newAddress);

        //assert
        assertEquals(false, result);
    }


    @Test
    void plzHasChangedTest() {
        //Arrange
        Address newAddress = createDefaultAddress();
        newAddress.setPlz(0);

        Mockito.doReturn(createDefaultAddress()).when(entityDao).find(newAddress.getId());

        boolean result;
        //Act
        result = subject.compareValues(newAddress);

        //assert
        assertEquals(false, result);
    }

    @Test
    void numberHasChangedTest() {
        //Arrange
        Address newAddress = createDefaultAddress();
        newAddress.setNumber(0);

        Mockito.doReturn(createDefaultAddress()).when(entityDao).find(newAddress.getId());

        boolean result;
        //Act
        result = subject.compareValues(newAddress);

        //assert
        assertEquals(false, result);
    }

    @Test
    void lngHasChangedTest() {
        //Arrange
        Address newAddress = createDefaultAddress();
        newAddress.setLongitude(0d);

        Mockito.doReturn(createDefaultAddress()).when(entityDao).find(newAddress.getId());

        boolean result;
        //Act
        result = subject.compareValues(newAddress);

        //assert
        assertEquals(false, result);
    }

    @Test
    void latHasChangedTest() {
        //Arrange
        Address newAddress = createDefaultAddress();
        newAddress.setLatitude(0d);

        Mockito.doReturn(createDefaultAddress()).when(entityDao).find(newAddress.getId());

        boolean result;
        //Act
        result = subject.compareValues(newAddress);

        //assert
        assertEquals(false, result);
    }


}
