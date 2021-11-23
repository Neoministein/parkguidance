package com.neo.parkguidance.microservices.dao;

import com.neo.parkguidance.framework.api.dao.EntityDao;
import com.neo.parkguidance.framework.entity.DataBaseEntity;
import org.hibernate.Criteria;

/**
 * This interface defines the abstract implementation of the {@link EntityDao}
 */
public interface EntityDaoAbstraction<T extends DataBaseEntity> extends EntityDao<T> {

    /**
     * The criteria which a query should be sorted against
     *
     * @param criteria the criteria that should be added too
     * @param object the example object
     */
    void addSubCriteria(Criteria criteria, T object);
}
