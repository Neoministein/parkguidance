package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.impl.validation.AddressValidator;
import com.neo.parkguidance.core.impl.validation.ParkingGarageValidator;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormFacade;

import javax.ejb.Stateless;
import javax.inject.Inject;


/**
 * The screen facade for the GarageForm screen
 */
@Stateless
public class GarageFormFacade extends AbstractFormFacade<ParkingGarage> {

    @Inject
    EntityDao<Address> addressDao;

    @Inject
    ParkingGarageValidator parkingGarageValidator;

    @Inject
    AddressValidator addressValidator;

    @Override
    public ParkingGarage newEntity() {
        return new ParkingGarage();
    }

    @Override
    public void edit(ParkingGarage parkingGarage) {
        if(addressValidator.hasNothingChanged(parkingGarage.getAddress())) {
            addressDao.edit(parkingGarage.getAddress());
        }
        super.edit(parkingGarage);
    }

    @Override
    public void create(ParkingGarage parkingGarage) {
        parkingGarage.setKey(parkingGarage.getKey().toUpperCase());
        addressDao.create(parkingGarage.getAddress());
        super.create(parkingGarage);
    }

    public void resetAccessKey(ParkingGarage parkingGarage) {
        parkingGarageValidator.newUniqueAccessKey(parkingGarage);
        getDao().edit(parkingGarage);
    }
}
