package com.neo.parkguidance.web.infra;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;

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
