package com.neo.parkguidance.ms.user.impl.security;

import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.impl.entity.LoginAttempt;
import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import com.neo.parkguidance.ms.user.impl.entity.UserToken;
import com.neo.parkguidance.ms.user.impl.security.credentail.AbstractLoginCredentials;
import com.neo.parkguidance.ms.user.impl.security.credentail.RegisteredCredentials;
import com.neo.parkguidance.ms.user.impl.utils.CredentialsUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.transaction.RollbackException;
import java.util.ArrayList;
import java.util.List;

class CredentialsManagerServiceTest {

    CredentialsManagerServiceImpl subject;

    EntityDao<RegisteredUser> userDao;

    EntityDao<UserToken> userTokenDao;

    EntityDao<LoginAttempt> loginAttemptDao;

    @BeforeEach
    public void init() {
        subject = Mockito.spy(CredentialsManagerServiceImpl.class);

        userDao = Mockito.mock(EntityDao.class);
        subject.userDao = userDao;

        userTokenDao = Mockito.mock(EntityDao.class);
        subject.userTokenDao = userTokenDao;

        loginAttemptDao = Mockito.mock(EntityDao.class);
        subject.loginAttemptDao = loginAttemptDao;
    }

    @Test
    void retrieveValidUsernameTest() {
        //Arrange
        String username = "TestUser";
        String salt = "salt";
        String password = "Test123!";
        String hashPassword = CredentialsUtils.hashPassword("Test123!", salt);

        RegisteredCredentials registeredCredentials = new RegisteredCredentials(username,password, "","");

        RegisteredUser user = new RegisteredUser();
        user.setUsername(username);
        user.setSalt(salt);
        user.setPassword(hashPassword);

        Mockito.doReturn(user).when(userDao).findOneByColumn(RegisteredUser.C_USERNAME, username);
        //Act
        RegisteredUser result = subject.retrieveUser(registeredCredentials);

        //Assert

        Assertions.assertEquals(user, result);
    }

    @Test
    void retrieveValidEmailTest() {
        //Arrange
        String email = "TestUser";
        String salt = "salt";
        String password = "Test123!";
        String hashPassword = CredentialsUtils.hashPassword(password, salt);

        RegisteredCredentials registeredCredentials = new RegisteredCredentials(email,password, "","");

        RegisteredUser user = new RegisteredUser();
        user.setEmail(email);
        user.setSalt(salt);
        user.setPassword(hashPassword);

        Mockito.doReturn(user).when(userDao).findOneByColumn(RegisteredUser.C_EMAIL, email);
        //Act
        RegisteredUser result = subject.retrieveUser(registeredCredentials);

        //Assert

        Assertions.assertEquals(user, result);
    }

    @Test
    void retrieveInvalidUserTest() {
        //Arrange
        String email = "TestUser";
        String salt = "salt";
        String password = "Test123!";
        String hashPassword = CredentialsUtils.hashPassword(password, salt);

        RegisteredCredentials registeredCredentials = new RegisteredCredentials(email,password, "","");

        RegisteredUser user = new RegisteredUser();
        user.setEmail("differentEmail");
        user.setSalt(salt);
        user.setPassword(hashPassword);
        user.setUserStatus(UserStatus.DEACTIVATED);

        Mockito.doReturn(user).when(userDao).findOneByColumn(RegisteredUser.C_EMAIL, "differentEmail");
        //Act
        RegisteredUser result = subject.retrieveUser(registeredCredentials);

        //Assert

        Assertions.assertNull(result);
    }

    @Test
    void retrieveInvalidPasswordTest() {
        //Arrange
        String email = "TestUser";
        String salt = "salt";
        String password = "Test123!";
        String passwordHash = CredentialsUtils.hashPassword(password, salt);

        RegisteredCredentials registeredCredentials = new RegisteredCredentials(email,password, "","");

        RegisteredUser user = new RegisteredUser();
        user.setEmail(email);
        user.setSalt(salt);
        user.setPassword("differentPassword");
        user.setUserStatus(UserStatus.DEACTIVATED);

        Mockito.doReturn(user).when(userDao).findOneByColumn(RegisteredUser.C_EMAIL, email);
        //Act
        RegisteredUser result = subject.retrieveUser(registeredCredentials);

        //Assert

        Assertions.assertNull(result);
    }

    @Test
    void retrieveInvalidSaltTest() {
        //Arrange
        String email = "TestUser";
        String salt = "salt";
        String password = "Test123!";
        String hashPassword = CredentialsUtils.hashPassword(password, salt);

        RegisteredCredentials registeredCredentials = new RegisteredCredentials(email,password, "","");

        RegisteredUser user = new RegisteredUser();
        user.setEmail(email);
        user.setSalt("differentSalt");
        user.setPassword(hashPassword);
        user.setUserStatus(UserStatus.DEACTIVATED);

        Mockito.doReturn(user).when(userDao).findOneByColumn(RegisteredUser.C_EMAIL, email);
        //Act
        RegisteredUser result = subject.retrieveUser(registeredCredentials);

        //Assert

        Assertions.assertNull(result);
    }

    @Test
    void loginAttemptLoginFailedTest() throws RollbackException {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();

        AbstractLoginCredentials loginCredentials = Mockito.mock(AbstractLoginCredentials.class);
        //Act

        subject.createLoginAttempt(loginCredentials, registeredUser, false);
        //Assert
        Mockito.verify(loginAttemptDao).create(Mockito.any(LoginAttempt.class));
        Mockito.verify(subject, Mockito.never()).checkForToManyAuth(Mockito.any());
        Assertions.assertEquals(UserStatus.OK, registeredUser.getUserStatus());
    }

    @Test
    void loginAttemptNoUserTest() {
        //Arrange
        AbstractLoginCredentials loginCredentials = Mockito.mock(AbstractLoginCredentials.class);
        //Act

        subject.createLoginAttempt(loginCredentials, null, false);
        //Assert
        Mockito.verify(subject, Mockito.never()).checkForToManyAuth(Mockito.any());
    }

    @Test
    void loginAttemptAccountDisabledTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUserStatus(UserStatus.DEACTIVATED);

        AbstractLoginCredentials loginCredentials = Mockito.mock(AbstractLoginCredentials.class);
        //Act

        subject.createLoginAttempt(loginCredentials, registeredUser, false);
        //Assert
        Mockito.verify(subject, Mockito.never()).checkForToManyAuth(Mockito.any());
        Assertions.assertEquals(UserStatus.DEACTIVATED, registeredUser.getUserStatus());
    }

    @Test
    void loginAttemptToManyFailedAuthTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUserStatus(UserStatus.OK);
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setFailed(true);
        List<LoginAttempt> loginAttemptList = new ArrayList<>();
        loginAttemptList.add(loginAttempt);
        loginAttemptList.add(loginAttempt);
        loginAttemptList.add(loginAttempt);
        loginAttemptList.add(loginAttempt);
        loginAttemptList.add(loginAttempt);

        Mockito.doReturn(loginAttemptList).when(loginAttemptDao).findByColumn(Mockito.anyMap(), Mockito.anyMap(), Mockito.anyInt(), Mockito.anyInt());
        AbstractLoginCredentials loginCredentials = Mockito.mock(AbstractLoginCredentials.class);
        //Act

        subject.createLoginAttempt(loginCredentials, registeredUser, true);

        //Assert
        Assertions.assertEquals(UserStatus.LOCKET_TO_MANY_FAILED_AUTH, registeredUser.getUserStatus());
    }

    @Test
    void loginAttemptNotEnoughFailedAuthTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setFailed(true);
        List<LoginAttempt> loginAttemptList = new ArrayList<>();
        loginAttemptList.add(loginAttempt);
        loginAttemptList.add(loginAttempt);
        loginAttemptList.add(loginAttempt);

        Mockito.doReturn(loginAttemptList).when(loginAttemptDao).findByColumn(Mockito.anyMap(), Mockito.anyMap(), Mockito.anyInt(), Mockito.anyInt());
        AbstractLoginCredentials loginCredentials = Mockito.mock(AbstractLoginCredentials.class);
        //Act

        subject.createLoginAttempt(loginCredentials, registeredUser, true);
        //Assert

        Assertions.assertEquals(UserStatus.OK, registeredUser.getUserStatus());
    }

    @Test
    void loginAttemptNoFailedAuthTest() {
        //Arrange
        RegisteredUser registeredUser = new RegisteredUser();
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setFailed(false);
        List<LoginAttempt> loginAttemptList = new ArrayList<>();
        loginAttemptList.add(loginAttempt);
        loginAttemptList.add(loginAttempt);
        loginAttemptList.add(loginAttempt);

        Mockito.doReturn(loginAttemptList).when(loginAttemptDao).findByColumn(Mockito.anyMap(), Mockito.anyMap(), Mockito.anyInt(), Mockito.anyInt());
        AbstractLoginCredentials loginCredentials = Mockito.mock(AbstractLoginCredentials.class);
        //Act

        subject.createLoginAttempt(loginCredentials, registeredUser, true);
        //Assert
        Assertions.assertEquals(UserStatus.OK, registeredUser.getUserStatus());
    }
}
