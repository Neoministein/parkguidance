package com.neo.parkguidance.web.user.pages.geolocation;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.api.geomap.DistanceMatrixService;
import com.neo.parkguidance.framework.api.geomap.GeoCodingService;
import com.neo.parkguidance.framework.api.geomap.GeoMapURL;
import com.neo.parkguidance.framework.api.config.ConfigService;
import com.neo.parkguidance.framework.impl.geomap.DistanceDataObject;
import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.web.impl.utils.Utils;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The screen facade for the NearAddress and NearLocation screen
 */
@Stateless
public class GeoLocationFacade {

    public static final int RADIUS_EARTH = 6371;
    public static final int DEFAULT_GARAGES = 5;

    public static final String WANTED_GARAGES = "user.wanted-garages";

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationFacade.class);

    @Inject
    DistanceMatrixService distanceMatrix;

    @Inject
    GeoCodingService geoCoding;

    @Inject
    GeoMapURL geoMapURL;

    @Inject
    EntityDao<ParkingGarage> parkingGarageDao;

    @Inject ConfigService configService;

    public List<DistanceDataObject> callDistance(double latitude, double longitude) {
        List<ParkingGarage> parkingGarageList = findNearest(parkingGarageDao.findAll(), latitude, longitude);

        return distanceMatrix.findDistance(parkingGarageList, latitude, longitude);
    }

    public List<DistanceDataObject> callDistance(Address address) {
        try {
            geoCoding.findCoordinates(address);
            List<ParkingGarage> parkingGarageList = findNearest(parkingGarageDao.findAll(), address.getLatitude(), address.getLongitude());

            return distanceMatrix.findDistance(parkingGarageList, address);
        }catch (RuntimeException e) {
            Utils.addDetailMessage(e.getMessage());
            return Collections.emptyList();
        }
    }

    public void redirectSearch(ParkingGarage parkingGarage) {
        PrimeFaces.current().executeScript("window.open('" + geoMapURL.search(parkingGarage) + "', '_newtab')");
    }

    public List<String> autoCompleteCity(String query, List<Address> addressList) {
        String queryLowerCase = query.toLowerCase();
        Set<String> cityList = new HashSet<>();
        for (Address address : addressList) {
            cityList.add(address.getCityName());
        }

        return cityList.stream().filter(t -> t.toLowerCase().startsWith(queryLowerCase)).collect(Collectors.toList());
    }

    protected List<ParkingGarage> findNearest(List<ParkingGarage> parkingGarageList, double latitude, double longitude) {
        LOGGER.info("Looking for nearest ParkingGarage");
        Map<String,Double> map = new HashMap<>();
        int wantedGarages = configService.getInteger(WANTED_GARAGES, DEFAULT_GARAGES);
        if(parkingGarageList.size() < wantedGarages) {
            LOGGER.debug("There aren't enough ParkingGarages using all");
            return parkingGarageList;
        }
        for(ParkingGarage parkingGarage: parkingGarageList) {
            map.put(parkingGarage.getKey(),
                    getDistanceFromLatLonInKm(
                            latitude,
                            longitude,
                            parkingGarage.getAddress().getLatitude(),
                            parkingGarage.getAddress().getLongitude()));
        }

        List<Map.Entry<String,Double>> entries = sortByValue(map);

        List<ParkingGarage> selectedGarages = new ArrayList<>();
        for(int i = 0; i < wantedGarages;i++) {
            selectedGarages.add(parkingGarageDao.find(entries.get(i).getKey()));
        }

        return selectedGarages;
    }

    // https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula
    protected double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return RADIUS_EARTH * c; // Distance in km
    }

    protected double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    protected static List<Map.Entry<String, Double>> sortByValue(Map<String, Double> unsortedMap) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortedMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()));
        return list;

    }
}
