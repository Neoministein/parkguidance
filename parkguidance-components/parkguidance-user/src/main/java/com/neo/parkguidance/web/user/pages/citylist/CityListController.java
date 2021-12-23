package com.neo.parkguidance.web.user.pages.citylist;

import com.neo.parkguidance.core.entity.ParkingGarage;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * The controller for the ParkList screen
 */
@RequestScoped
@Named(value = CityListController.BEAN_NAME)
public class CityListController {

    public static final String BEAN_NAME = "cityList";

    private String cityName;
    private String urlParam;

    @Inject
    CityListModel model;

    @Inject
    CityListFacade facade;

    public void initUrl() {
        cityName = facade.validateURLInput(urlParam, model.getGarageData().keySet());
    }

    public void initData() {
        facade.initDataModel(model);
    }

    public List<ParkingGarage> getParkingGarage() {
        return model.getGarageData().getOrDefault(getCityName(), new ArrayList<>());
    }

    public List<String> getCities() {
        return model.getCities();
    }

    public String getCityName() {
        return cityName;
    }
    public String getUrlParam() {
        return urlParam;
    }

    public void setUrlParam(String urlParam) {
        this.urlParam = urlParam;
    }

    public CityListModel getModel() {
        return model;
    }
}
