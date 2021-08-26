package com.neo.parkguidance.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import java.io.InputStream;

/**
 * This class loads configuration on startup from configuration files
 */
@Singleton
@Startup
public class LoadProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadProperties.class);

    /**
     * Loads application server properties on startup
     */
    @PostConstruct
    public void init() {
        try (InputStream input = LoadProperties.class.getClassLoader().getResourceAsStream("application-server.properties")) {
            System.getProperties().load(input);

        } catch (Exception e) {
            LOGGER.error("Unable to load application-server.properties, the application will most likely not be working correctly", e);
        }
    }
}
