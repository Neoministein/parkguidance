package com.neo.parkguidance.ms.user.impl.entity;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;
import com.neo.parkguidance.ms.user.impl.security.UserStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * This entity class is used for logging into the admin interface
 */
@Entity
@Table(name = RegisteredUser.TABLE_NAME, indexes = {
        @Index(name="username", columnList = RegisteredUser.C_USERNAME, unique = true),
        @Index(name="email", columnList = RegisteredUser.C_EMAIL, unique = true)})
public class RegisteredUser implements DataBaseEntity {

    public static final String TABLE_NAME = "registeredUser";
    public static final String C_USERNAME = "username";
    public static final String C_EMAIL = "email";
    public static final String C_EMAIL_VERIFIED = "email_verified";
    public static final String C_SALT = "salt";
    public static final String C_PASSWORD = "password";
    public static final String C_STATUS = "status";
    public static final String C_CREATE_ON = "created_on";
    public static final String C_DEACTIVATED_ON = "deactivated_on";
    public static final String C_PICTURE = "picture";

    @Id
    @Column(name = DataBaseEntity.C_ID)
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @Size(max = 50)
    @Column(name = C_USERNAME, unique = true, nullable = false)
    private String username;

    @Email
    @Column(name = C_EMAIL, unique = true, nullable = false)
    private String email;

    @Column(name = C_EMAIL_VERIFIED, nullable = false)
    private Boolean emailVerified = false;

    @Column(name = C_SALT, nullable = false)
    private String salt;

    @Column(name = C_PASSWORD)
    private String password;

    @Column(name = C_STATUS, nullable = false)
    private UserStatus userStatus = UserStatus.OK;

    @Column(name = C_CREATE_ON, updatable = false)
    private Date createdOn;

    @Column(name = C_DEACTIVATED_ON)
    private Date deactivatedOn;

    @Column(name = C_PICTURE)
    private String picture;

    @ManyToMany
    private List<Permission> permissions = new ArrayList<>();

    @ManyToMany
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = TABLE_NAME, orphanRemoval = true)
    private List<UserToken> tokens = new ArrayList<>();

    @OneToMany(mappedBy = TABLE_NAME, orphanRemoval = true)
    private List<UserCredentials> userCredentials = new ArrayList<>();

    @OneToMany(mappedBy = TABLE_NAME, orphanRemoval = true, cascade = CascadeType.MERGE)
    private List<LoginAttempt> loginAttempts = new ArrayList<>();

    public void setId(UUID id) {
        this.id = id;
    }

    public String getId() {
        return id.toString();
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

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerifyed) {
        this.emailVerified = emailVerifyed;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<UserToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<UserToken> tokens) {
        this.tokens = tokens;
    }

    public List<UserCredentials> getUserCredentials() {
        return userCredentials;
    }

    public void setUserCredentials(List<UserCredentials> userCredentials) {
        this.userCredentials = userCredentials;
    }

    public List<LoginAttempt> getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(List<LoginAttempt> loginAttempts) {
        this.loginAttempts = loginAttempts;
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
