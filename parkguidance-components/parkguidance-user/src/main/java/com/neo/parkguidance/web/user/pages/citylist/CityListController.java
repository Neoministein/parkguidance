package com.neo.parkguidance.web.user.pages.citylist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.user.impl.utils.ColorGenerator;
import com.neo.parkguidance.web.user.impl.UserConfig;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * The controller for the ParkList screen
 */
@RequestScoped
@Named(value = CityListController.BEAN_NAME)
public class CityListController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityListController.class);

    public static final String BEAN_NAME = "cityList";

    private String cityName;
    private String urlParam;

    @Inject
    CityListModel model;

    @Inject
    CityListFacade facade;

    @Inject
    UserConfig config;

    public void init() {
        facade.initDataModel(model);

        try {
            cityName = URLDecoder.decode(urlParam, StandardCharsets.UTF_8.toString());
            if (model.getGarageData().containsKey(cityName)) {
                return;
            }
        } catch (Exception ex) {
            LOGGER.error("The provided city name isn't valid {}", urlParam);
        }
        Messages.addError(null, "Ungültige Stadt");
    }

    public List<ParkingGarage> getParkingGarage() {
        return model.getGarageData().getOrDefault(getCityName(), new ArrayList<>());
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

    public String getStyleColorOfOccupied(ParkingGarage parkingGarage) {
        return ColorGenerator.getStyleColor(
                parkingGarage.getSpaces(),
                parkingGarage.getOccupied(),
                config.isColorBlind());
    }
}
