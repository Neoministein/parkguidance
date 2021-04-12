package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = Permission.TABLE_NAME)
public class Permission implements DataBaseEntity {

    public static final String TABLE_NAME = "permissions";
    public static final String C_ID = "id";
    public static final String C_NAME = "name";

    public static final String SUPER_USER = "superUser";

    @Id
    @Column(name = C_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_NAME)
    @NotNull
    @Size(max = 50)
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
}
