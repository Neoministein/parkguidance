package com.neo.parkguidance.core.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = ParkingData.TABLE_NAME)
public class ParkingData implements DataBaseEntity<ParkingData> {

    public static final String TABLE_NAME = "parkingData";
    public static final String C_DATE = "date";
    public static final String C_OCCUPIED = "occupied";
    public static final String C_SORTED = "sorted";

    public ParkingData() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private ParkingGarage parkingGarage;

    @Column(name = C_DATE, nullable = false)
    private Date date;

    @Min(0)
    @Column(name = C_OCCUPIED ,nullable = false)
    private int occupied;

    @Column(name = C_SORTED)
    private Boolean sorted;

    public ParkingData(ParkingGarage parkingGarage, Date date, int occupied) {
        this.parkingGarage = parkingGarage;
        this.date = date;
        this.occupied = occupied;
        this.sorted = false;
    }

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

    public Boolean getSorted() {
        return sorted;
    }

    public void setSorted(Boolean sorted) {
        this.sorted = sorted;
    }

    @Override
    public boolean compareValues(ParkingData o) {
        if(!parkingGarage.compareValues(o.getParkingGarage())) {
            return false;
        }
        if(!date.equals(o.getDate())) {
            return false;
        }
        if (occupied != o.getOccupied()) {
            return false;
        }

        return Objects.equals(sorted,o.getSorted());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ParkingData that = (ParkingData) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
