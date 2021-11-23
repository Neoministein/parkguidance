package com.neo.parkguidance.microservices.impl.dao;

import com.neo.parkguidance.framework.entity.UserToken;
import com.neo.parkguidance.microservices.api.dao.EntityDaoAbstraction;
import com.neo.parkguidance.microservices.impl.validation.UserTokenValidator;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Priority(100)
@RequestScoped
public class UserTokenRepository extends AbstractEntityDao<UserToken> implements EntityDaoAbstraction<UserToken> {

    @Inject UserTokenValidator userTokenValidator;

    @PersistenceContext(unitName = "parkguidancePersistence")
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
