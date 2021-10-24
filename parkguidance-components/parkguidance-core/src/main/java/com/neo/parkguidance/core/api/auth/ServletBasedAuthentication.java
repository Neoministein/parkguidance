package com.neo.parkguidance.core.api.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServletBasedAuthentication {

    void login(String oauth2Token, String provider, String redirectSucess, String redirectFail,
            HttpServletRequest request, HttpServletResponse response) throws IOException;
}
