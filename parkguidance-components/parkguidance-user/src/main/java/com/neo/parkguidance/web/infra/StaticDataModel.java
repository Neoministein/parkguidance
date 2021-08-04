package com.neo.parkguidance.web.infra;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.impl.event.DataBaseEntityChangeEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

/**
 * This class contains static data used for autocomplete
 */
@ApplicationScoped
public class StaticDataModel {

    private List<Address> addressList;

    @Inject
    StaticDataFacade facade;

    @PostConstruct
    public void init() {
        addressList = facade.getAddress();
    }

    public void addressUpdate(@Observes DataBaseEntityChangeEvent<Address> changeEvent) {
        switch (changeEvent.getStatus()) {
        case DataBaseEntityChangeEvent.CREATE:
            addressList.add(changeEvent.getChangedObject());
            break;
        case DataBaseEntityChangeEvent.REMOVE:
            addressList.remove(changeEvent.getChangedObject());
            break;
        case DataBaseEntityChangeEvent.EDIT:
        default:
            addressList = facade.getAddress();
            break;
        }
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
