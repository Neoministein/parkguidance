package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.api.validation.AbstractDatabaseEntityValidation;
import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.utils.RandomString;
import com.neo.parkguidance.core.impl.utils.StringUtils;

import javax.ejb.Stateless;

/**
 * Validates changes done to the {@link ParkingGarage} entity
 */
@Stateless
public class ParkingGarageValidator extends AbstractDatabaseEntityValidation<ParkingGarage> {

    @Override
    public void validatePrimaryKey(Object primaryKey) throws EntityValidationException {
        super.validatePrimaryKey(primaryKey);

        String key = (String) primaryKey;
        if (StringUtils.isEmpty(key)) {
            throw new EntityValidationException("Key cannot be empty");
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
        return getDao().findOneByColumn(ParkingGarage.C_ACCESS_KEY,accessKey) != null;
    }


    protected void checkInvalidCharsInPrimaryKey(String primaryKey) throws EntityValidationException {
        if (primaryKey.replaceAll("[a-zA-Z\\d\\-_]","").length() > 0) {
            throw new EntityValidationException("Unsupported Character, Valid Characters include A-Z, 0-9, '_' and '-'.");
        }
    }
}
