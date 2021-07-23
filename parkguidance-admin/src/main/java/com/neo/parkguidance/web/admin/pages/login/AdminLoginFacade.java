package com.neo.parkguidance.web.admin.pages.login;

import com.github.adminfaces.template.config.AdminConfig;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.impl.dao.AbstractEntityDao;
import com.neo.parkguidance.web.admin.security.UserBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;

/**
 * The screen facade for the AdminLogin screen
 */
@Stateless
public class AdminLoginFacade {

    private static final Logger LOGGER = LogManager.getLogger(AdminLoginFacade.class);

    @Inject
    AbstractEntityDao<RegisteredUser> userDAO;

    @Inject
    AdminConfig adminConfig;

    public void checkCredentials(AdminLoginModel model, UserBean user) throws IOException {
        LOGGER.info("Login attempt");
        String userPassword = new DigestUtils(SHA_224).digestAsHex(model.getPassword().getBytes());
        RegisteredUser dbUser = lookUpDBUser(model.getUsername());

        if(dbUser != null && dbUser.getPassword().equals(userPassword)) {
            user.setIsLoggedIn(true);
            user.setRegisteredUser(dbUser);

            addDetailMessage("Logged in successfully as " + model.getUsername());
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect(adminConfig.getIndexPage());
            LOGGER.info("Login success with [{}] account", dbUser.getUsername());
        } else {
            LOGGER.info("Login failed on [{}] account", model.getUsername());
            Messages.addError(null, "Login failed");
        }
    }

    private RegisteredUser lookUpDBUser(String username){
        List<RegisteredUser> dbLookup = userDAO.findByColumn(RegisteredUser.C_USERNAME,username);

        if(!dbLookup.isEmpty()) {
            return dbLookup.get(0);
        } else {
            return null;
        }
    }
}
