package com.neo.parkguidance.web.admin.pages.sheetfrom;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingGarage;
import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;
import java.util.List;

@ViewScoped
public class DataSheetFormModel implements Serializable {

    private Integer id;
    private DataSheet item;

    private List<ParkingGarage> parkingGarages;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DataSheet getItem() {
        return item;
    }

    public void setItem(DataSheet item) {
        this.item = item;
    }

    public List<ParkingGarage> getParkingGarages() {
        return parkingGarages;
    }

    public void setParkingGarages(List<ParkingGarage> parkingGarages) {
        this.parkingGarages = parkingGarages;
    }
}
