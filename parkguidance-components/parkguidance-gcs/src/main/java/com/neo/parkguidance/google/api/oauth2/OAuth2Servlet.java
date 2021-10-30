package com.neo.parkguidance.google.api.oauth2;

import com.neo.parkguidance.core.api.auth.ServletBasedAuthentication;
import com.neo.parkguidance.core.impl.auth.credential.OAuthCredential;

import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/auth/google")
public class OAuth2Servlet extends HttpServlet {

    @Inject
    ServletBasedAuthentication servletBasedAuthentication;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> map = new HashMap<>();
        try {
            String postMessage = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            for (String entry: postMessage.split("&")) {
                String[] entryValues = entry.split("=");
                map.put(entryValues[0],entryValues[1]);
            }
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        Credential credential = new OAuthCredential(map.get("credential"), OAuth2GoogleClient.TYPE);
        if (map.containsKey("success")) {
            servletBasedAuthentication.login(credential, map.get("success"), map.get("failed"), req, resp);
        } else {
            servletBasedAuthentication.login(credential, "/index", "/login", req, resp);
        }
    }
}

