package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * This entity class is only used as part of the {@link ParkingGarage} entity
 */
@Entity
@Table(name = Address.TABLE_NAME)
public class Address implements DataBaseEntity {

    public static final String TABLE_NAME = "address";
    public static final String C_ID = "id_address";
    public static final String C_CITY_NAME = "city_name";
    public static final String C_PLZ = "plz";
    public static final String C_STREET = "street";
    public static final String C_NUMBER = "number";
    public static final String C_LONGITUDE = "longitude";
    public static final String C_LATITUDE = "latitude";

    @Id
    @Column(name = C_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = C_CITY_NAME, nullable = false)
    private String cityName;

    @Min(0)
    @Column(name = C_PLZ, nullable = false)
    private Integer plz;

    @Size(max = 50)
    @Column(name = C_STREET, nullable = false)
    private String street;

    @Min(0)
    @Column(name = C_NUMBER)
    private Integer number;

    @Column(name = C_LATITUDE)
    private Double latitude;

    @Column(name = C_LONGITUDE)
    private Double longitude;

    public Address(@Size(max = 50) String cityName, @Min(0) int plz,
            @Size(max = 50) String street, @Min(0) int number) {
        this.cityName = cityName;
        this.plz = plz;
        this.street = street;
        this.number = number;
    }

    public Address(String cityName, Integer plz, String street, Integer number, Double latitude, Double longitude) {
        this.cityName = cityName;
        this.plz = plz;
        this.street = street;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Address() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getPlz() {
        return plz;
    }

    public void setPlz(Integer plz) {
        this.plz = plz;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
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
        Address address = (Address) o;
        return id.equals(address.id);
    }

    @Override
    public String toString() {
        return street + " " + number + " " + plz + " " + cityName;
    }

    public String getToAddressString() {
        return street + " " + ((number == null) ? "" : number) + "\n" + plz + " " + cityName + " ";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
