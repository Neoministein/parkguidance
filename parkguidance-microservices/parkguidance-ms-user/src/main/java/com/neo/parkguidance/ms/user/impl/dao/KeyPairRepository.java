package com.neo.parkguidance.ms.user.impl.dao;

import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.impl.entity.KeyPair;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RequestScoped
public class KeyPairRepository extends AbstractEntityDao<KeyPair> implements EntityDao<KeyPair> {

    @PersistenceContext(unitName = "parkguidancePersistence")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public KeyPairRepository() {
        super(KeyPair.class);
    }
}
