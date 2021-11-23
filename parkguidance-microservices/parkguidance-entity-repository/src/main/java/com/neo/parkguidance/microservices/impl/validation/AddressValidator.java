package com.neo.parkguidance.microservices.impl.validation;

import com.neo.parkguidance.framework.entity.Address;

import javax.enterprise.context.RequestScoped;
import java.util.Objects;

/**
 * Validates changes done to the {@link Address} entity
 */
@RequestScoped
public class AddressValidator extends AbstractDatabaseEntityValidation<Address> {

    @Override
    public boolean hasNothingChanged(Address entity) {
        Address originalObject = super.returnOriginalObject(entity);
        if (originalObject == null) {
            return false;
        }

        if (!Objects.equals(originalObject.getCityName(),entity.getCityName())) {
            return false;
        }
        if (!Objects.equals(originalObject.getPlz(),entity.getPlz())) {
            return false;
        }
        if (!Objects.equals(originalObject.getStreet(), entity.getStreet())) {
            return false;
        }
        if (!Objects.equals(originalObject.getNumber(), entity.getNumber())) {
            return false;
        }
        if (!Objects.equals(originalObject.getLongitude(),entity.getLongitude())) {
            return false;
        }
        return Objects.equals(originalObject.getLatitude(), entity.getLatitude());
    }
}
