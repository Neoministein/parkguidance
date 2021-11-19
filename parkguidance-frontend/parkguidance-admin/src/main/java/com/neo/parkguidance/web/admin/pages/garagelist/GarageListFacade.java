package com.neo.parkguidance.web.admin.pages.garagelist;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.impl.pages.lazy.AbstractLazyFacade;
import com.neo.parkguidance.web.impl.table.Filter;

import javax.ejb.Stateless;

/**
 * The screen facade for the GarageList screen
 */
@Stateless
public class GarageListFacade extends AbstractLazyFacade<ParkingGarage> {

    @Override
    public Filter<ParkingGarage> newFilter() {
        return new Filter<>(new ParkingGarage());
    }
}
