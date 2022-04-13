package com.neo.parkguidance.ms.user.impl.security.jwt;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPrivateKey;
import com.neo.parkguidance.ms.security.impl.authentication.key.JWTPublicKey;
import com.neo.parkguidance.ms.user.api.dao.EntityDao;
import com.neo.parkguidance.ms.user.api.security.jwt.KeyService;
import com.neo.parkguidance.ms.user.impl.entity.KeyPair;
import com.neo.parkguidance.ms.user.impl.utils.KeyPairUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.RollbackException;
import java.util.*;

@ApplicationScoped
public class KeyServiceImpl implements KeyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyServiceImpl.class);

    private static final long THREE_MONTHS = 1000L * 60L * 60L * 24L * 90L;
    private static final long TIME_TO_UPDATE = 1000L * 10L;

    private static final Map<String, Boolean> CHECK_UPDATE_SORT_ORDER = Map.of(
            KeyPair.C_EXPIRATION_DATE, false
    );

    private static final Map<String, Object> ENABLED_KEYS = Map.of(
            KeyPair.C_DISABLED, false
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
    public void relieveCurrentPrivateKey() {
        createKeyPair();
        update();
    }

    @Override
    public void revokeActivePrivateKeys() {
        for (KeyPair keyPair: keyPairDao.findByColumn(KeyPair.C_DISABLED, false)) {
            revokeKey(keyPair);
        }
        createKeyPair();
        update();
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
        try {
            keyPairDao.create(keyPair);
        } catch (RollbackException ex) {
            LOGGER.error("Unable to create new keypair", ex);
            throw new InternalLogicException("Unable to create new keypair");
        }

    }

    protected void checkToUpdate() {
        if (lastUpdate < System.currentTimeMillis()) {
            update();
        }
    }

    protected void update() {
        List<KeyPair> keyPairs = keyPairDao.findByColumn(ENABLED_KEYS, CHECK_UPDATE_SORT_ORDER,0,3);

        if (keyPairs.isEmpty()) {
            createKeyPair();
            update();
            return;
        }

        for (KeyPair keyPair: keyPairs) {
            if (keyPair.getExpirationDate().before(new Date())) {
                revokeKey(keyPair);
                update();
            }

        }

        List<JWTPublicKey> publicKeyList = new ArrayList<>();
        for (KeyPair keyPair: keyPairs) {
            publicKeyList.add(KeyPairUtils.parseToJWTPublicKey(keyPair));
        }

        this.currentPublicKey = publicKeyList;
        currentPrivateKey = KeyPairUtils.parseToJWTPrivateKey(keyPairs.get(0));

        lastUpdate = System.currentTimeMillis() + TIME_TO_UPDATE;
    }

    protected void revokeKey(KeyPair keyPair) {
        keyPair.setDisabled(true);
        try {
            keyPairDao.edit(keyPair);
        } catch (RollbackException ex) {
            LOGGER.error("Unable to revoke private key", ex);
            throw new InternalLogicException("Unable to revoke private key");
        }

    }
}
