package com.neo.parkguidance.web.user.pages.geolocation;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.google.api.maps.CrossPlatformURL;
import com.neo.parkguidance.web.infra.StaticDataModel;
import org.primefaces.PrimeFaces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.List;


@RequestScoped
@Named(value = NearAddressController.BEAN_NAME)
public class NearAddressController {

    public static final String BEAN_NAME = "nearAddress";

    @Inject
    NearAddressModel model;

    @Inject
    StaticDataModel staticDataModel;

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

    public void redirectSearch(ParkingGarage parkingGarage) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(CrossPlatformURL.search(parkingGarage));
    }

    public List<String> completeCity(String query) {
        return facade.autoCompleteCity(query, staticDataModel.getAddressList());
    }

    public NearAddressModel getModel() {
        return model;
    }
}
