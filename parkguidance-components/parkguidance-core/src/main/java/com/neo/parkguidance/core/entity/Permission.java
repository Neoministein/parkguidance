package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = Permission.TABLE_NAME)
public class Permission implements DataBaseEntity<Permission> {

    public static final String TABLE_NAME = "permissions";
    public static final String C_ID = "id";
    public static final String C_NAME = "name";

    public static final String SUPER_USER = "superUser";

    @Id
    @Column(name = C_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = C_NAME, nullable = false)
    private String name;

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

    @Override
    public Object getPrimaryKey() {
        return getId();
    }

    @Override
    public boolean compareValues(Permission o) {
        return name.equals(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Permission that = (Permission) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
