package com.neo.parkguidance.framework.impl.validation;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.DataBaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AbstractDataBaseEntityValidationTest {

    AbstractDatabaseEntityValidation subject;

    EntityDao entityDao;


    @BeforeEach
    public void setUp() {
        subject = Mockito.spy(AbstractDatabaseEntityValidation.class);

        entityDao = Mockito.mock(EntityDao.class);
        subject.dao = entityDao;
    }


    @Test
    void primaryKeyNullTest() {
        //Arrange
        String primaryKey = null;
        String expectedMessage = "Key cannot be null";

        //Act
        Exception exception = assertThrows(EntityValidationException.class, () -> subject.validatePrimaryKey(primaryKey));
        //Assert

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void primaryKeyIsEmptyTest() {
        //Arrange
        Mockito.doReturn(Mockito.mock(DataBaseEntity.class)).when(entityDao).find(any());

        String primaryKey = "";
        String expectedMessage = "A String Key cannot be empty";

        //Act
        Exception exception = assertThrows(EntityValidationException.class, () -> subject.validatePrimaryKey(primaryKey));
        //Assert

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void primaryKeyExistTest() {
        //Arrange
        Mockito.doReturn(Mockito.mock(DataBaseEntity.class)).when(entityDao).find(any());

        String primaryKey = "Test";
        String expectedMessage = "Key already exists";

        //Act
        Exception exception = assertThrows(EntityValidationException.class, () -> subject.validatePrimaryKey(primaryKey));
        //Assert

        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void validationSuccessTest() {
        //Arrange
        Mockito.doReturn(null).when(entityDao).find(any());
        String primaryKey = "Test";

        assertDoesNotThrow(() -> subject.validatePrimaryKey(primaryKey));
    }

    @Test
    void returnOriginalObjectSuccessTest() {
        //Arrange
        DataBaseEntity entity = Mockito.mock(DataBaseEntity.class);
        Mockito.doReturn("Test").when(entity).getPrimaryKey();
        Mockito.doReturn(entity).when(entityDao).find(any());

        DataBaseEntity result;
        //Act
        result = subject.returnOriginalObject(entity);
        //Assert

        assertNotNull(result);
    }

    @Test
    void returnOriginalObjectNullObjectTest() {
        //Arrange
        DataBaseEntity entity = Mockito.mock(DataBaseEntity.class);
        Mockito.doReturn("Test").when(entity).getPrimaryKey();
        Mockito.doReturn(entity).when(entityDao).find(any());

        DataBaseEntity result;
        //Act
        result = subject.returnOriginalObject(null);
        //Assert

        assertNull(result);
    }

    @Test
    void returnOriginalObjectNullPrimaryKeyTest() {
        //Arrange
        DataBaseEntity entity = Mockito.mock(DataBaseEntity.class);
        Mockito.doReturn(null).when(entity).getPrimaryKey();
        Mockito.doReturn(entity).when(entityDao).find(any());

        DataBaseEntity result;
        //Act
        result = subject.returnOriginalObject(null);
        //Assert

        assertNull(result);
    }
}
