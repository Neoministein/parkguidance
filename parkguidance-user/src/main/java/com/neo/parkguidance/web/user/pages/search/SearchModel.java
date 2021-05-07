package com.neo.parkguidance.web.user.pages.search;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.table.Filter;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;

@ViewScoped
public class SearchModel implements Serializable {

    private boolean instantiated = false;
    private Filter<ParkingGarage> filter;
    private LazyDataModel<ParkingGarage> lazyDataModel;

    public boolean isInstantiated() {
        return instantiated;
    }

    public void setInstantiated(boolean instantiated) {
        this.instantiated = instantiated;
    }

    public Filter<ParkingGarage> getFilter() {
        return filter;
    }

    public void setFilter(Filter<ParkingGarage> filter) {
        this.filter = filter;
    }

    public LazyDataModel<ParkingGarage> getLazyDataModel() {
        return lazyDataModel;
    }

    public void setLazyDataModel(LazyDataModel<ParkingGarage> lazyDataModel) {
        this.lazyDataModel = lazyDataModel;
    }
}
