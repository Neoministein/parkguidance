package com.neo.parkguidance.web.impl.pages.lazy;

import com.neo.parkguidance.core.entity.DataBaseEntity;

import javax.inject.Inject;

import static com.neo.parkguidance.web.impl.utils.Utils.addDetailMessage;

public abstract class AbstractLazyController<T extends DataBaseEntity<T>> {

    @Inject
    AbstractLazyModel<T> model;

    @Inject
    AbstractLazyFacade<T> facade;

    protected void init() {
        if(!model.isInstantiated()) {
            clearFilter();
            model.setData(facade.initDataModel(model.getFilter()));
            model.setInstantiated(true);
        }
    }

    public void clearFilter() {
        model.setFilter(facade.newFilter());
    }

    public void delete() {
        int num = facade.delete(model.getSelected());
        if(num != 0) {
            addDetailMessage(num + " ParkingGarage deleted successfully!");
            model.getSelected().clear();
        }
    }

    public AbstractLazyModel<T> getModel() {
        return model;
    }

    protected AbstractLazyFacade<T> getFacade() {
        return facade;
    }
}
