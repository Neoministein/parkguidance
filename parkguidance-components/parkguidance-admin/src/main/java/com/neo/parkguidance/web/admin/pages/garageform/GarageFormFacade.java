package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.google.api.maps.GeoCoding;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.utils.RandomString;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.impl.pages.form.AbstractFormFacade;

import javax.ejb.Stateless;
import javax.inject.Inject;


/**
 * The screen facade for the GarageForm screen
 */
@Stateless
public class GarageFormFacade extends AbstractFormFacade<ParkingGarage> {

    @Inject
    AbstractEntityDao<Address> addressDao;

    @Inject
    GeoCoding geoCoding;

    @Override
    public ParkingGarage newEntity() {
        return new ParkingGarage();
    }

    @Override
    public void edit(ParkingGarage parkingGarage) {
        if(hasAddressChanged(parkingGarage.getAddress())) {
            geoCoding.findCoordinates(parkingGarage.getAddress());
            addressDao.edit(parkingGarage.getAddress());
        }
        super.edit(parkingGarage);
    }

    @Override
    public void create(ParkingGarage parkingGarage) {
        parkingGarage.setKey(parkingGarage.getKey().toUpperCase());
        geoCoding.findCoordinates(parkingGarage.getAddress());
        setAccessKey(parkingGarage);

        addressDao.create(parkingGarage.getAddress());
        super.create(parkingGarage);
    }

    public void setAccessKey(ParkingGarage parkingGarage) {
        String accessKey;
        do {
            accessKey = new RandomString().nextString();
        } while (exists(accessKey));
        parkingGarage.setAccessKey(accessKey);
    }

    protected boolean exists(String accessKey) {
        return !getDao().findByColumn(ParkingGarage.C_ACCESS_KEY,accessKey).isEmpty();
    }

    protected boolean hasAddressChanged(Address address) {
        if(address == null || address.getId() == null) {
            return false;
        }

        return !address.compareValues(addressDao.find(address.getId()));
    }
}
