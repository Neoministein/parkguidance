package com.neo.parkguidance.ms.user.impl.rest.authentication;

import com.neo.parkguidance.ms.user.api.security.CredentialsManagerService;
import com.neo.parkguidance.ms.user.api.security.jwt.JWTGeneratorService;
import com.neo.parkguidance.ms.user.impl.entity.RegisteredUser;
import com.neo.parkguidance.ms.user.impl.entity.UserToken;
import com.neo.parkguidance.ms.user.impl.rest.HttpMethod;
import com.neo.parkguidance.ms.user.impl.rest.RequestDetails;
import com.neo.parkguidance.ms.user.impl.security.TokenType;
import com.neo.parkguidance.ms.user.impl.security.UserStatus;
import com.neo.parkguidance.ms.user.impl.security.credentail.RegisteredCredentials;
import com.neo.parkguidance.ms.user.impl.security.credentail.TokenCredentials;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.ws.rs.core.Response;

class AuthenticationResourceTest {

    AuthenticationResource subject;

    RequestDetails requestDetails;

    JWTGeneratorService jwtGeneratorService;

    CredentialsManagerService credentialsManagerService;

    @BeforeEach
    public void init() {
        subject = Mockito.spy(AuthenticationResource.class);

        requestDetails = Mockito.mock(RequestDetails.class);
        subject.setRequestDetails(requestDetails);
        Mockito.doReturn("").when(requestDetails).getRequestId();
        Mockito.doReturn("127.0.0.1").when(requestDetails).getRemoteAddress();

        jwtGeneratorService = Mockito.mock(JWTGeneratorService.class);
        subject.jwtGeneratorService = jwtGeneratorService;

        credentialsManagerService = Mockito.mock(CredentialsManagerService.class);
        subject.credentialsManagerService = credentialsManagerService;

    }

    @Test
    void credentialsSuccessTest() {
        //Arrange
        String input = new JSONObject().put("identification","").put("password","").toString();

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("Test user");
        registeredUser.setUserStatus(UserStatus.OK);
        registeredUser.setEmailVerified(true);

        Mockito.doReturn(registeredUser).when(credentialsManagerService).retrieveUser(Mockito.any(RegisteredCredentials.class));
        //Act

        subject.credentials(input);
        //Assert
        Mockito.verify(jwtGeneratorService).generateJWTResponse(registeredUser,true,false);
    }

    @Test
    void credentialsFailedTest() {
        //Arrange
        String input = new JSONObject().put("identification","").put("password","").toString();

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("Test user");
        registeredUser.setUserStatus(UserStatus.OK);
        registeredUser.setEmailVerified(false);

        Mockito.doReturn(null).when(credentialsManagerService).retrieveUser(Mockito.any(RegisteredCredentials.class));
        //Act

        Response response = subject.credentials(input);
        //Assert
        Assertions.assertEquals(
                AuthenticationResponse.error(
                        AuthenticationResponse.CREDENTIALS_INVALID,
                        subject.getContext(HttpMethod.POST, "/credentials"))
                        .getEntity().toString(),
                response.getEntity().toString());
    }

    @Test
    void credentialsUnverifiedEmailTest() {
        //Arrange
        String input = new JSONObject().put("identification","").put("password","").toString();

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("Test user");
        registeredUser.setUserStatus(UserStatus.OK);
        registeredUser.setEmailVerified(false);

        Mockito.doReturn(registeredUser).when(credentialsManagerService).retrieveUser(Mockito.any(RegisteredCredentials.class));
        //Act

        Response response = subject.credentials(input);
        //Assert
        Mockito.verify(jwtGeneratorService).generateJWTResponse(registeredUser,true,true);
        Assertions.assertEquals(
                AuthenticationResponse.partialSuccess(
                        5,
                        new JSONArray().put((Object) null),
                        subject.getContext(HttpMethod.POST,"/credentials"))
                        .getEntity().toString(),
                response.getEntity().toString());
    }

    @Test
    void tokenSuccessTest() {
        //Arrange
        String input = new JSONObject().put("token","").toString();

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("Test user");
        registeredUser.setUserStatus(UserStatus.OK);
        registeredUser.setEmailVerified(true);

        UserToken userToken = new UserToken();
        userToken.setType(TokenType.REFRESH);
        userToken.setRegisteredUser(registeredUser);

        Mockito.doReturn(userToken).when(credentialsManagerService).retrieveUser(Mockito.any(TokenCredentials.class));
        //Act

        subject.token(input);
        //Assert
        Mockito.verify(jwtGeneratorService).generateJWTResponse(registeredUser,false,false);
    }

    @Test
    void tokenUnverifiedEmailTest() {
        //Arrange
        String input = new JSONObject().put("token","").toString();

        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("Test user");
        registeredUser.setUserStatus(UserStatus.OK);
        registeredUser.setEmailVerified(false);

        UserToken userToken = new UserToken();
        userToken.setType(TokenType.REFRESH);
        userToken.setRegisteredUser(registeredUser);

        Mockito.doReturn(userToken).when(credentialsManagerService).retrieveUser(Mockito.any(TokenCredentials.class));
        //Act

        subject.token(input);
        //Assert
        Mockito.verify(jwtGeneratorService).generateJWTResponse(registeredUser,false,true);
    }

    @Test
    void tokenFailedTest() {
        //Arrange
        String input = new JSONObject().put("token","").toString();

        Mockito.doReturn(null).when(credentialsManagerService).retrieveUser(Mockito.any(TokenCredentials.class));
        //Act

        Response response = subject.token(input);
        //Assert

        Assertions.assertEquals(
                AuthenticationResponse.error(
                        AuthenticationResponse.INVALID_TOKEN,
                        subject.getContext(HttpMethod.POST, "/token"))
                        .getEntity().toString(),
                response.getEntity().toString());
    }

    @Test
    void tokenPartialTest() {
        //Arrange
        String input = new JSONObject().put("token","").toString();
        RegisteredUser registeredUser = new RegisteredUser();
        registeredUser.setUsername("Test user");
        registeredUser.setUserStatus(UserStatus.OK);
        registeredUser.setEmailVerified(true);

        UserToken userToken = new UserToken();
        userToken.setType(TokenType.PARTIAL);
        userToken.setRegisteredUser(registeredUser);

        Mockito.doReturn(userToken).when(credentialsManagerService).retrieveUser(Mockito.any(TokenCredentials.class));
        //Act

        Response response = subject.token(input);
        //Assert

        Mockito.verify(jwtGeneratorService).generateJWTResponse(registeredUser,false,true);
        Assertions.assertEquals(
                AuthenticationResponse.partialSuccess(
                        AuthenticationResponse.PARTIAL_LOGIN_TOKEN,
                        new JSONArray().put((Object) null),
                        subject.getContext(HttpMethod.POST,"/token"))
                        .getEntity().toString(),
                response.getEntity().toString());
    }

    @Test
    void noUnhandledUserStatus() {
        for (UserStatus userStatus: UserStatus.values()) {
            subject.checkUserStatus(userStatus, "");
        }
        //Throws error when there is an unhandled user status
    }
}
