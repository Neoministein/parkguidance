package com.neo.parkguidance.web.infra;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@ApplicationScoped
@Named(value = TemplateValues.BEAN_NAME)
public class TemplateValues {
    public static final String BEAN_NAME = "templateValues";

    public String getTitle() {
        return "Admin";
    }

    public String getLogo(){
        return "Admin Panel";
    }

    public String getLogoMini() {
        return "A";
    }
}
