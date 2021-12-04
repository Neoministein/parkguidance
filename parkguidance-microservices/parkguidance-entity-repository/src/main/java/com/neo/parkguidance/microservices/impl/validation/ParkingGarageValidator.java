package com.neo.parkguidance.microservices.impl.validation;

import com.neo.parkguidance.framework.entity.ParkingGarage;
import com.neo.parkguidance.framework.impl.utils.RandomString;
import com.neo.parkguidance.framework.impl.validation.EntityValueValidationException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 * Validates changes done to the {@link ParkingGarage} entity
 */
@RequestScoped
public class ParkingGarageValidator extends AbstractDatabaseEntityValidation<ParkingGarage> {

    @Inject
    AddressValidator addressValidation;

    @Override
    public void validatePrimaryKey(Object primaryKey) throws EntityValidationException {
        super.validatePrimaryKey(primaryKey);
        checkInvalidCharsInPrimaryKey((String) primaryKey);
    }

    @Override
    public boolean hasNothingChanged(ParkingGarage entity) {
        ParkingGarage originalObject = super.returnOriginalObject(entity);
        if (originalObject == null) {
            return false;
        }

        if(!originalObject.getName().equals(entity.getName())) {
            return false;
        }

        if(!originalObject.getSpaces().equals(entity.getSpaces())) {
            return false;
        }
        if(!originalObject.getOccupied().equals(entity.getOccupied())) {
            return false;
        }
        return originalObject.getAccessKey().equals(entity.getAccessKey());
    }

    public void newUniqueAccessKey(ParkingGarage parkingGarage) {
        String accessKey;
        do {
            accessKey = new RandomString().nextString();
        } while (valueExists(ParkingGarage.C_ACCESS_KEY ,accessKey));
        parkingGarage.setAccessKey(accessKey);
    }

    protected void checkInvalidCharsInPrimaryKey(String primaryKey) throws EntityValidationException {
        if (primaryKey.replaceAll("[A-Z\\d\\-_]","").length() > 0) {
            throw new EntityValueValidationException("key" ,"Unsupported Character, Valid Characters include A-Z, 0-9, '_' and '-'.");
        }
    }
}
