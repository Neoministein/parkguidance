package com.neo.parkguidance.core.impl.dao;

import com.neo.parkguidance.core.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.core.entity.UserToken;
import com.neo.parkguidance.core.impl.validation.UserTokenValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserTokenEntityManager extends AbstractEntityDao<UserToken> implements
        EntityDaoAbstraction<UserToken> {

    @Inject
    UserTokenValidator userTokenValidator;

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserTokenEntityManager() {
        super(UserToken.class);
    }

    @Override
    public void create(UserToken userToken) {
        userTokenValidator.newUniqueKey(userToken);
        super.create(userToken);
    }
}
