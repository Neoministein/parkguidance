package com.neo.parkguidance.ms.user.impl.entity;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = KeyPair.TABLE_NAME)
public class KeyPair implements DataBaseEntity {

    public static final String TABLE_NAME = "keyPair";
    public static final String C_PUBLIC_KEY = "publicKey";
    public static final String C_PRIVATE_KEY = "privateKey";
    public static final String C_DISABLED = "disabled";
    public static final String C_EXPIRATION_DATE = "expirationDate";

    @Id
    @Column(name = DataBaseEntity.C_ID)
    @Type(type = "uuid-char")
    private final UUID id = UUID.randomUUID();

    @Column(name = C_PUBLIC_KEY, nullable = false, length = 2000)
    private String publicKey;

    @Column(name = C_PRIVATE_KEY, nullable = false, length = 2000)
    private String privateKey;

    @Column(name = C_DISABLED,nullable = false)
    private Boolean disabled = false;

    @Column(name = C_EXPIRATION_DATE, nullable = false)
    private Date expirationDate;

    public KeyPair() {

    }

    public KeyPair(String publicKey, String privateKey, Date expirationDate) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.expirationDate = expirationDate;
    }

    public UUID getId() {
        return id;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public Object getPrimaryKey() {
        return getId().toString();
    }
}
