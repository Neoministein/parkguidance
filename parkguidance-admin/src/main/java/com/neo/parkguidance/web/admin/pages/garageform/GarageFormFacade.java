package com.neo.parkguidance.web.admin.pages.garageform;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.RandomString;
import com.neo.parkguidance.core.impl.dao.AddressEntityManager;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;

import javax.ejb.Stateless;
import javax.inject.Inject;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class GarageFormFacade {

    @Inject
    private ParkingGarageEntityService garageService;

    @Inject
    private AddressEntityManager addressManager;

    public ParkingGarage findGarageById(Integer id) {
        return garageService.findById(id);
    }

    public boolean remove(ParkingGarage parkingGarage) {
        if (has(parkingGarage) && has(parkingGarage.getId())) {
            garageService.remove(parkingGarage);
            return true;
        } else {
            return false;
        }
    }

    public void edit(ParkingGarage parkingGarage) {
        addressManager.edit(parkingGarage.getAddress());
        garageService.edit(parkingGarage);
    }

    public void create(ParkingGarage parkingGarage) {
        checkAccessKey(parkingGarage);

        addressManager.create(parkingGarage.getAddress());
        garageService.create(parkingGarage);
    }

    protected void checkAccessKey(ParkingGarage parkingGarage) {
        if (parkingGarage.getAccessKey() == null || parkingGarage.getAccessKey().isEmpty()) {
            String accesskey;
            do {
                accesskey = new RandomString().nextString();
            }while (exists(accesskey));
            parkingGarage.setAccessKey(accesskey);
        }
    }

    protected boolean exists(String accessKey) {
        return !garageService.findByColumn(ParkingGarage.C_ACCESS_KEY,accessKey).isEmpty();
    }
}
