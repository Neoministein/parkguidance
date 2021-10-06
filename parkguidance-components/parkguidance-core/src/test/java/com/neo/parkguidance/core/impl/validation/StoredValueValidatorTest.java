package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.StoredValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.neo.parkguidance.core.entity.DefaultTestEntity.createDefaultStoredValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class StoredValueValidatorTest {

    StoredValueValidator subject;

    EntityDao entityDao;


    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(StoredValueValidator.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;
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
        StoredValue entity = createDefaultStoredValue();
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultStoredValue());

        //assert
        assertEquals(true, result);
    }

    @Test
    void valueHasChanged() {
        //Arrange
        StoredValue entity = createDefaultStoredValue();
        entity.setValue("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultStoredValue());

        //assert
        assertEquals(false, result);
    }

    @Test
    void hiddenHasChanged() {
        //Arrange
        StoredValue entity = createDefaultStoredValue();
        entity.setHidden(true);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultStoredValue());

        //assert
        assertEquals(false, result);
    }

    @Test
    void componentHasChanged() {
        //Arrange
        StoredValue entity = createDefaultStoredValue();
        entity.setComponent("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultStoredValue());

        //assert
        assertEquals(false, result);
    }

    @Test
    void descriptionHasChanged() {
        //Arrange
        StoredValue entity = createDefaultStoredValue();
        entity.setComponent("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultStoredValue());

        //assert
        assertEquals(false, result);
    }
}
