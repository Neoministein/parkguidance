package com.neo.parkguidance.web.infra.entity.converter;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EntityParkingGarageConverter implements Converter {

    public static final String BEAN_NAME = "entityParkingConverter";

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageEntityService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        ParkingGarage parkingGarage = parkingGarageEntityService.find(Long.valueOf(value));
        if(parkingGarage == null) {
            return new ParkingGarage();
        }
        return parkingGarage;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(!(value instanceof ParkingGarage)) {
            return "";
        }
        return String.valueOf(((ParkingGarage) value).getId());
    }
}
