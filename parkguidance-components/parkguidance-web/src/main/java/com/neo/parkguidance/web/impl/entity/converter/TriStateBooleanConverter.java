package com.neo.parkguidance.web.impl.entity.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

/**
 * This class is {@link javax.faces.convert.Converter} impl for using the p:triStateCheckbox component
 */
@SuppressWarnings("java:S2447") //Ignoring that booleans shouldn't be null, due to how triStateCheckbox work
@Named(value = TriStateBooleanConverter.BEAN_NAME)
public class TriStateBooleanConverter implements Converter<Boolean> {

    public static final String BEAN_NAME = "tristatebooleanconverter";

    @Override
    public Boolean getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) {
            return null;
        }
        switch (value) {
        case "0": return null;
        case "1": return Boolean.TRUE;
        case "2": return Boolean.FALSE;
        default: throw new ConverterException();
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Boolean value) {
        if (value == null) {
            return "0";
        }
        return Boolean.TRUE.equals(value) ? "1" : "2";
    }
}
