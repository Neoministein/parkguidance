package com.neo.parkguidance.web.utils;

import org.omnifaces.util.Messages;

import javax.faces.application.FacesMessage;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

public class Utils {

    private Utils(){}

    public static void addDetailMessage(String message) {
        addDetailMessage("",message);
    }

    public static void addDetailMessage(String title,String message) {
        addDetailMessage(title ,message, null);
    }

    public static void addDetailMessage(String title ,String message, FacesMessage.Severity severity) {

        FacesMessage facesMessage = Messages.create(title).detail(message).get();
        if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
            facesMessage.setSeverity(severity);
        }
        Messages.add(null, facesMessage);
    }

    public static boolean pingHost(String url) {
        try {
            URL con = new URL(url);
            InputStream stream = con.openStream();
            stream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
