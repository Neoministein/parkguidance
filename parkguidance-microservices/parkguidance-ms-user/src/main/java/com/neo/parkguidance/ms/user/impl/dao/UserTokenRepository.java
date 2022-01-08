package com.neo.parkguidance.ms.user.impl.dao;

import com.neo.parkguidance.common.impl.util.RandomString;
import com.neo.parkguidance.ms.user.impl.entity.UserToken;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        do {
            RandomString randomString = new RandomString();
            entity.setKey(randomString.nextString());
        } while (findByColumn(UserToken.C_KEY, entity.getKey()) != null);
        entity.setCreationDate(new Date());
        super.create(entity);
    }
}
