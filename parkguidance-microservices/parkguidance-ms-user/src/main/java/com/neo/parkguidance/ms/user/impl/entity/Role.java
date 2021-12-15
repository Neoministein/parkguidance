package com.neo.parkguidance.ms.user.impl.entity;

import com.neo.parkguidance.ms.user.api.entity.DataBaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This entity class is used for segregation of parts of the website with user permissions
 */
@Entity
@Table(name = Role.TABLE_NAME, indexes =
@Index(name = "name", columnList = Role.C_NAME))
public class Role implements DataBaseEntity {

    public static final String TABLE_NAME = "role";
    public static final String C_NAME = "name";

    @Id
    @Column(name = DataBaseEntity.C_ID, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = C_NAME, nullable = false, updatable = false)
    private String name;

    @OneToMany()
    private List<Permission> permissions = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Role that = (Role) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
