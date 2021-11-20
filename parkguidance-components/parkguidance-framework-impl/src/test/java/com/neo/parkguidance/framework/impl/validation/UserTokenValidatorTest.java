package com.neo.parkguidance.framework.impl.validation;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.Permission;
import com.neo.parkguidance.framework.entity.UserToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.neo.parkguidance.framework.entity.DefaultTestEntity.*;
import static org.junit.jupiter.api.Assertions.*;

class UserTokenValidatorTest {

    UserTokenValidator subject;

    EntityDao entityDao;


    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(UserTokenValidator.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;
    }

    @Test
    void nothingHasChanged() {
        //Arrange
        UserToken entity = createDefaultUserToken();
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultUserToken());

        //assert
        assertEquals(true, result);
    }

    @Test
    void nameHasChanged() {
        //Arrange
        UserToken entity = createDefaultUserToken();
        entity.setName("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultUserToken());

        //assert
        assertEquals(false, result);
    }

    @Test
    void keyHasChanged() {
        //Arrange
        UserToken entity = createDefaultUserToken();
        entity.setKey("newValue");
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultUserToken());

        //assert
        assertEquals(false, result);
    }

    @Test
    void permissionsHaveChanged() {
        //Arrange
        UserToken entity = createDefaultUserToken();
        Permission permission = createDefaultPermission();
        permission.setId(1l);
        entity.getPermissions().add(permission);
        Mockito.doReturn(entity).when(entityDao).find(entity.getPrimaryKey());

        boolean result;
        //Act
        result = subject.hasNothingChanged(createDefaultUserToken());

        //assert
        assertEquals(false, result);
    }
}
