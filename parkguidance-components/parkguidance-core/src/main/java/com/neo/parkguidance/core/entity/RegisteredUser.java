package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

/**
 * This entity class is used for logging into the admin interface
 */
@Entity
@Table(name = RegisteredUser.TABLE_NAME)
public class RegisteredUser implements DataBaseEntity {

    public static final String TABLE_NAME = "registeredUser";
    public static final String C_USERNAME = "username";
    public static final String C_PASSWORD = "password";
    public static final String C_TOKEN = "token";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = C_USERNAME, unique = true, nullable = false)
    private String username;

    @Column(name = C_PASSWORD, nullable = false)
    private String password;

    @Column(name = C_TOKEN, unique = true)
    private String token;

    @ManyToMany
    private Set<Permission> permissions;

    public RegisteredUser(@Size(max = 50) String username, String password) {
        this.username = username;
        this.password = password;
    }

    public RegisteredUser() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
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
        RegisteredUser that = (RegisteredUser) o;
        return that.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
