package com.neo.parkguidance.web.admin.pages.parkdata;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.parkdata.api.service.ParkDataService;
import com.neo.parkguidance.parkdata.impl.data.ParkDataObject;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.omnifaces.util.Messages;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * The screen facade for the ParkData screen
 */
@Stateless
public class ParkDataFacade {

    @Inject
    ParkDataService parkDataService;

    @Inject
    EntityDao<ParkingGarage> entityDao;

    public void sortParkingData() {
        try {
            parkDataService.sortParkData();
        } catch (Exception ex) {
            Messages.addError(null, "Something went wrong while sorting " + ex.getMessage());
        }
    }

    public void deleteOld() {
        try {
            parkDataService.deleteRawSortedData();
            Utils.addDetailMessage("Raw Parking Data has been sorted");
        } catch (RuntimeException ex) {
            Messages.addError(null, "Something went wrong: " , ex.getMessage());
        }
    }

    public List<ParkDataObject> loadParkDataStatistics() {
        List<ParkDataObject> parkDataObjectList = new ArrayList<>();

        for (ParkingGarage parkingGarage: entityDao.findAll()) {
            parkDataObjectList.add(parkDataService.getMetaData(parkingGarage));
        }
        return parkDataObjectList;
    }

}
