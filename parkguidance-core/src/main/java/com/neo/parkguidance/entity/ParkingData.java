package com.neo.parkguidance.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = ParkingData.TABLE_NAME)
public class ParkingData implements Serializable {

    public static final String TABLE_NAME = "parkingData";
    public static final String C_DATE = "DATE";
    public static final String C_OCCUPIED = "occupied";

    public ParkingData() {}

    public ParkingData(ParkingGarage parkingGarage, Date date, int occupied) {
        this.parkingGarage = parkingGarage;
        this.date = date;
        this.occupied = occupied;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private ParkingGarage parkingGarage;

    @Column(name = C_DATE)
    @NotNull
    private Date date;

    @Column(name = C_OCCUPIED)
    @NotNull
    @Min(0)
    private int occupied;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParkingGarage getParkingGarage() {
        return parkingGarage;
    }

    public void setParkingGarage(ParkingGarage parkingGarage) {
        this.parkingGarage = parkingGarage;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }
}
