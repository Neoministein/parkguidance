package com.neo.parkguidance.ms.user.impl.rest;

import io.helidon.security.SecurityContext;
import io.helidon.webserver.ServerRequest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Context;

@RequestScoped
public class RequestDetails {

    @Context
    ServerRequest serverRequest;

    @Context
    SecurityContext securityContext;


    public String getRemoteAddress() {
        return serverRequest.remoteAddress();
    }

    public String getRequestId() {
        return securityContext.id();
    }
}