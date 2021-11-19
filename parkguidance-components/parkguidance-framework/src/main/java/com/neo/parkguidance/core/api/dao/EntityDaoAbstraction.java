package com.neo.parkguidance.core.api.dao;

import com.neo.parkguidance.core.entity.DataBaseEntity;
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
