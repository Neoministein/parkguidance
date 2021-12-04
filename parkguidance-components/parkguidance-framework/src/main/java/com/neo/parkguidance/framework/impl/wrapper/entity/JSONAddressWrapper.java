package com.neo.parkguidance.framework.impl.wrapper.entity;

import com.neo.parkguidance.framework.api.wrapper.entity.JSONEntityWrapper;
import com.neo.parkguidance.framework.entity.Address;
import com.neo.parkguidance.framework.entity.DataBaseEntity;
import com.neo.parkguidance.framework.impl.validation.EntityValidationException;
import org.json.JSONException;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import java.util.Collection;

@RequestScoped
public class JSONAddressWrapper extends AbstractJSONWrapper implements JSONEntityWrapper<Address> {

    @Override
    public JSONObject toJSON(Address entity, Collection<String> callerPermissions) {
        if (entity == null) {
            return null;
        }
        JSONObject jsonEntity = new JSONObject();
        jsonEntity.put(Address.C_ID, entity.getId());
        jsonEntity.put(Address.C_CITY_NAME, entity.getCityName());
        jsonEntity.put(Address.C_ZIP_CODE, entity.getZipCode());
        jsonEntity.put(Address.C_STREET, entity.getStreet());
        jsonEntity.put(Address.C_HOUSE_NUMBER, entity.getHouseNumber());
        jsonEntity.put(Address.C_LATITUDE, entity.getLatitude());
        jsonEntity.put(Address.C_LONGITUDE, entity.getLongitude());
        return jsonEntity;
    }

    @Override
    public Address toValidEntity(JSONObject jsonObject, Collection<String> callerPermissions) {
        try {
            Address address = new Address();
            address.setId(optLong(jsonObject, DataBaseEntity.C_ID));
            address.setCountry(jsonObject.getString(Address.C_COUNTRY));
            address.setCityName(jsonObject.getString(Address.C_CITY_NAME));
            address.setZipCode(jsonObject.getInt(Address.C_ZIP_CODE));
            address.setStreet(jsonObject.getString(Address.C_STREET));
            address.setHouseNumber(optInteger(jsonObject, Address.C_HOUSE_NUMBER));
            address.setLatitude(optDouble(jsonObject, Address.C_LATITUDE));
            address.setLongitude(optDouble(jsonObject, Address.C_LONGITUDE));
            return address;
        } catch (JSONException ex) {
            throw new EntityValidationException("Invalid address entity missing attributes");
        }
    }

    @Override
    public Address toOptEntity(JSONObject jsonObject) {
        Address address = new Address();
        address.setId(optLong(jsonObject, Address.C_ID));
        address.setCityName(optString(jsonObject, Address.C_CITY_NAME));
        address.setZipCode(optInteger(jsonObject, Address.C_ZIP_CODE));
        address.setStreet(optString(jsonObject, Address.C_STREET));
        address.setHouseNumber(optInteger(jsonObject, Address.C_HOUSE_NUMBER));
        address.setLatitude(optDouble(jsonObject, Address.C_LATITUDE));
        address.setLongitude(optDouble(jsonObject, Address.C_LONGITUDE));
        return address;
    }
}
