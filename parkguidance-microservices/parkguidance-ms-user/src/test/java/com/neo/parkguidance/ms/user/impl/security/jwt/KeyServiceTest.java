package com.neo.parkguidance.ms.user.impl.security.jwt;

import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.impl.entity.KeyPair;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

class KeyServiceTest {

    KeyServiceImpl subject;

    EntityDao<KeyPair> keyPairDao;

    @BeforeEach
    public void init() {
        subject = Mockito.spy(KeyServiceImpl.class);

        keyPairDao = Mockito.mock(EntityDao.class);
        subject.keyPairDao = keyPairDao;
    }
}
