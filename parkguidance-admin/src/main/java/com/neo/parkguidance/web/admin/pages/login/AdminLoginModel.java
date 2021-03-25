package com.neo.parkguidance.web.admin.pages.login;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class AdminLoginModel implements Serializable {

    private String username;
    private String password;

    private boolean remember = false;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }
}
