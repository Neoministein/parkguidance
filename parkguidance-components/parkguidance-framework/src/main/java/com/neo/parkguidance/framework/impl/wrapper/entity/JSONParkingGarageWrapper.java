package com.neo.parkguidance.framework.impl.wrapper.entity;

import com.neo.parkguidance.framework.api.wrapper.entity.JSONEntityWrapper;
import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import org.json.JSONException;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class JSONParkingGarageWrapper implements JSONEntityWrapper<ParkingGarage> {

    @Inject
    JSONEntityWrapper<Address> jsonAddressWrapper;

    @Override
    public JSONObject toJSON(ParkingGarage entity, boolean hideValues) {
        if (entity == null) {
            return null;
        }
        JSONObject jsonEntity = new JSONObject();
        jsonEntity.put(ParkingGarage.C_KEY, entity.getKey());
        jsonEntity.put(ParkingGarage.C_NAME, entity.getName());
        jsonEntity.put(ParkingGarage.C_SPACES, entity.getSpaces());
        jsonEntity.put(ParkingGarage.C_OCCUPIED, entity.getOccupied());
        jsonEntity.put(ParkingGarage.C_PRICE, entity.getPrice());
        jsonEntity.put(ParkingGarage.C_OPERATOR, entity.getOperator());
        jsonEntity.put(ParkingGarage.C_DESCRIPTION, entity.getDescription());
        jsonEntity.put(Address.TABLE_NAME, jsonAddressWrapper.toJSON(entity.getAddress(), hideValues));

        if (!hideValues) {
            jsonEntity.put(ParkingGarage.C_ACCESS_KEY, entity.getAccessKey());
        }
        return jsonEntity;
    }

    @Override
    public ParkingGarage toEntity(JSONObject jsonObject) {
        try {
            ParkingGarage parkingGarage = new ParkingGarage();
            parkingGarage.setKey(jsonObject.getString(ParkingGarage.C_KEY));
            parkingGarage.setName(jsonObject.getString(ParkingGarage.C_NAME));
            parkingGarage.setSpaces(jsonObject.getInt(ParkingGarage.C_SPACES));
            parkingGarage.setOccupied(jsonObject.getInt(ParkingGarage.C_OCCUPIED));
            parkingGarage.setPrice(jsonObject.getString(ParkingGarage.C_PRICE));
            parkingGarage.setOperator(jsonObject.getString(ParkingGarage.C_OPERATOR));
            parkingGarage.setDescription(jsonObject.getString(ParkingGarage.C_DESCRIPTION));
            parkingGarage.setAddress(jsonAddressWrapper.toEntity(jsonObject.getJSONObject(Address.TABLE_NAME)));
            return parkingGarage;
        } catch (JSONException ex) {
            return null;
        }

    }
}
