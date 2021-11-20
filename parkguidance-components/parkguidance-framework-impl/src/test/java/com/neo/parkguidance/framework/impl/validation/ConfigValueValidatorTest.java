package com.neo.parkguidance.framework.impl.validation;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.ConfigValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.neo.parkguidance.framework.entity.DefaultTestEntity.createDefaultConfigValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class ConfigValueValidatorTest {

    ConfigValueValidator subject;

    EntityDao entityDao;


    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(ConfigValueValidator.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;
    }

    @Test
    void validationSuccessTest() {
        //Arrange
        Mockito.doReturn(null).when(entityDao).find(any());
        String primaryKey = "Test02-_.";

        assertDoesNotThrow(() -> subject.checkInvalidCharsInKey(primaryKey));
    }

    @Test
    void validationSpaceInNameTest() {
        //Arrange
        Mockito.doReturn(null).when(entityDao).find(any());

        String primaryKey = "Te st";
        String expectedMessage = "Unsupported Character";

        //Act
        Exception exception = assertThrows(EntityValidationException.class, () -> subject.checkInvalidCharsInKey(primaryKey));
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
        Exception exception = assertThrows(EntityValidationException.class, () -> subject.checkInvalidCharsInKey(primaryKey));
        //Assert

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void nothingHasChanged() {
        //Arrange
        ConfigValue entity = createDefaultConfigValue();
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultConfigValue());

        //assert
        assertEquals(true, result);
    }

    @Test
    void valueHasChanged() {
        //Arrange
        ConfigValue entity = createDefaultConfigValue();
        entity.setValue("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultConfigValue());

        //assert
        assertEquals(false, result);
    }

    @Test
    void hiddenHasChanged() {
        //Arrange
        ConfigValue entity = createDefaultConfigValue();
        entity.setHidden(true);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultConfigValue());

        //assert
        assertEquals(false, result);
    }

    @Test
    void descriptionHasChanged() {
        //Arrange
        ConfigValue entity = createDefaultConfigValue();
        entity.setDescription("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultConfigValue());

        //assert
        assertEquals(false, result);
    }
}
