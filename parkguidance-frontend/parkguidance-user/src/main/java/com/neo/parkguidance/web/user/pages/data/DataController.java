package com.neo.parkguidance.web.user.pages.data;

import com.neo.parkguidance.framework.api.geomap.component.EmbeddedMapComponentLogic;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.omnifaces.util.Faces;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * The controller for the Data screen
 */
@RequestScoped
@Named(value = DataController.BEAN_NAME)
public class DataController {

    public static final String BEAN_NAME = "data";

    @Inject
    DataModel model;

    @Inject
    DataFacade facade;

    public void init() {
        if(Faces.isAjaxRequest()){
            return;
        }

        if(!model.isInitialized()) {
            model.setParkingGarage(facade.getParkingGarage(model.getKey()));
            if (model.getParkingGarage() == null) {
                Utils.addDetailMessage("Parking Garage " + model.getKey() + " does not exists");
                facade.redirectToIndex();
                return;
            }

            model.setCartesianLinerModel(facade.createCartesianLinerModel(model.getParkingGarage()));
            model.setInitialized(true);
        }
    }

    public EmbeddedMapComponentLogic getEmbeddedMapLogic() {
        return facade.getEmbeddedMapLogic();
    }

    public DataModel getModel() {
        return model;
    }
}
