package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = Address.TABLE_NAME)
public class Address implements Serializable {

    public static final String TABLE_NAME = "address";
    public static final String C_ID = "id_address";
    public static final String C_CITY_NAME = "city_name";
    public static final String C_PLZ = "plz";
    public static final String C_STREET = "street";
    public static final String C_NUMBER = "number";

    @Id
    @Column(name = C_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = C_CITY_NAME)
    @NotNull
    @Size(max = 50)
    private String cityName;

    @Column(name = C_PLZ)
    @NotNull
    @Min(0)
    private int plz;

    @Column(name = C_STREET)
    @NotNull
    @Size(max = 50)
    private String street;

    @Column(name = C_NUMBER)
    @Min(0)
    private int number;

    public Address(@NotNull @Size(max = 50) String cityName, @NotNull @Min(0) int plz,
            @NotNull @Size(max = 50) String street, @Min(0) int number) {
        this.cityName = cityName;
        this.plz = plz;
        this.street = street;
        this.number = number;
    }

    public Address() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getPlz() {
        return plz;
    }

    public void setPlz(int plz) {
        this.plz = plz;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
