package com.neo.parkguidance.framework.entity;

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
    public static final String C_ID = "id";
    public static final String C_COUNTRY = "country";
    public static final String C_CITY_NAME = "city_name";
    public static final String C_ZIP_CODE = "zip_code";
    public static final String C_STREET = "street";
    public static final String C_HOUSE_NUMBER = "number_house";
    public static final String C_LONGITUDE = "longitude";
    public static final String C_LATITUDE = "latitude";

    @Id
    @Column(name = C_ID, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    @Column(name = C_COUNTRY, nullable = false)
    private String country;

    @Size(max = 50)
    @Column(name = C_CITY_NAME, nullable = false)
    private String cityName;

    @Min(0)
    @Column(name = C_ZIP_CODE, nullable = false)
    private Integer zipCode;

    @Size(max = 50)
    @Column(name = C_STREET, nullable = false)
    private String street;

    @Min(0)
    @Column(name = C_HOUSE_NUMBER)
    private Integer houseNumber;

    @Column(name = C_LATITUDE)
    private Double latitude;

    @Column(name = C_LONGITUDE)
    private Double longitude;

    public Address(@Size(max = 50) String cityName, @Min(0) int zipCode,
            @Size(max = 50) String street, @Min(0) int houseNumber) {
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public Address(String country, String cityName, Integer zipCode, String street, Integer houseNumber, Double latitude, Double longitude) {
        this.country = country;
        this.cityName = cityName;
        this.zipCode = zipCode;
        this.street = street;
        this.houseNumber = houseNumber;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer plz) {
        this.zipCode = plz;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer number) {
        this.houseNumber = number;
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
        return street + " " + houseNumber + " " + zipCode + " " + cityName;
    }

    public String getToAddressString() {
        return street + " " + ((houseNumber == null) ? "" : houseNumber) +
                "\n" + zipCode + " " + cityName +
                "\n" + country;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
