package com.neo.parkguidance.web.impl.properties;

import com.neo.parkguidance.core.impl.event.ApplicationPostReadyEvent;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;

/**
 *
 */
@RequestScoped
public class AdminFaceProperties {

    public void init(@Observes ApplicationPostReadyEvent event) {
        System.setProperty("admin.dateFormat","yyyy-MM-dd HH:mm:ss");
        System.setProperty("admin.renderControlSidebar", "false");
        System.setProperty("admin.extensionLessUrls","true");
        System.setProperty("admin.controlSidebar.showOnMobile","false");
    }
}
