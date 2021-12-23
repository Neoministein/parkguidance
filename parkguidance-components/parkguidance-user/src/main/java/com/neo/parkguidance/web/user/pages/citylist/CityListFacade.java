package com.neo.parkguidance.web.user.pages.citylist;

import com.neo.parkguidance.core.api.dao.EntityDao;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.utils.StringUtils;
import org.omnifaces.util.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

/**
 * The screen facade for the ParkList screen
 */
@Stateless
public class CityListFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityListFacade.class);


    private static final int TIME_BETWEEN_UPDATES = 5 * 1000; // 5 sec

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    public void initDataModel(CityListModel model) {
        if(model.getLastUpdate().getTime() + TIME_BETWEEN_UPDATES < System.currentTimeMillis()) {
            model.setLastUpdate(new Date());

            Map<String, List<ParkingGarage>> garageData = getGarageData();
            model.setGarageData(garageData);
            model.setCities(getCities(garageData));
        }
    }

    public String validateURLInput(String urlInput, Collection<String> cities) {
        try {
            String cityName = StringUtils.decodeUrl(urlInput);
            if (cities.contains(cityName)) {
                return cityName;
            }
        } catch (Exception ex) {
            LOGGER.error("The provided city name isn't valid {}", urlInput);
        }
        Messages.addError(null, "Ungültige Stadt");
        return "";
    }

    public Map<String, List<ParkingGarage>> getGarageData(){
        Map<String, List<ParkingGarage>> data = new HashMap<>();

        for (ParkingGarage parkingGarage: parkingGarageDao.findAll(ParkingGarage.C_NAME, true)) {
            String cityId = parkingGarage.getAddress().getCityName();

            List<ParkingGarage> garagesInCity = data.computeIfAbsent(cityId, k -> new ArrayList<>());
            garagesInCity.add(parkingGarage);
        }
        return data;
    }

    public List<String> getCities(Map<String, List<ParkingGarage>> garageData) {
        List<String> cities = new ArrayList<>();
        for (String city: garageData.keySet()) {
            cities.add(StringUtils.encodeUrl(city));
        }
        return cities;
    }
}
