package com.neo.parkguidance.framework.impl.wrapper.entity;

import com.neo.parkguidance.framework.api.wrapper.entity.JSONEntityWrapper;
import com.neo.parkguidance.framework.entity.Address;
import org.json.JSONException;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class JSONAddressWrapper implements JSONEntityWrapper<Address> {

    @Override
    public JSONObject toJSON(Address entity, boolean hideValues) {
        if (entity == null) {
            return null;
        }
        JSONObject jsonEntity = new JSONObject();
        jsonEntity.put(Address.C_ID, entity.getId());
        jsonEntity.put(Address.C_CITY_NAME, entity.getCityName());
        jsonEntity.put(Address.C_PLZ, entity.getPlz());
        jsonEntity.put(Address.C_STREET, entity.getStreet());
        jsonEntity.put(Address.C_NUMBER, entity.getNumber());
        jsonEntity.put(Address.C_LATITUDE, entity.getLatitude());
        jsonEntity.put(Address.C_LONGITUDE, entity.getLongitude());
        return jsonEntity;
    }

    @Override
    public Address toEntity(JSONObject jsonObject) {
        try {
            Address address = new Address();
            address.setId(jsonObject.getLong(Address.C_ID));
            address.setCityName(jsonObject.getString(Address.C_CITY_NAME));
            address.setPlz(jsonObject.getInt(Address.C_PLZ));
            address.setStreet(jsonObject.getString(Address.C_STREET));
            address.setNumber(jsonObject.getInt(Address.C_NUMBER));
            address.setLatitude(jsonObject.getDouble(Address.C_LATITUDE));
            address.setLongitude(jsonObject.getDouble(Address.C_LONGITUDE));
            return address;
        } catch (JSONException ex) {
            return null;
        }
    }
}
