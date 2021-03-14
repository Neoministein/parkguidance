package com.neo.parkguidance.web.pages.admin.dataform;

import com.neo.parkguidance.entity.ParkingData;
import com.neo.parkguidance.entity.ParkingGarage;
import com.neo.parkguidance.impl.RandomString;
import com.neo.parkguidance.web.infra.entity.AddressEntityManager;
import com.neo.parkguidance.web.infra.entity.ParkingDataEntityService;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityManager;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.List;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class DataFormFacade {

    @Inject
    private ParkingDataEntityService dataService;

    @Inject
    private ParkingGarageEntityService parkingGarageManager;

    public ParkingData findGarageById(Integer id) {
        return dataService.findById(id);
    }

    public boolean remove(ParkingData parkingData) {
        if (has(parkingData) && has(parkingData.getId())) {
            dataService.remove(parkingData);
            return true;
        } else {
            return false;
        }
    }

    public void edit(ParkingData parkingData) {
        dataService.edit(parkingData);
    }

    public void create(ParkingData parkingData) {
        dataService.create(parkingData);
    }

    public List<ParkingGarage> getParkingGarageList() {
        return parkingGarageManager.findAll();
    }
}