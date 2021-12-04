package com.neo.parkguidance.framework.impl.wrapper.entity;

import com.neo.parkguidance.framework.api.wrapper.entity.JSONEntityWrapper;
import com.neo.parkguidance.framework.entity.DataBaseEntity;
import com.neo.parkguidance.framework.entity.GarageInfo;
import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.framework.impl.validation.EntityValidationException;
import org.json.JSONException;
import org.json.JSONObject;

import javax.enterprise.context.RequestScoped;
import java.util.Collection;

@RequestScoped
public class JSONGarageInfoWrapper extends AbstractJSONWrapper implements JSONEntityWrapper<GarageInfo> {

    @Override
    public JSONObject toJSON(GarageInfo entity, Collection<String> callerPermissions) {
        JSONObject jsonEntity = new JSONObject();
        jsonEntity.put(DataBaseEntity.C_ID, entity.getId());
        jsonEntity.put(GarageInfo.C_KEY, entity.getKey());
        jsonEntity.put(GarageInfo.C_VALUE, entity.getValue());
        jsonEntity.put(ParkingGarage.TABLE_NAME, entity.getParkingGarage().getPrimaryKey());
        return jsonEntity;
    }

    @Override
    public GarageInfo toValidEntity(JSONObject jsonObject, Collection<String> callerPermissions) {
        try {
            GarageInfo garageInfo = new GarageInfo();
            garageInfo.setId(optString(jsonObject, DataBaseEntity.C_ID));
            garageInfo.setKey(jsonObject.getString(GarageInfo.C_KEY));
            garageInfo.setValue(jsonObject.getString(GarageInfo.C_VALUE));
            return garageInfo;
        } catch (JSONException | ClassCastException ex) {
            throw new EntityValidationException("Invalid GarageInfo entity missing attributes");
        }
    }

    @Override
    public GarageInfo toOptEntity(JSONObject jsonObject) {
        GarageInfo garageInfo = new GarageInfo();
        garageInfo.setId(optString(jsonObject, DataBaseEntity.C_ID));
        garageInfo.setKey(optString(jsonObject, GarageInfo.C_KEY));
        garageInfo.setValue(optString(jsonObject, GarageInfo.C_VALUE));
        return garageInfo;
    }
}
