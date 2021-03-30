package com.neo.parkguidance.web.admin.pages.sheetlist;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.web.infra.table.Filter;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;

import java.io.Serializable;
import java.util.List;

@ViewScoped
public class DataSheetListModel implements Serializable {

    private boolean instantiated = false;
    private boolean sorterOffline;

    private LazyDataModel<DataSheet> data;
    private List<DataSheet> selected;
    private List<DataSheet> filteredValue;
    private Filter<DataSheet> filter = new Filter<>(new DataSheet());

    public boolean isInstantiated() {
        return instantiated;
    }

    public void setInstantiated(boolean instantiated) {
        this.instantiated = instantiated;
    }

    public boolean isSorterOffline() {
        return sorterOffline;
    }

    public void setSorterOffline(boolean sorterOffline) {
        this.sorterOffline = sorterOffline;
    }

    public LazyDataModel<DataSheet> getData() {
        return data;
    }

    public void setData(LazyDataModel<DataSheet> data) {
        this.data = data;
    }

    public List<DataSheet> getSelected() {
        return selected;
    }

    public void setSelected(List<DataSheet> selected) {
        this.selected = selected;
    }

    public List<DataSheet> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<DataSheet> filteredValue) {
        this.filteredValue = filteredValue;
    }

    public Filter<DataSheet> getFilter() {
        return filter;
    }

    public void setFilter(Filter<DataSheet> filter) {
        this.filter = filter;
    }
}
