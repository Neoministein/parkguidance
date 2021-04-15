package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = Address.TABLE_NAME)
public class Address implements DataBaseEntity<Address> {

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

    @Column(name = C_LATITUDE)
    private Double latitude;

    @Column(name = C_LONGITUDE)
    private Double longitude;

    public Address(@NotNull @Size(max = 50) String cityName, @NotNull @Min(0) int plz,
            @NotNull @Size(max = 50) String street, @Min(0) int number) {
        this.cityName = cityName;
        this.plz = plz;
        this.street = street;
        this.number = number;
    }

    public Address(String cityName, int plz, String street, int number, Double latitude, Double longitude) {
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
    public boolean compareValues(Address o) {
        if (!cityName.equals(o.cityName)) {
            return false;
        }
        if (plz != o.getPlz()) {
            return false;
        }
        if (!street.equals(o.getStreet())) {
            return false;
        }
        if (number != o.getNumber()) {
            return false;
        }
        if (!Objects.equals(longitude,o.getLongitude())) {
            return false;
        }
        return Objects.equals(latitude, o.getLatitude());
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
        return street + " " + number + "\n" + plz + " " + cityName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
