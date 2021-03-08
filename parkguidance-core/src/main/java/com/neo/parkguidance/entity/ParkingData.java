package com.neo.parkguidance.entity;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = ParkingData.TABLE_NAME)
public class ParkingData implements Serializable {

    public static final String TABLE_NAME = "parkingData";
    public static final String C_DATE = "DATE";
    public static final String C_OCCUPIED = "occupied";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private ParkingGarage parkingGarage;

    @Column(name = C_DATE)
    @NotNull
    @Size(max = 50)
    private Date date;

    @Column(name = C_OCCUPIED)
    @NotNull
    @Min(0)
    private int occupied;
}
