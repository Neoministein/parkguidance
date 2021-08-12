package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.utils.RandomString;
import com.neo.parkguidance.core.impl.utils.StringUtils;


/**
 * Validates changes done to the {@link ParkingGarage} entity
 */
public class ParkingGarageValidator extends AbstractDatabaseEntityValidation<ParkingGarage> {

    @Override
    public void validatePrimaryKey(Object primaryKey) {
        super.validatePrimaryKey(primaryKey);

        String key = (String) primaryKey;
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key cannot be empty");
        }
        checkInvalidCharsInPrimaryKey(key);
    }

    public void invalidateAccessKey(ParkingGarage parkingGarage) {
        String accessKey;
        do {
            accessKey = new RandomString().nextString();
        } while (exists(accessKey));
        parkingGarage.setAccessKey(accessKey);
    }

    protected boolean exists(String accessKey) {
        return dao.findOneByColumn(ParkingGarage.C_ACCESS_KEY,accessKey) != null;
    }


    protected void checkInvalidCharsInPrimaryKey(String primaryKey) {
        if (primaryKey.replaceAll("[A-Z\\d\\-_]","").length() > 0) {
            throw new IllegalArgumentException("Unsupported Character, Valid Characters include A-Z, 0-9, '_' and '-'.");
        }
    }
}
