package com.neo.parkguidance.framework.entity;

import javax.persistence.*;

/**
 * This entity contains info data in regrades to a {@link ParkingGarage}
 */
@Entity
@Table(name = GarageInfo.TABLE_NAME)
public class GarageInfo implements DataBaseEntity {

    public static final String TABLE_NAME = "garageInfo";

    public static final String C_KEY = "key";
    public static final String C_VALUE = "value";

    @Id
    @Column(name = DataBaseEntity.C_ID, columnDefinition = "serial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = C_KEY, nullable = false)
    private String key;

    @Column(name = C_VALUE, nullable = false)
    private String value;

    @ManyToOne
    private ParkingGarage parkingGarage;

    public GarageInfo() {}

    public GarageInfo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ParkingGarage getParkingGarage() {
        return parkingGarage;
    }

    public void setParkingGarage(ParkingGarage parkingGarage) {
        this.parkingGarage = parkingGarage;
    }

    @Override
    public Object getPrimaryKey() {
        return getId();
    }
}
