package com.neo.parkguidance.web.impl.pages.lazy;

import com.neo.parkguidance.framework.entity.DataBaseEntity;
import com.neo.parkguidance.web.impl.table.Filter;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractLazyModel<T extends DataBaseEntity> implements Serializable {

    private boolean instantiated = false;

    private List<T> selected;
    private List<T> filteredValue;
    private LazyDataModel<T> data;
    private Filter<T> filter = new Filter<>();

    public boolean isInstantiated() {
        return instantiated;
    }

    public void setInstantiated(boolean instantiated) {
        this.instantiated = instantiated;
    }

    public List<T> getSelected() {
        return selected;
    }

    public void setSelected(List<T> selected) {
        this.selected = selected;
    }

    public List<T> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<T> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public LazyDataModel<T> getData() {
        return data;
    }

    public void setData(LazyDataModel<T> data) {
        this.data = data;
    }

    public Filter<T> getFilter() {
        return filter;
    }

    public void setFilter(Filter<T> filter) {
        this.filter = filter;
    }
}
