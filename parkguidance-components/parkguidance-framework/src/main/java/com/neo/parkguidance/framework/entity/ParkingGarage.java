package com.neo.parkguidance.framework.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This entity class is used for storing data of the parking garages
 */
@Entity
@Table(name = ParkingGarage.TABLE_NAME)
public class ParkingGarage implements DataBaseEntity {

    public static final String TABLE_NAME = "parkingGarage";
    public static final String C_KEY = "key";
    public static final String C_NAME = "name";
    public static final String C_SPACES = "spaces";
    public static final String C_OCCUPIED = "occupied";
    public static final String C_ACCESS_KEY = "accessKey";
    public static final String C_PRICE = "price";

    @Id
    @Column(name = C_KEY)
    private String key;

    @Size(max = 50)
    @Column(name = C_NAME, nullable = false)
    private String name;

    @Min(0)
    @Column(name = C_SPACES, nullable = false)
    private Integer spaces;

    @Column(name = C_OCCUPIED, nullable = false)
    private Integer occupied;

    @Column(name = C_ACCESS_KEY, nullable = false)
    private String accessKey;

    @OneToOne
    @JoinColumn(name = Address.TABLE_NAME, nullable = false)
    private Address address;

    @OneToMany(mappedBy = TABLE_NAME)
    private List<GarageInfo> garageInfo;

    public boolean isEmpty() {
        return occupied == -1;
    }

    public ParkingGarage() {
        this.address = new Address();
        this.garageInfo = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String id) {
        this.key = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSpaces() {
        return spaces;
    }

    public void setSpaces(Integer spaces) {
        this.spaces = spaces;
    }

    public Integer getOccupied() {
        return occupied;
    }

    public void setOccupied(Integer occupied) {
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

    public List<GarageInfo> getGarageInfo() {
        return garageInfo;
    }

    public void setGarageInfo(List<GarageInfo> garageInfo) {
        this.garageInfo = garageInfo;
    }

    @Override
    public Object getPrimaryKey() {
        return getKey();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ParkingGarage that = (ParkingGarage) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return name;
    }
}
