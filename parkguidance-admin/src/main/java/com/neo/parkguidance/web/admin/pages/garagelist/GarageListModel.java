package com.neo.parkguidance.web.admin.pages.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.table.Filter;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.List;

@ViewScoped
public class GarageListModel implements Serializable {

    private boolean instantiated = false;

    private LazyDataModel<ParkingGarage> data;

    private List<ParkingGarage> selected;

    private List<ParkingGarage> filteredValue;

    private Filter<ParkingGarage> filter = new Filter<>(new ParkingGarage());

    public boolean isInstantiated() {
        return instantiated;
    }

    public void setInstantiated(boolean instantiated) {
        this.instantiated = instantiated;
    }

    public LazyDataModel<ParkingGarage> getData() {
        return data;
    }

    public void setData(LazyDataModel<ParkingGarage> data) {
        this.data = data;
    }

    public List<ParkingGarage> getSelected() {
        return selected;
    }

    public void setSelected(List<ParkingGarage> selected) {
        this.selected = selected;
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
