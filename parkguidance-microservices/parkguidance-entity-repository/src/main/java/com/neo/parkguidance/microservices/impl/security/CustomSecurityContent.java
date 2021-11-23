package com.neo.parkguidance.microservices.impl.security;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Collection;

public class CustomSecurityContent implements SecurityContext {

    private final Principal principal;
    private final Collection<String> roles;

    public CustomSecurityContent(Principal principal, Collection<String> roles) {
        this.principal = principal;
        this.roles = roles;
    }

    @Override
    public Principal getUserPrincipal() {
        return principal;
    }

    @Override
    public boolean isUserInRole(String s) {
        return roles.contains(s);
    }

    @Override
    public boolean isSecure() {
        return true;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
