package com.neo.parkguidance.core.impl;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import java.io.InputStream;

@Singleton
@Startup
public class LoadProperties {

    @PostConstruct
    public void init() {
        try (InputStream input = LoadProperties.class.getClassLoader().getResourceAsStream("application-server.properties")) {
            System.getProperties().load(input);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
