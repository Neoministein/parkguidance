package com.neo.parkguidance.web.admin.pages.sheetform;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class DataSheetFormFacade {

    @Inject AbstractEntityDao<DataSheet> dataSheetDao;

    @Inject
    AbstractEntityDao<ParkingGarage> parkingGarageDao;

    public DataSheet findById(Integer id) {
        return dataSheetDao.find(Long.valueOf(id));
    }

    public boolean remove(DataSheet parkingData) {
        if (has(parkingData) && has(parkingData.getId())) {
            dataSheetDao.remove(parkingData);
            return true;
        } else {
            return false;
        }
    }

    public void edit(DataSheet parkingData) {
        dataSheetDao.edit(parkingData);
    }

    public void create(DataSheet parkingData) {
        dataSheetDao.create(parkingData);
    }

    public List<ParkingGarage> getParkingGarageList() {
        return parkingGarageDao.findAll();
    }
}
