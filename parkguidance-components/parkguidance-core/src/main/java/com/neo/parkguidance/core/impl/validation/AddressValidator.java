package com.neo.parkguidance.core.impl.validation;

import com.neo.parkguidance.core.entity.Address;

import javax.ejb.Stateless;

/**
 * Validates changes done to the {@link Address} entity
 */
@Stateless
public class AddressValidator extends AbstractDatabaseEntityValidation<Address> {

    public boolean hasAddressChanged(Address address) {
        if(address == null || address.getId() == null) {
            return false;
        }

        return !address.compareValues(dao.find(address.getId()));
    }
}
