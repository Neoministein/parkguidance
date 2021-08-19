package com.neo.parkguidance.web.impl.pages.lazy;

import com.neo.parkguidance.core.entity.DataBaseEntity;

import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;

public abstract class AbstractLazyController<T extends DataBaseEntity<T>> {

    protected void init() {
        if(!getModel().isInstantiated()) {
            clearFilter();
            getModel().setData(getFacade().initDataModel(getModel().getFilter()));
            getModel().setInstantiated(true);
        }
    }

    public void clearFilter() {
        getModel().setFilter(getFacade().newFilter());
    }

    public void delete() {
        int num = getFacade().delete(getModel().getSelected());
        if(num != 0) {
            addDetailMessage(num + " ParkingGarage deleted successfully!");
            getModel().getSelected().clear();
        }
    }

    public abstract AbstractLazyModel<T> getModel();

    protected abstract AbstractLazyFacade<T> getFacade();
}
