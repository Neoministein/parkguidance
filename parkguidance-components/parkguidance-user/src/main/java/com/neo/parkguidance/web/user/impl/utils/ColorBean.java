package com.neo.parkguidance.web.user.impl.utils;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.user.impl.UserConfig;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@RequestScoped
@Named(ColorBean.BEAN_NAME)
public class ColorBean {

    public static final String BEAN_NAME = "colorBean";

    @Inject
    UserConfig config;


    public String getStyleColorOfOccupied(ParkingGarage parkingGarage) {
        return ColorGenerator.getStyleColor(
                parkingGarage.getSpaces(),
                parkingGarage.getOccupied(),
                config.isColorBlind());
    }

}
