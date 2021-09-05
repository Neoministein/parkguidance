package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = UserToken.TABLE_NAME)
public class UserToken implements DataBaseEntity{

    public static final String TABLE_NAME = "usertoken";

    public static final String C_KEY = "key";
    public static final String T_USER = "userid";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_KEY, nullable = false)
    private String key;

    @ManyToMany
    private List<Permission> permissions;

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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public Object getPrimaryKey() {
        return getId();
    }
}
