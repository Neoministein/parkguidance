package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = RegisteredUser.TABLE_NAME)
public class RegisteredUser implements DataBaseEntity<RegisteredUser> {

    public static final String TABLE_NAME = "registeredUser";
    public static final String C_USERNAME = "username";
    public static final String C_PASSWORD = "password";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = C_USERNAME, unique = true, nullable = false)
    private String username;

    @Column(name = C_PASSWORD, nullable = false)
    private String password;

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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean compareValues(RegisteredUser o) {
        if(!username.equals(o.getUsername())) {
            return false;
        }
        return password.equals(o.getPassword());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RegisteredUser that = (RegisteredUser) o;
        return that.getId() == this.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
