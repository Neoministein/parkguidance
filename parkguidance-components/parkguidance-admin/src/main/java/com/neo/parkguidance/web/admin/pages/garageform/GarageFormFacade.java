package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.google.api.maps.GeoCoding;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.RandomString;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import static com.github.adminfaces.template.util.Assert.has;

/**
 * The screen facade for the GarageForm screen
 */
@Stateless
public class GarageFormFacade {

    private static final Logger LOGGER = LogManager.getLogger(GarageFormFacade.class);

    @Inject
    AbstractEntityDao<ParkingGarage> garageDao;

    @Inject
    AbstractEntityDao<Address> addressDao;

    @Inject
    GeoCoding geoCoding;

    public ParkingGarage findGarageById(String key) {
        return garageDao.find(key);
    }

    public boolean remove(ParkingGarage parkingGarage) {
        if (has(parkingGarage) && has(parkingGarage.getKey())) {
            garageDao.remove(parkingGarage);
            LOGGER.info("Deleting ParkingGarage [{}]", parkingGarage.getKey());
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
        LOGGER.info("Updated ParkingGarage [{}]", parkingGarage.getKey());
    }

    public void create(ParkingGarage parkingGarage) {
        parkingGarage.setKey(parkingGarage.getKey().toUpperCase());
        geoCoding.findCoordinates(parkingGarage.getAddress());
        setAccessKey(parkingGarage);

        addressDao.create(parkingGarage.getAddress());
        garageDao.create(parkingGarage);
        LOGGER.info("Created ParkingGarage [{}]", parkingGarage.getKey());
    }

    public void setAccessKey(ParkingGarage parkingGarage) {
        String accessKey;
        do {
            accessKey = new RandomString().nextString();
        } while (exists(accessKey));
        parkingGarage.setAccessKey(accessKey);
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
