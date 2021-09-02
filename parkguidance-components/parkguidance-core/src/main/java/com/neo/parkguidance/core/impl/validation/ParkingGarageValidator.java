package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.ParkingGarage;
import com.neo.parkguidance.core.impl.utils.RandomString;
import com.neo.parkguidance.core.impl.utils.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Objects;

/**
 * Validates changes done to the {@link ParkingGarage} entity
 */
@Stateless
public class ParkingGarageValidator extends AbstractDatabaseEntityValidation<ParkingGarage> {

    @Inject
    AddressValidator addressValidation;

    @Override
    public void validatePrimaryKey(Object primaryKey) throws EntityValidationException {
        super.validatePrimaryKey(primaryKey);

        String key = (String) primaryKey;
        if (StringUtils.isEmpty(key)) {
            throw new EntityValidationException("Key cannot be empty");
        }
        checkInvalidCharsInPrimaryKey(key);
    }

    @Override
    public boolean compareValues(ParkingGarage entity) {
        ParkingGarage originalObject = super.returnOriginalObject(entity);
        if (originalObject == null) {
            return false;
        }

        if(!originalObject.getName().equals(entity.getName())) {
            return false;
        }

        if(originalObject.getSpaces().equals(entity.getSpaces())) {
            return false;
        }
        if(originalObject.getOccupied().equals(entity.getOccupied())) {
            return false;
        }
        if(!originalObject.getAccessKey().equals(entity.getAccessKey())) {
            return false;
        }
        if(!addressValidation.compareValues(entity.getAddress())) {
            return false;
        }
        if(!Objects.equals(originalObject.getPrice(), entity.getPrice())) {
            return false;
        }
        if(!Objects.equals(originalObject.getOperator(), entity.getOperator())) {
            return false;
        }
        return Objects.equals(originalObject.getDescription(), entity.getDescription());
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
