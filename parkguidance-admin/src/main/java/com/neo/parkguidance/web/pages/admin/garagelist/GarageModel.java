package com.neo.parkguidance.web.pages.admin.garagelist;

import com.neo.parkguidance.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.table.Filter;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.List;

@ViewScoped
public class GarageModel implements Serializable {

    private boolean instantiated = false;

    private LazyDataModel<ParkingGarage> parkingGarages;

    private List<ParkingGarage> selectedGarages;

    private List<ParkingGarage> filteredValue;

    private Filter<ParkingGarage> filter = new Filter<>(new ParkingGarage());

    public boolean isInstantiated() {
        return instantiated;
    }

    public void setInstantiated(boolean instantiated) {
        this.instantiated = instantiated;
    }

    public LazyDataModel<ParkingGarage> getParkingGarages() {
        return parkingGarages;
    }

    public void setParkingGarages(LazyDataModel<ParkingGarage> parkingGarages) {
        this.parkingGarages = parkingGarages;
    }

    public List<ParkingGarage> getSelectedGarages() {
        return selectedGarages;
    }

    public void setSelectedGarages(List<ParkingGarage> selectedGarages) {
        this.selectedGarages = selectedGarages;
    }

    public List<ParkingGarage> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<ParkingGarage> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Filter<ParkingGarage> getFilter() {
        return filter;
    }

    public void setFilter(Filter<ParkingGarage> filter) {
        this.filter = filter;
    }
}
