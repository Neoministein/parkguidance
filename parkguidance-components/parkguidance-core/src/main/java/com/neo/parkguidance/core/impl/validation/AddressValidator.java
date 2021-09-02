package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.Address;

import javax.ejb.Stateless;
import java.util.Objects;

/**
 * Validates changes done to the {@link Address} entity
 */
@Stateless
public class AddressValidator extends AbstractDatabaseEntityValidation<Address> {

    public boolean hasAddressChanged(Address address) {
        if(address == null || address.getId() == null) {
            return false;
        }

        return !compareValues(address, getDao().find(address.getId()));
    }

    public boolean compareValues(Address currentAddress, Address databaseAddress) {
        if (!Objects.equals(databaseAddress.getCityName(),currentAddress.getCityName())) {
            return false;
        }
        if (!Objects.equals(databaseAddress.getPlz(),currentAddress.getPlz())) {
            return false;
        }
        if (!Objects.equals(databaseAddress.getStreet(), currentAddress.getStreet())) {
            return false;
        }
        if (!Objects.equals(databaseAddress.getNumber(), currentAddress.getNumber())) {
            return false;
        }
        if (!Objects.equals(databaseAddress.getLongitude(),currentAddress.getLongitude())) {
            return false;
        }
        return Objects.equals(databaseAddress.getLatitude(), currentAddress.getLatitude());
    }
}
