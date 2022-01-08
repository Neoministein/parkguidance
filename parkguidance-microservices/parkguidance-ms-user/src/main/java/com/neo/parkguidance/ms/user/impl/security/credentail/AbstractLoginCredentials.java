package com.neo.parkguidance.ms.user.impl.security.credentail;

import javax.security.enterprise.credential.Credential;
import java.util.Date;

public abstract class AbstractLoginCredentials implements Credential {

    private final Date time;
    private final String ipAddress;
    private final String endPoint;

    protected AbstractLoginCredentials(Date time, String ipAddress, String endPoint) {
        this.time = time;
        this.ipAddress = ipAddress;
        this.endPoint = endPoint;
    }

    public Date getTime() {
        return time;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
