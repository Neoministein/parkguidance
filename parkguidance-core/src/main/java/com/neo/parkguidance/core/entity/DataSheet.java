package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = DataSheet.TABLE_NAME)
public class DataSheet implements DataBaseEntity<DataSheet> {

    public static final String TABLE_NAME = "dataSheet";
    public static final String C_YEAR = "year";
    public static final String C_WEEK = "week";
    public static final String C_DAY = "day";
    public static final String C_HALF_HOUR = "halfHour";
    public static final String C_OCCUPIED = "occupied";
    public static final String C_WAITING_TIME = "waitingTime";

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
    private Integer week;

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

    @Column(name = C_WAITING_TIME)
    @Min(0)
    @NotNull
    private Integer waitingTime;

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

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
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

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(Integer waitingTime) {
        this.waitingTime = waitingTime;
    }

    public ParkingGarage getParkingGarage() {
        return parkingGarage;
    }

    public void setParkingGarage(ParkingGarage parkingGarage) {
        this.parkingGarage = parkingGarage;
    }

    @Override
    public boolean compareValues(DataSheet o) {
        if (!year.equals(o.getYear())) {
            return false;
        }
        if (!week.equals(o.getWeek())) {
            return false;
        }
        if (!day.equals(o.getDay())) {
            return false;
        }
        if(!halfHour.equals(o.getHalfHour())) {
            return false;
        }
        if(!occupied.equals(o.getOccupied())) {
            return false;
        }
        if(!waitingTime.equals(o.getWaitingTime())) {
            return false;
        }
        return parkingGarage.compareValues(o.getParkingGarage());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DataSheet dataSheet = (DataSheet) o;
        return id.equals(dataSheet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
