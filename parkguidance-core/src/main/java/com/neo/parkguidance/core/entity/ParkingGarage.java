package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = ParkingGarage.TABLE_NAME)
public class ParkingGarage implements DataBaseEntity, Comparable<ParkingGarage> {
    public static final String C_ID = "id";
    public static final String TABLE_NAME = "parkingGarage";
    public static final String C_NAME = "name";
    public static final String C_SPACES = "spaces";
    public static final String C_OCCUPIED = "occupied";
    public static final String C_ACCESS_KEY = "accessKey";
    public static final String C_PRICE = "price";
    public static final String C_OPERATOR = "operator";
    public static final String C_DESCRIPTION = "description";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = C_NAME)
    @NotNull
    @Size(max = 50)
    private String name;

    @Column(name = C_SPACES)
    @Min(0)
    @NotNull
    private int spaces;

    @Column(name = C_OCCUPIED)
    @NotNull
    private int occupied;

    @Column(name = C_ACCESS_KEY)
    @NotNull
    private String accessKey;

    @JoinColumn(name = Address.TABLE_NAME)
    @OneToOne
    @NotNull
    private Address address;

    @Column(name = C_PRICE)
    private String price;

    @Column(name = C_OPERATOR)
    private String operator;

    @Column(name = C_DESCRIPTION)
    private String description;

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

    public int getSpaces() {
        return spaces;
    }

    public void setSpaces(int spaces) {
        this.spaces = spaces;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public int compareTo(ParkingGarage o) {
        return Long.compare(this.getId(), o.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ParkingGarage that = (ParkingGarage) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }
}
