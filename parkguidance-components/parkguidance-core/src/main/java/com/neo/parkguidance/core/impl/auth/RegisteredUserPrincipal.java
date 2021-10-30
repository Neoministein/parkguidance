package com.neo.parkguidance.core.impl.auth;

import javax.security.enterprise.CallerPrincipal;

public class RegisteredUserPrincipal extends CallerPrincipal {


    private final long userId;

    public RegisteredUserPrincipal(long userId ,String name) {
        super(name);
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
