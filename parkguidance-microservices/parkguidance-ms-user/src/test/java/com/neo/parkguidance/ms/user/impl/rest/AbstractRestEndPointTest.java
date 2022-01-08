package com.neo.parkguidance.ms.user.impl.rest;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.ms.user.api.rest.RestAction;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AbstractRestEndPointTest {

    AbstractRestEndpoint subject;

    RequestDetails requestDetails;

    @BeforeEach
    public void init() {
        subject = Mockito.spy(AbstractRestEndpoint.class);
        Mockito.doReturn("").when(subject).getClassURI();

        requestDetails = Mockito.mock(RequestDetails.class);
        subject.setRequestDetails(requestDetails);
        Mockito.doReturn("").when(requestDetails).getRequestId();
    }

    @Test
    void handlesJSONExceptionTest() {
        //Arrange
        RestAction restAction = () -> {
            throw new JSONException("");
        };
        //Act / Assert
        Assertions.assertDoesNotThrow(() -> subject.restCall(restAction, HttpMethod.POST,""));
    }

    @Test
    void handlesInternalLogicExceptionTest() {
        //Arrange
        RestAction restAction = () -> {
            throw new InternalLogicException("");
        };
        //Act / Assert
        Assertions.assertDoesNotThrow(() -> subject.restCall(restAction, HttpMethod.POST,""));
    }

    @Test
    void handlesIcExceptionTest() {
        //Arrange
        RestAction restAction = () -> {
            throw new Exception("");
        };
        //Act / Assert
        Assertions.assertDoesNotThrow(() -> subject.restCall(restAction, HttpMethod.POST,""));
    }
}
