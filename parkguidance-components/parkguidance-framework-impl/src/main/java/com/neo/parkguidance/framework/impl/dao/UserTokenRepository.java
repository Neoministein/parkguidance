package com.neo.parkguidance.framework.impl.dao;

import com.neo.parkguidance.framework.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.framework.entity.UserToken;
import com.neo.parkguidance.framework.impl.validation.UserTokenValidator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Stateless
public class UserTokenRepository extends AbstractEntityDao<UserToken> implements
        EntityDaoAbstraction<UserToken> {

    @Inject
    UserTokenValidator userTokenValidator;

    @PersistenceContext(unitName = "data_persistence_unit")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserTokenRepository() {
        super(UserToken.class);
    }

    @Override
    public void create(UserToken userToken) {
        userToken.setCreationDate(new Date());
        userTokenValidator.newUniqueKey(userToken);
        super.create(userToken);
    }
}
