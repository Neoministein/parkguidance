package com.neo.parkguidance.ms.user.impl.rest;

import io.helidon.security.SecurityContext;
import io.helidon.security.Subject;
import io.helidon.webserver.ServerRequest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.Context;
import java.util.Optional;

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

    public String getUUId() {
        Optional<Subject> optionalSubject = securityContext.user();

        if (optionalSubject.isPresent()) {
            return optionalSubject.get().principal().id();
        }
        return null;
    }

    public boolean isInRole(String role) {
        return securityContext.isUserInRole(role);
    }
}
