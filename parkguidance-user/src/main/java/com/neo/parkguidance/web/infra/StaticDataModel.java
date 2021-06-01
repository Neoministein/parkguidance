package com.neo.parkguidance.web.infra;

import com.neo.parkguidance.core.entity.Address;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class StaticDataModel {

    private List<Address> addressList;


    @Inject
    StaticDataFacade facade;

    @PostConstruct
    public void init() {
        addressList = facade.getAddress();
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
