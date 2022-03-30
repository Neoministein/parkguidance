package com.neo.parkguidance.ms.user.impl.entity;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;

import javax.persistence.*;

@Entity
@Table(name = UserCredentials.TABLE_NAME, indexes = {
        @Index(name = "clienId", columnList = UserCredentials.C_CLIENT_ID)})
public class UserCredentials implements DataBaseEntity {

    public static final String TABLE_NAME = "userCredentials";
    public static final String C_TYPE = "type";
    public static final String C_CLIENT_ID = "clientId";

    @Id
    @Column(name = DataBaseEntity.C_ID, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_TYPE, nullable = false, updatable = false)
    private String type;

    @Column(name = C_CLIENT_ID, updatable = false)
    private String clientId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private RegisteredUser registeredUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public RegisteredUser getRegisteredUser() {
        return registeredUser;
    }

    public void setRegisteredUser(RegisteredUser registeredUser) {
        this.registeredUser = registeredUser;
    }

    public Object getPrimaryKey() {
        return getId();
    }
}