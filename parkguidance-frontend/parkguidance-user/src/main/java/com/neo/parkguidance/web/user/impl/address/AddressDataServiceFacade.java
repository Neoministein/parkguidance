package com.neo.parkguidance.web.user.impl.address;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.Address;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

/**
 * This class handles the logic for retrieving data which is used for a lot
 */
@Stateless
public class AddressDataServiceFacade {

    @Inject
    EntityDao<Address> addressDao;


    public List<Address> getAddress() {
        return addressDao.findAll();
    }
}
