package com.neo.parkguidance.framework.impl.validation;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.neo.parkguidance.framework.entity.DefaultTestEntity.createDefaultParkingGarage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class ParkingGarageValidatorTest {

    ParkingGarageValidator subject;

    AddressValidator addressValidator;

    EntityDao entityDao;


    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(ParkingGarageValidator.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;

        addressValidator = Mockito.mock(AddressValidator.class);
        subject.addressValidation = addressValidator;

        Mockito.doReturn(false).when(addressValidator).hasNothingChanged(any());
        Mockito.doReturn(entityDao).when(subject).getDao();
    }

    @Test
    void validationSuccessTest() {
        //Arrange
        Mockito.doReturn(null).when(entityDao).find(any());
        String primaryKey = "Test02-_";

        assertDoesNotThrow(() -> subject.validatePrimaryKey(primaryKey));
    }

    @Test
    void validationSpaceInNameTest() {
        //Arrange
        Mockito.doReturn(null).when(entityDao).find(any());

        String primaryKey = "Te st";
        String expectedMessage = "Unsupported Character";

        //Act
        Exception exception = assertThrows(EntityValidationException.class, () -> subject.validatePrimaryKey(primaryKey));
        //Assert

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void validationIllegalCharacterTest() {
        //Arrange
        Mockito.doReturn(null).when(entityDao).find(any());

        String primaryKey = "Test$!";
        String expectedMessage = "Unsupported Character";

        //Act
        Exception exception = assertThrows(EntityValidationException.class, () -> subject.validatePrimaryKey(primaryKey));
        //Assert

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void nothingHasChanged() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultParkingGarage());

        //assert
        assertEquals(true, result);
    }

    @Test
    void nameHasChanged() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        entity.setName("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultParkingGarage());

        //assert
        assertEquals(false, result);
    }

    @Test
    void spacesHasChanged() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        entity.setSpaces(0);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultParkingGarage());

        //assert
        assertEquals(false, result);
    }

    @Test
    void occupiedHasChanged() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        entity.setOccupied(0);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultParkingGarage());

        //assert
        assertEquals(false, result);
    }

    @Test
    void priceHasChanged() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        entity.setPrice("");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultParkingGarage());

        //assert
        assertEquals(false, result);
    }

    @Test
    void operatorHasChanged() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        entity.setOperator("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultParkingGarage());

        //assert
        assertEquals(false, result);
    }

    @Test
    void descriptionHasChanged() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        entity.setDescription("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultParkingGarage());

        //assert
        assertEquals(false, result);
    }

    @Test
    void accessKeyHasChanged() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        entity.setAccessKey("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultParkingGarage());

        //assert
        assertEquals(false, result);
    }

    @Test
    void newUniqueAccessKey() {
        //Arrange
        ParkingGarage entity = createDefaultParkingGarage();
        Mockito.doReturn(null).when(entityDao).findByColumn(any(),any());

        //Act
        subject.newUniqueAccessKey(entity);

        //assert
        assertNotEquals( createDefaultParkingGarage().getAccessKey(), entity.getAccessKey());
        Mockito.verify(entityDao, Mockito.times(1)).findOneByColumn(anyString(), any());
    }
}
