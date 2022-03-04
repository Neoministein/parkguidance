package com.neo.parkguidance.ms.user.impl.dao;

import com.neo.parkguidance.common.impl.util.RandomString;
import com.neo.parkguidance.ms.user.impl.entity.UserToken;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Date;

@RequestScoped
public class UserTokenRepository extends AbstractEntityDao<UserToken> implements EntityDao<UserToken> {

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
    public void create(UserToken entity) {
        RandomString randomString = new RandomString();
        entity.setCreationDate(new Date());
        for(int i = 0; i < 10;i++) {
            try {
                entity.setKey(randomString.nextString());
                super.create(entity);
                return;
            } catch (PersistenceException ex) {
                //TODO IMPL BETTER RETRY
            }
        }
    }
}
