package com.neo.parkguidance.web.impl.entity.converter;

import com.neo.parkguidance.core.entity.ParkingGarage;
import javax.inject.Named;

/**
 * This class is {@link javax.faces.convert.Converter} impl for the {@link ParkingGarage} entity
 */
@Named
public class EntityParkingGarageConverter extends AbstractDataBaseEntityConverter<ParkingGarage> {

    public static final String BEAN_NAME = "entityParkingConverter";

    @Override
    protected ParkingGarage newInstance() {
        return new ParkingGarage();
    }
}
