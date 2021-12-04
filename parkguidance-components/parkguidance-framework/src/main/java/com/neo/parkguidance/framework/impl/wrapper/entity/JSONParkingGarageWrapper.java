package com.neo.parkguidance.framework.impl.wrapper.entity;

import com.neo.parkguidance.framework.api.wrapper.entity.JSONEntityWrapper;
import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.entity.GarageInfo;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.framework.impl.validation.EntityValidationException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Collection;

@RequestScoped
public class JSONParkingGarageWrapper extends AbstractJSONWrapper implements JSONEntityWrapper<ParkingGarage> {

    @Inject
    JSONEntityWrapper<Address> jsonAddressWrapper;

    @Inject
    JSONEntityWrapper<GarageInfo> garageInfoJSONWrapper;

    @Override
    public JSONObject toJSON(ParkingGarage entity, Collection<String> callerPermissions) {
        if (entity == null) {
            return null;
        }
        JSONObject jsonEntity = new JSONObject();
        jsonEntity.put(ParkingGarage.C_KEY, entity.getKey());
        jsonEntity.put(ParkingGarage.C_NAME, entity.getName());
        jsonEntity.put(ParkingGarage.C_SPACES, entity.getSpaces());
        jsonEntity.put(ParkingGarage.C_OCCUPIED, entity.getOccupied());
        jsonEntity.put(Address.TABLE_NAME, jsonAddressWrapper.toJSON(entity.getAddress(), callerPermissions));

        if (callerPermissions.contains("ADMIN")) {
            jsonEntity.put(ParkingGarage.C_ACCESS_KEY, entity.getAccessKey());
        }
        return jsonEntity;
    }

    @Override
    public ParkingGarage toValidEntity(JSONObject jsonObject, Collection<String> callerPermissions) {
        try {
            ParkingGarage parkingGarage = new ParkingGarage();
            parkingGarage.setKey(jsonObject.getString(ParkingGarage.C_KEY));
            parkingGarage.setName(jsonObject.getString(ParkingGarage.C_NAME));
            parkingGarage.setSpaces(jsonObject.getInt(ParkingGarage.C_SPACES));
            parkingGarage.setOccupied(jsonObject.getInt(ParkingGarage.C_OCCUPIED));
            parkingGarage.setAddress(jsonAddressWrapper.toValidEntity(
                    jsonObject.getJSONObject(Address.TABLE_NAME),
                    callerPermissions));
            JSONArray garageInfo = jsonObject.getJSONArray(GarageInfo.TABLE_NAME);
            for (int i = 0; i < garageInfo.length();i++) {
                parkingGarage.getGarageInfo().add(
                        garageInfoJSONWrapper.toValidEntity(garageInfo.getJSONObject(i), callerPermissions));
            }

            return parkingGarage;
        } catch (JSONException ex) {
           throw new EntityValidationException("Invalid parking garage entity missing attribute");
        }

    }

    @Override
    public ParkingGarage toOptEntity(JSONObject jsonObject) {
        ParkingGarage parkingGarage = new ParkingGarage();
        parkingGarage.setKey(optString(jsonObject, ParkingGarage.C_KEY));
        parkingGarage.setName(optString(jsonObject, ParkingGarage.C_KEY));
        parkingGarage.setSpaces(optInteger(jsonObject, ParkingGarage.C_KEY));
        parkingGarage.setOccupied(optInteger(jsonObject, ParkingGarage.C_KEY));
        if (jsonObject.has(Address.TABLE_NAME)) {
            parkingGarage.setAddress(jsonAddressWrapper.toOptEntity(jsonObject.optJSONObject(Address.TABLE_NAME)));
        }
        return parkingGarage;
    }
}
