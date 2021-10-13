package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.Configuration;
import com.neo.parkguidance.core.impl.config.ConfigType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.neo.parkguidance.core.entity.DefaultTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class ConfigurationValidatorTest {

    ConfigurationValidator subject;

    EntityDao entityDao;


    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(ConfigurationValidator.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;
    }

    @Test
    void validationSuccessTest() {
        //Arrange
        Mockito.doReturn(null).when(entityDao).find(any());
        String primaryKey = "Test02-_.";

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
        Configuration entity = createDefaultConfiguration();
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultConfiguration());

        //assert
        assertEquals(true, result);
    }

    @Test
    void componentHasChanged() {
        //Arrange
        Configuration entity = createDefaultConfiguration();
        entity.setComponent("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultConfiguration());

        //assert
        assertEquals(false, result);
    }

    @Test
    void typeHasChanged() {
        //Arrange
        Configuration entity = createDefaultConfiguration();
        entity.setType(ConfigType.MAP);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultConfiguration());

        //assert
        assertEquals(false, result);
    }

    @Test
    void descriptionHasChanged() {
        //Arrange
        Configuration entity = createDefaultConfiguration();
        entity.setDescription("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultConfiguration());

        //assert
        assertEquals(false, result);
    }
}
