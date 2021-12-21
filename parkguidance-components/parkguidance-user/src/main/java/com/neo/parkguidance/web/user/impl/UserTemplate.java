package com.neo.parkguidance.web.user.impl;

import com.neo.parkguidance.web.TemplateValues;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;

/**
 * This class extends {@link TemplateValues} which returns parameters for the xhtml Template
 */
@Specializes
@ApplicationScoped
public class UserTemplate extends TemplateValues {

    @Override
    public String getTitle() {
        return "Parkleitsystem";
    }

    @Override
    public String getLogo(){
        return "Parkleitsystem";
    }

    @Override
    public String getLogoMini() {
        return "";
    }
}
