package com.neo.parkguidance.framework.impl.security;

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
