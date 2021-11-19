package com.neo.parkguidance.web;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 * This class returns the string used for in the xhtml template and can be change by overriding it using the {@link javax.enterprise.inject.Specializes} annotation
 */
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
