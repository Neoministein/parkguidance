package com.neo.parkguidance.ms.user.impl.security.credentail;

import java.util.Date;

public class RegisteredCredentials extends AbstractLoginCredentials {

    private final String identification;
    private final String password;


    public RegisteredCredentials(String identification, String password ,String ipAddress, String endPoint) {
        super(new Date(), ipAddress, endPoint);
        this.identification = identification;
        this.password = password;
    }

    public String getIdentification() {
        return identification;
    }

    public String getPassword() {
        return password;
    }
}
