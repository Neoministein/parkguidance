package com.neo.parkguidance.ms.user.impl.entity;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;
import com.neo.parkguidance.ms.user.impl.security.TokenType;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = UserToken.TABLE_NAME, indexes = {
        @Index(name = "key", columnList = UserToken.C_KEY, unique = true)})
public class UserToken implements DataBaseEntity {

    public static final String TABLE_NAME = "userToken";

    public static final String C_KEY = "key";
    public static final String C_DESCRIPTION = "description";
    public static final String C_TYPE = "type";
    public static final String C_CREATED_ON = "created_on";
    public static final String C_EXPIRATION_DATE = "expiration_date";

    @Id
    @Column(name = DataBaseEntity.C_ID, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_KEY, nullable = false, unique = true, updatable = false)
    private String key;

    @Column(name = C_DESCRIPTION, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = C_TYPE, nullable = false, updatable = false)
    private TokenType type;
    @Column(name = C_CREATED_ON, nullable = false , updatable = false)
    private Date creationDate;

    @Column(name = C_EXPIRATION_DATE, updatable = false)
    private Date expirationDate;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private RegisteredUser registeredUser;

    public UserToken() {}

    public UserToken(String description, TokenType type, Date expirationDate, RegisteredUser registeredUser) {
        this.description = description;
        this.type = type;
        this.expirationDate = expirationDate;
        this.registeredUser = registeredUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date experationDate) {
        this.expirationDate = experationDate;
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
        UserToken that = (UserToken) o;
        return that.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
