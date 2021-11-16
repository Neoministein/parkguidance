package com.neo.parkguidance.core.impl;

import com.neo.parkguidance.core.impl.event.ApplicationPostReadyEvent;
import com.neo.parkguidance.core.impl.event.ApplicationPreReadyEvent;
import com.neo.parkguidance.core.impl.event.ApplicationReadyEvent;
import com.neo.parkguidance.core.impl.utils.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * This class start services
 */
@Singleton
@Startup
public class ApplicationStartUp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartUp.class);

    @Inject
    Event<ApplicationPreReadyEvent> applicationPreReadyEventEvent;

    @Inject
    Event<ApplicationReadyEvent> applicationReadyEvent;

    @Inject
    Event<ApplicationPostReadyEvent> applicationPostReadyEvent;

    /**
     * Fires an {@link ApplicationReadyEvent} after the server as stared up
     */
    @PostConstruct
    public void init() {
        fireApplicationPreReadyEvent();
        fireApplicationReadyEvent();
        fireApplicationPostReadyEvent();
    }

    protected void fireApplicationPreReadyEvent() {
        LOGGER.info("Fire Application pre ready event");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        applicationPreReadyEventEvent.fire(new ApplicationPreReadyEvent());
        stopWatch.stop();
        LOGGER.info("Application ready pre event took: {} ms ", stopWatch.getElapsedTimeMs());
    }

    protected void fireApplicationReadyEvent() {
        LOGGER.info("Fire Application ready event");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        applicationReadyEvent.fire(new ApplicationReadyEvent());
        stopWatch.stop();
        LOGGER.info("Application Init event took: {} ms ", stopWatch.getElapsedTimeMs());
    }

    protected void fireApplicationPostReadyEvent() {
        LOGGER.info("Fire Application post ready event");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        applicationPostReadyEvent.fire(new ApplicationPostReadyEvent());
        stopWatch.stop();
        LOGGER.info("Application ready post event took: {} ms ", stopWatch.getElapsedTimeMs());
    }
}
