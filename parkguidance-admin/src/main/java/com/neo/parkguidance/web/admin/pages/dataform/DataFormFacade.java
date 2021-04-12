package com.neo.parkguidance.web.admin.pages.dataform;

import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;

import javax.ejb.Stateless;
import javax.inject.Inject;

import java.util.List;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class DataFormFacade {

    @Inject
    AbstractEntityDao<ParkingData> parkingDataDao;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    public ParkingData findGarageById(Integer id) {
        return parkingDataDao.find(Long.valueOf(id));
    }

    public boolean remove(ParkingData parkingData) {
        if (has(parkingData) && has(parkingData.getId())) {
            parkingDataDao.remove(parkingData);
            return true;
        } else {
            return false;
        }
    }

    public void edit(ParkingData parkingData) {
        parkingDataDao.edit(parkingData);
    }

    public void create(ParkingData parkingData) {
        parkingDataDao.create(parkingData);
    }

    public List<ParkingGarage> getParkingGarageList() {
        return parkingGarageDao.findAll();
    }
}
