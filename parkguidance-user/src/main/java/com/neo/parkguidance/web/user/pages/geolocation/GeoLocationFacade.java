package com.neo.parkguidance.web.user.pages.geolocation;

import com.neo.parkguidance.core.api.external.google.maps.DistanceDataObject;
import com.neo.parkguidance.core.api.external.google.maps.DistanceMatrix;
import com.neo.parkguidance.core.api.external.google.maps.GeoCoding;
import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.utils.Utils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class GeoLocationFacade {

    public static final int RADIUS_EARTH = 6371;
    public static final int WANTED_GARAGES = 5;

    @Inject
    DistanceMatrix distanceMatrix;

    @Inject
    GeoCoding geoCoding;

    @Inject
    AbstractEntityDao<ParkingGarage> entityDao;

    public List<DistanceDataObject> callDistance(double latitude, double longitude) {
        List<ParkingGarage> parkingGarageList = findNearest(entityDao.findAll(), latitude,longitude);

        return distanceMatrix.findDistance(parkingGarageList, latitude,longitude);
    }

    public List<DistanceDataObject> callDistance(Address address) {
        try {
            geoCoding.findCoordinates(address);
            List<ParkingGarage> parkingGarageList = findNearest(entityDao.findAll(), address.getLatitude(), address.getLongitude());

            return distanceMatrix.findDistance(parkingGarageList, address);
        }catch (RuntimeException e) {
            Utils.addDetailMessage("Die Addresse konnte nicht gefunden werden.");
            return Collections.emptyList();
        }
    }

    protected List<ParkingGarage> findNearest(List<ParkingGarage> parkingGarageList, double latitude, double longitude) {
        Map<Long,Double> map = new HashMap<>();
        if(parkingGarageList.size() < WANTED_GARAGES) {
            return parkingGarageList;
        }
        for(ParkingGarage parkingGarage: parkingGarageList) {
            map.put(parkingGarage.getId(),
                    getDistanceFromLatLonInKm(latitude,
                            longitude,
                            parkingGarage.getAddress().getLatitude(),
                            parkingGarage.getAddress().getLongitude()));
        }

        List<Map.Entry<Long,Double>> entries = sortByValue(map);

        List<ParkingGarage> selectedGarages = new ArrayList<>();
        for(int i = 0; i < WANTED_GARAGES;i++) {
            selectedGarages.add(entityDao.find(entries.get(i).getKey()));
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

    protected static List<Map.Entry<Long, Double>> sortByValue(Map<Long, Double> unsortedMap) {
        List<Map.Entry<Long, Double>> list = new LinkedList<>(unsortedMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()));
        return list;

    }
}
