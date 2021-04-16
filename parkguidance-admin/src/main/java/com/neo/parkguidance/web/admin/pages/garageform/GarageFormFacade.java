package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.api.external.google.maps.GeoCoding;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.RandomString;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;

import javax.ejb.Stateless;
import javax.inject.Inject;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class GarageFormFacade {

    @Inject
    AbstractEntityDao<ParkingGarage> garageDao;

    @Inject
    AbstractEntityDao<Address> addressDao;

    @Inject
    GeoCoding geoCoding;

    public ParkingGarage findGarageById(Integer id) {
        return garageDao.find(Long.valueOf(id));
    }

    public boolean remove(ParkingGarage parkingGarage) {
        if (has(parkingGarage) && has(parkingGarage.getId())) {
            garageDao.remove(parkingGarage);
            return true;
        } else {
            return false;
        }
    }

    public void edit(ParkingGarage parkingGarage) {
        if(hasAddressChanged(parkingGarage.getAddress())) {
            geoCoding.findCoordinates(parkingGarage.getAddress());
            addressDao.edit(parkingGarage.getAddress());
        }
        garageDao.edit(parkingGarage);
    }

    public void create(ParkingGarage parkingGarage) {
        geoCoding.findCoordinates(parkingGarage.getAddress());
        checkAccessKey(parkingGarage);

        addressDao.create(parkingGarage.getAddress());
        garageDao.create(parkingGarage);
    }

    protected void checkAccessKey(ParkingGarage parkingGarage) {
        if (parkingGarage.getAccessKey() == null || parkingGarage.getAccessKey().isEmpty()) {
            String accessKey;
            do {
                accessKey = new RandomString().nextString();
            }while (exists(accessKey));
            parkingGarage.setAccessKey(accessKey);
        }
    }

    protected boolean exists(String accessKey) {
        return !garageDao.findByColumn(ParkingGarage.C_ACCESS_KEY,accessKey).isEmpty();
    }

    protected boolean hasAddressChanged(Address address) {
        if(address == null || address.getId() == null) {
            return false;
        }

        return !address.compareValues(addressDao.find(address.getId()));
    }
}
