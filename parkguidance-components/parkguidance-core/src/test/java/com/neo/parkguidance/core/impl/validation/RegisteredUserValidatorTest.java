package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.Permission;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.entity.UserToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static com.neo.parkguidance.core.entity.DefaultTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

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
        RegisteredUser entity = createDefaultRegisteredUser();
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultRegisteredUser());

        //assert
        assertEquals(true, result);
    }

    @Test
    void usernameHasChanged() {
        //Arrange
        RegisteredUser entity = createDefaultRegisteredUser();
        entity.setUsername("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultRegisteredUser());

        //assert
        assertEquals(false, result);
    }

    @Test
    void emailHasChanged() {
        //Arrange
        RegisteredUser entity = createDefaultRegisteredUser();
        entity.setEmail("newValue@newValue.newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultRegisteredUser());

        //assert
        assertEquals(false, result);
    }

    @Test
    void createdOnHasChanged() {
        //Arrange
        RegisteredUser entity = createDefaultRegisteredUser();
        entity.setCreatedOn(new Date());
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultRegisteredUser());

        //assert
        assertEquals(false, result);
    }

    @Test
    void deactivatedOnHasChanged() {
        //Arrange
        RegisteredUser entity = createDefaultRegisteredUser();
        entity.setDeactivatedOn(new Date());
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultRegisteredUser());

        //assert
        assertEquals(false, result);
    }

    @Test
    void deactivatedHasChanged() {
        //Arrange
        RegisteredUser entity = createDefaultRegisteredUser();
        entity.setDeactivated(true);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultRegisteredUser());

        //assert
        assertEquals(false, result);
    }

    @Test
    void permissionsHaveChanged() {
        //Arrange
        RegisteredUser entity = createDefaultRegisteredUser();
        Permission permission = createDefaultPermission();
        permission.setId(1l);
        entity.getPermissions().add(permission);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultRegisteredUser());

        //assert
        assertEquals(false, result);
    }

    @Test
    void userTokenHaveChanged() {
        //Arrange
        RegisteredUser entity = createDefaultRegisteredUser();
        UserToken token = createDefaultUserToken();
        token.setId(1l);
        entity.getTokens().add(token);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultRegisteredUser());

        //assert
        assertEquals(false, result);
    }
}
