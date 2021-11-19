package com.neo.parkguidance.web.user.impl;

import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named(UserConfig.BEAN_NAME)
public class UserConfig implements Serializable {

    public static final String BEAN_NAME = "userConfig";

    public static final String COOKIE_COLOR_BLIND = "colorBlind";
    public static final int TEN_YEARS = 60 * 60 * 24 * 365 * 10;

    private boolean colorBlind = false;

    @PostConstruct
    protected void init() {
        colorBlind = Boolean.parseBoolean(Faces.getRequestCookie(COOKIE_COLOR_BLIND));
    }

    public void reload() {
        init();
    }

    public void setColorBlind(boolean colorBlind) {
        this.colorBlind = colorBlind;
        Faces.addResponseCookie(COOKIE_COLOR_BLIND, String.valueOf(colorBlind), TEN_YEARS);
    }

    public boolean isColorBlind() {
        return colorBlind;
    }
}
