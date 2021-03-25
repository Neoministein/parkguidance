package com.neo.parkguidance.web.infra;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Specializes;

@Specializes
@ApplicationScoped
public class UserTemplate extends TemplateValues {

    public String getTitle() {
        return "Parkleitsystem";
    }

    public String getLogo(){
        return "Parkleitsystem";
    }

    public String getLogoMini() {
        return "";
    }
}
