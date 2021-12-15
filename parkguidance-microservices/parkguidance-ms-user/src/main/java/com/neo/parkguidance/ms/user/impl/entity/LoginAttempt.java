package com.neo.parkguidance.ms.user.impl.entity;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

/**
 * This entity class is used for segregation of parts of the website with user permissions
 */
@Entity
@Table(name = LoginAttempt.TABLE_NAME)
public class LoginAttempt implements DataBaseEntity {

    public static final String TABLE_NAME = "loginAttempt";
    public static final String C_TIME = "time";
    public static final String C_FAILED = "failed";
    public static final String C_IP_ADDRESS = "ip_address";
    public static final String C_END_POINT = "end_point";

    @Id
    @Column(name = DataBaseEntity.C_ID, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_TIME, nullable = false, updatable = false)
    private Date time;

    @Column(name = C_IP_ADDRESS, nullable = false, updatable = false)
    private String ipAddress;

    @Column(name = C_FAILED, nullable = false, updatable = false)
    private Boolean failed;

    @Column(name = C_END_POINT, nullable = false, updatable = false)
    private String endPoint;

    @ManyToOne
    private RegisteredUser registeredUser;

    public LoginAttempt() {}

    public LoginAttempt(Date time, String ipAddress, Boolean failed) {
        this.time = time;
        this.ipAddress = ipAddress;
        this.failed = failed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Boolean getFailed() {
        return failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    @Override
    public Object getPrimaryKey() {
        return getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoginAttempt that = (LoginAttempt) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
