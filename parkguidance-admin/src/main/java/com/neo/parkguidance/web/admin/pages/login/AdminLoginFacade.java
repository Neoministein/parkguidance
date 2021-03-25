package com.neo.parkguidance.web.admin.pages.login;

import com.github.adminfaces.template.config.AdminConfig;
import com.neo.parkguidance.core.entity.RegisteredUser;
import com.neo.parkguidance.core.impl.dao.UserEntityFactory;
import com.neo.parkguidance.web.admin.security.UserBean;
import org.apache.commons.codec.digest.DigestUtils;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static com.neo.parkguidance.web.utils.Utils.addDetailMessage;
import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_224;

@Stateless
public class AdminLoginFacade {

    @EJB
    private UserEntityFactory userDAO;

    @Inject
    private AdminConfig adminConfig;

    public void checkCredentials(AdminLoginModel model, UserBean user) throws IOException {
        String userPassword = new DigestUtils(SHA_224).digestAsHex(model.getPassword().getBytes());
        RegisteredUser dbUser = lookUpDBUser(model.getUsername());

        if(dbUser != null && dbUser.getPassword().equals(userPassword)) {
            user.setIsLoggedIn(true);
            user.setRegisteredUser(dbUser);

            addDetailMessage("Logged in successfully as <b>" + model.getUsername() + "</b>");
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect(adminConfig.getIndexPage());
        } else {
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
