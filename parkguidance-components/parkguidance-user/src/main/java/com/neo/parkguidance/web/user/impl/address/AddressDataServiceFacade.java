package com.neo.parkguidance.web.user.impl.address;

import com.neo.parkguidance.core.entity.Address;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * This class handles the logic for retrieving data which is used for a lot
 */
@Stateless
public class AddressDataServiceFacade {

    @Inject
    AbstractEntityDao<Address> addressDao;


    public List<Address> getAddress() {
        return addressDao.findAll();
    }
}
