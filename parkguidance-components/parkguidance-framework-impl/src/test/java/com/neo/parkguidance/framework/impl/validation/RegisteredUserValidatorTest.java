package com.neo.parkguidance.framework.impl.validation;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.DefaultTestEntity;
import com.neo.parkguidance.framework.entity.Permission;
import com.neo.parkguidance.framework.entity.RegisteredUser;
import com.neo.parkguidance.framework.entity.UserToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

class RegisteredUserValidatorTest {

    RegisteredUserValidator subject;

    EntityDao entityDao;


    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(RegisteredUserValidator.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;
    }

    @Test
    void nothingHasChanged() {
        //Arrange
        RegisteredUser entity = DefaultTestEntity.createDefaultRegisteredUser();
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(DefaultTestEntity.createDefaultRegisteredUser());

        //assert
        Assertions.assertEquals(true, result);
    }

    @Test
    void usernameHasChanged() {
        //Arrange
        RegisteredUser entity = DefaultTestEntity.createDefaultRegisteredUser();
        entity.setUsername("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(DefaultTestEntity.createDefaultRegisteredUser());

        //assert
        Assertions.assertEquals(false, result);
    }

    @Test
    void emailHasChanged() {
        //Arrange
        RegisteredUser entity = DefaultTestEntity.createDefaultRegisteredUser();
        entity.setEmail("newValue@newValue.newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(DefaultTestEntity.createDefaultRegisteredUser());

        //assert
        Assertions.assertEquals(false, result);
    }

    @Test
    void createdOnHasChanged() {
        //Arrange
        RegisteredUser entity = DefaultTestEntity.createDefaultRegisteredUser();
        entity.setCreatedOn(new Date());
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(DefaultTestEntity.createDefaultRegisteredUser());

        //assert
        Assertions.assertEquals(false, result);
    }

    @Test
    void deactivatedOnHasChanged() {
        //Arrange
        RegisteredUser entity = DefaultTestEntity.createDefaultRegisteredUser();
        entity.setDeactivatedOn(new Date());
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(DefaultTestEntity.createDefaultRegisteredUser());

        //assert
        Assertions.assertEquals(false, result);
    }

    @Test
    void deactivatedHasChanged() {
        //Arrange
        RegisteredUser entity = DefaultTestEntity.createDefaultRegisteredUser();
        entity.setDeactivated(true);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(DefaultTestEntity.createDefaultRegisteredUser());

        //assert
        Assertions.assertEquals(false, result);
    }

    @Test
    void permissionsHaveChanged() {
        //Arrange
        RegisteredUser entity = DefaultTestEntity.createDefaultRegisteredUser();
        Permission permission = DefaultTestEntity.createDefaultPermission();
        permission.setId(1l);
        entity.getPermissions().add(permission);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(DefaultTestEntity.createDefaultRegisteredUser());

        //assert
        Assertions.assertEquals(false, result);
    }

    @Test
    void userTokenHaveChanged() {
        //Arrange
        RegisteredUser entity = DefaultTestEntity.createDefaultRegisteredUser();
        UserToken token = DefaultTestEntity.createDefaultUserToken();
        token.setId(1l);
        entity.getTokens().add(token);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(DefaultTestEntity.createDefaultRegisteredUser());

        //assert
        Assertions.assertEquals(false, result);
    }

    @Test
    void passwordIsValid() {
        //Arrange
        String password = "APassword0$";
        String expectedMessage = RegisteredUserValidator.INVALID_PASSWORD;

        //Act
        //Assert
        Assertions.assertDoesNotThrow(() -> subject.validatePassword(password));
    }

    @Test
    void invalidPasswordCheck() {
        //Arrange
        String[] passwords = { "Aa0!", //To Short
                "assword0!", // No uppercase
                "APASSWORD0!", // No lowercase
                "APassword!",  // No Number
                "APassword0" // No special character
        };
        String expectedMessage = RegisteredUserValidator.INVALID_PASSWORD;

        //Act
        for (String password : passwords) {
            Exception exception = Assertions.assertThrows(EntityValidationException.class, () -> subject.validatePassword(password));
            //Assert
            Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
        }
    }
}
