package com.neo.parkguidance.web.admin.pages.sheetfrom;

import com.neo.parkguidance.core.entity.DataSheet;
import com.neo.parkguidance.core.entity.ParkingData;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.web.infra.entity.DataSheetEntityService;
import com.neo.parkguidance.web.infra.entity.ParkingGarageEntityService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

import static com.github.adminfaces.template.util.Assert.has;

@Stateless
public class DataSheetFormFacade {

    @Inject private DataSheetEntityService dataSheetService;

    @Inject private ParkingGarageEntityService parkingGarageManager;

    public DataSheet findById(Integer id) {
        return dataSheetService.findById(id);
    }

    public boolean remove(DataSheet parkingData) {
        if (has(parkingData) && has(parkingData.getId())) {
            dataSheetService.remove(parkingData);
            return true;
        } else {
            return false;
        }
    }

    public void edit(DataSheet parkingData) {
        dataSheetService.edit(parkingData);
    }

    public void create(DataSheet parkingData) {
        dataSheetService.create(parkingData);
    }

    public List<ParkingGarage> getParkingGarageList() {
        return parkingGarageManager.findAll();
    }
}
