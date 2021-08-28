package com.neo.parkguidance.parkdata.impl.service;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.event.DataBaseEntityChangeEvent;
import com.neo.parkguidance.core.impl.event.ParkDataChangeEvent;
import com.neo.parkguidance.parkdata.api.service.ParkDataService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ParkDataServiceImpl implements ParkDataService, Serializable {

    @Inject
    ParkDataServiceFacade facade;

    @Inject
    Event<ParkDataChangeEvent> parkDataChangeEvent;

    private Map<String, List<Integer>> parkData;

    @PostConstruct
    public void init() {
        parkData = facade.requestParkData();
    }

    public Map<String, List<Integer>> getParkData() {
        return parkData;
    }

    public List<Integer> getParkData(String key) {
        return parkData.get(key);
    }

    public void reload() {
        init();
    }

    public void parkDataChangeListener(@Observes ParkDataChangeEvent changeEvent) {
        if (ParkDataChangeEvent.SORTED_RESPONSE.equals(changeEvent.getStatus())) {
            init();
            parkDataChangeEvent.fire(new ParkDataChangeEvent(ParkDataChangeEvent.SERVICE_RESPONSE));
        }
    }

    public void parkDataChangeListener(@Observes DataBaseEntityChangeEvent<ParkingGarage> changeEvent) {
        switch (changeEvent.getStatus()) {
        case DataBaseEntityChangeEvent.CREATE:
            parkData.put(changeEvent.getChangedObject().getKey(), facade.getEmptyParkData());
            break;
        case DataBaseEntityChangeEvent.REMOVE:
            parkData.remove(changeEvent.getChangedObject().getKey());
            break;
        case DataBaseEntityChangeEvent.EDIT:
        default:
            return;
        }
        parkDataChangeEvent.fire(new ParkDataChangeEvent(ParkDataChangeEvent.SERVICE_RESPONSE));
    }
}
