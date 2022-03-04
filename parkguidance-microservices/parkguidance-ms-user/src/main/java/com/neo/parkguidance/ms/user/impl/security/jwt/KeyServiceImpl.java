package com.neo.parkguidance.ms.user.impl.security.jwt;

import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPrivateKey;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPublicKey;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.api.security.jwt.KeyService;
import com.neo.parkguidance.ms.user.impl.entity.KeyPair;
import com.neo.parkguidance.ms.user.impl.utils.KeyPairUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;

@ApplicationScoped
public class KeyServiceImpl implements KeyService {

    private static final long THREE_MONTHS = 1000L * 60L * 60L * 24L * 90L;
    private static final long TIME_TO_UPDATE = 1000L * 10L;

    private static final Map<String, Boolean> CHECK_UPDATE_SORT_ORDER = Map.of(
            KeyPair.C_EXPIRATION_DATE, false
    );

    private long lastUpdate = 0;
    private JWTPrivateKey currentPrivateKey;
    private List<JWTPublicKey> currentPublicKey = new ArrayList<>();

    @Inject
    EntityDao<KeyPair> keyPairDao;

    @PostConstruct
    public void init() {
        checkToUpdate();
    }

    @Override
    public void generateNewKeyPair() {
        for (KeyPair keyPair: keyPairDao.findByColumn(KeyPair.C_DISABLED, false)) {
            keyPair.setDisabled(true);
            keyPairDao.edit(keyPair);
        }
        createKeyPair();
    }

    @Override
    public JWTPrivateKey getCurrentPrivateKey() {
        checkToUpdate();
        return currentPrivateKey;
    }

    @Override
    public List<JWTPublicKey> getActivePublicKeys() {
        checkToUpdate();
        return currentPublicKey;
    }

    protected void createKeyPair() {
        Date expirationDate = new Date(THREE_MONTHS + System.currentTimeMillis());
        KeyPair keyPair = KeyPairUtils.generateNewKeyPair(expirationDate);
        keyPairDao.create(keyPair);
    }

    protected void checkToUpdate() {
        if (lastUpdate < System.currentTimeMillis()) {
            List<KeyPair> keyPairs = keyPairDao.findByColumn(Collections.emptyMap() ,CHECK_UPDATE_SORT_ORDER,0,3);

            if (keyPairs.isEmpty() || keyPairs.get(0).getDisabled().booleanValue()) {
                createKeyPair();
                checkToUpdate();
                return;
            }

            if (keyPairs.get(0).getExpirationDate().before(new Date())) {
                generateNewKeyPair();
                checkToUpdate();
                return;
            }

            List<JWTPublicKey> publicKeyList = new ArrayList<>();
            for (KeyPair keyPair: keyPairs) {
                publicKeyList.add(new JWTPublicKey(
                        keyPair.getId().toString(),
                        KeyPairUtils.getPublicKey(keyPair),
                        keyPair.getExpirationDate()));
            }

            this.currentPublicKey = publicKeyList;
            currentPrivateKey = new JWTPrivateKey(
                    keyPairs.get(0).getId().toString(),
                    KeyPairUtils.getPrivateKey(keyPairs.get(0)),
                    keyPairs.get(0).getExpirationDate());


            lastUpdate = System.currentTimeMillis() + TIME_TO_UPDATE;
        }
    }
}
