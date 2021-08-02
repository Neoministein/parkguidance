package com.neo.parkguidance.web.admin.pages.storedvaluelist;

import com.neo.parkguidance.core.entity.StoredValue;
import com.neo.parkguidance.web.infra.table.Filter;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.List;

/**
 * The screen model for the StoredValueList screen
 */
@ViewScoped
public class StoredValueListModel implements Serializable {

    private boolean initialized = false;

    private List<StoredValue> selected;
    private List<StoredValue> filteredValue;
    private LazyDataModel<StoredValue> data;
    private Filter<StoredValue> filter = new Filter<>(new StoredValue());

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public List<StoredValue> getSelected() {
        return selected;
    }

    public void setSelected(List<StoredValue> selected) {
        this.selected = selected;
    }

    public List<StoredValue> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<StoredValue> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public LazyDataModel<StoredValue> getData() {
        return data;
    }

    public void setData(LazyDataModel<StoredValue> data) {
        this.data = data;
    }

    public Filter<StoredValue> getFilter() {
        return filter;
    }

    public void setFilter(Filter<StoredValue> filter) {
        this.filter = filter;
    }
}
