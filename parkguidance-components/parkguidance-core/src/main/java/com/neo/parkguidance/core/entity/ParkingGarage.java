package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * This entity class is used for storing data of the parking garages
 */
@Entity
@Table(name = ParkingGarage.TABLE_NAME)
public class ParkingGarage implements DataBaseEntity<ParkingGarage> {

    public static final String TABLE_NAME = "parkingGarage";
    public static final String C_KEY = "key";
    public static final String C_NAME = "name";
    public static final String C_SPACES = "spaces";
    public static final String C_OCCUPIED = "occupied";
    public static final String C_ACCESS_KEY = "accessKey";
    public static final String C_PRICE = "price";
    public static final String C_OPERATOR = "operator";
    public static final String C_DESCRIPTION = "description";

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

    @Column(name = C_PRICE)
    private String price;

    @Column(name = C_OPERATOR)
    private String operator;

    @Column(name = C_DESCRIPTION)
    private String description;

    public ParkingGarage() {
        this.address = new Address();
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
    public Object getPrimaryKey() {
        return getKey();
    }

    @Override
    public boolean compareValues(ParkingGarage o) {
        if(!name.equals(o.getName())) {
            return false;
        }

        if(spaces.equals(o.getSpaces())) {
            return false;
        }
        if(occupied.equals(o.getOccupied())) {
            return false;
        }
        if(!accessKey.equals(o.getAccessKey())) {
            return false;
        }
        if(!address.compareValues(o.getAddress())) {
            return false;
        }
        if(!Objects.equals(price,o.getPrice())) {
            return false;
        }
        if(!Objects.equals(operator, o.getOperator())) {
            return false;
        }
        return Objects.equals(description, o.getDescription());
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
