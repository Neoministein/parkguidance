package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This entity class is used for logging into the admin interface
 */
@Entity
@Table(name = RegisteredUser.TABLE_NAME)
public class RegisteredUser implements DataBaseEntity {

    public static final String TABLE_NAME = "registeredUser";
    public static final String C_USERNAME = "username";
    public static final String C_EMAIL = "email";
    public static final String C_PASSWORD = "password";
    public static final String C_DEACTIVATED = "deactivated";
    public static final String C_CREATE_ON = "createdOn";
    public static final String C_DEACTIVATED_ON = "deactivatedOn";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = C_USERNAME, unique = true, nullable = false)
    private String username;

    @Email
    @Column(name = C_EMAIL, unique = true, nullable = false)
    private String email;

    @Column(name = C_PASSWORD, nullable = false)
    private String password;

    @Column(name = C_DEACTIVATED, nullable = false)
    private Boolean deactivated;

    @Column(name = C_CREATE_ON)
    private Date createdOn;

    @Column(name = C_DEACTIVATED_ON)
    private Date deactivatedOn;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Permission> permissions;

    @OneToMany
    @JoinColumn(name = UserToken.T_USER)
    private List<UserToken> tokens;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(Boolean deactivated) {
        this.deactivated = deactivated;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getDeactivatedOn() {
        return deactivatedOn;
    }

    public void setDeactivatedOn(Date deactivatedOn) {
        this.deactivatedOn = deactivatedOn;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<UserToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<UserToken> tokens) {
        this.tokens = tokens;
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
