package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = DataSheet.TABLE_NAME)
public class DataSheet implements Serializable {

    public static final String TABLE_NAME = "dataSheet";
    public static final String C_YEAR = "year";
    public static final String C_WEEK = "week";
    public static final String C_DAY = "day";
    public static final String C_HALF_HOUR = "halfHour";
    public static final String C_OCCUPIED = "occupied";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = C_YEAR)
    @NotNull
    private Integer year;

    @Column(name = C_WEEK)
    @Min(1)
    @Max(53)
    @NotNull
    private Enum week;

    @Column(name = C_DAY)
    @Min(0)
    @Max(6)
    @NotNull
    private Integer day;

    @Column(name = C_HALF_HOUR)
    @Min(0)
    @Max(48)
    @NotNull
    private Integer halfHour;

    @Column(name = C_OCCUPIED)
    @Min(0)
    @NotNull
    private Integer occupied;

    @ManyToOne
    @NotNull
    private ParkingGarage parkingGarage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Enum getWeek() {
        return week;
    }

    public void setWeek(Enum week) {
        this.week = week;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHalfHour() {
        return halfHour;
    }

    public void setHalfHour(Integer halfHour) {
        this.halfHour = halfHour;
    }

    public Integer getOccupied() {
        return occupied;
    }

    public void setOccupied(Integer occupied) {
        this.occupied = occupied;
    }

    public ParkingGarage getParkingGarage() {
        return parkingGarage;
    }

    public void setParkingGarage(ParkingGarage parkingGarage) {
        this.parkingGarage = parkingGarage;
    }
}
