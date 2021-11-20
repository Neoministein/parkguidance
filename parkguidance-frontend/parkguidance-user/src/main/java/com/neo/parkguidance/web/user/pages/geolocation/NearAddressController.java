package com.neo.parkguidance.web.user.pages.geolocation;

import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.web.user.impl.address.AddressDataService;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * The controller for the NearAddress screen
 */
@RequestScoped
@Named(value = NearAddressController.BEAN_NAME)
public class NearAddressController {

    public static final String BEAN_NAME = "nearAddress";

    @Inject
    NearAddressModel model;

    @Inject AddressDataService staticDataModel;

    @Inject
    GeoLocationFacade facade;

    @PostConstruct
    public void init() {
        if (!model.isInitiated()) {
            model.setAddress(new Address());
            model.setInitiated(true);
        }
    }

    public void findByAddress() {
        model.setDistanceDataObjects(facade.callDistance(model.getAddress()));
        PrimeFaces.current().ajax().update("form");
    }

    public void redirectSearch(ParkingGarage parkingGarage) {
        facade.redirectSearch(parkingGarage);
    }

    public List<String> completeCity(String query) {
        return facade.autoCompleteCity(query, staticDataModel.getAddressList());
    }

    public NearAddressModel getModel() {
        return model;
    }
}
