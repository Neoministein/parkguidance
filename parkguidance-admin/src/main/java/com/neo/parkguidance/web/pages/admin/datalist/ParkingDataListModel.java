package com.neo.parkguidance.web.pages.admin.datalist;

import com.neo.parkguidance.entity.ParkingData;
import com.neo.parkguidance.web.infra.table.Filter;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.List;

@ViewScoped
public class ParkingDataListModel implements Serializable {

    private boolean instantiated = false;

    private LazyDataModel<ParkingData> data;

    private List<ParkingData> selected;

    private List<ParkingData> filteredValue;

    private Filter<ParkingData> filter = new Filter<>(new ParkingData());

    public boolean isInstantiated() {
        return instantiated;
    }

    public void setInstantiated(boolean instantiated) {
        this.instantiated = instantiated;
    }

    public LazyDataModel<ParkingData> getData() {
        return data;
    }

    public void setData(LazyDataModel<ParkingData> data) {
        this.data = data;
    }

    public List<ParkingData> getSelected() {
        return selected;
    }

    public void setSelected(List<ParkingData> selected) {
        this.selected = selected;
    }

    public List<ParkingData> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<ParkingData> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Filter<ParkingData> getFilter() {
        return filter;
    }

    public void setFilter(Filter<ParkingData> filter) {
        this.filter = filter;
    }
}
