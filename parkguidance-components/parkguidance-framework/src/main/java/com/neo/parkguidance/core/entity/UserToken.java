package com.neo.parkguidance.core.entity;

import com.neo.parkguidance.core.impl.security.token.TokenType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = UserToken.TABLE_NAME)
public class UserToken implements DataBaseEntity{

    public static final String TABLE_NAME = "usertoken";

    public static final String C_KEY = "key";
    public static final String C_NAME = "name";
    public static final String C_TYPE = "type";
    public static final String C_CREATED_ON = "createdOn";
    public static final String C_EXPIRATION_DATE = "expirationDate";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_KEY, nullable = false)
    private String key;

    @Column(name = C_NAME, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = C_TYPE, nullable = false)
    private TokenType type;

    @Column(name = C_CREATED_ON, updatable = false)
    private Date creationDate;

    @Column(name = C_EXPIRATION_DATE)
    private Date expirationDate;

    @ManyToMany()
    private List<Permission> permissions;

    @ManyToOne
    private RegisteredUser registeredUser;

    public UserToken() {
        this.permissions = new ArrayList<>();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
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
