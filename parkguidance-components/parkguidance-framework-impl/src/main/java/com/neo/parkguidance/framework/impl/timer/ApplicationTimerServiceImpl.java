package com.neo.parkguidance.framework.impl.timer;

import com.neo.parkguidance.framework.api.timer.ApplicationTimer;
import com.neo.parkguidance.framework.api.timer.ApplicationTimerService;
import com.neo.parkguidance.framework.impl.event.ApplicationReadyEvent;
import com.neo.parkguidance.framework.impl.utils.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class ApplicationTimerServiceImpl implements ApplicationTimerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationTimerServiceImpl.class);

    private final Map<String, ApplicationTimer> applicationTimerMap = new HashMap<>();

    private AtomicBoolean initialized = new AtomicBoolean(false);


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void startUp(@Observes ApplicationReadyEvent applicationReadyEvent) {
        startsAllTimers();
        initialized.set(true);
    }

    @Override
    public void registerTimer(ApplicationTimer applicationTimer) {
        applicationTimerMap.put(applicationTimer.getTimerId(), applicationTimer);
    }

    @Override
    public void executeAllTimers() {
        if (initialized.get()) {
            LOGGER.info("Executing all timers...");
            for (ApplicationTimer applicationTimer: applicationTimerMap.values()) {
                LOGGER.info("Executing timer {}", applicationTimer.getTimerId());
                applicationTimer.poll();
            }
        }
    }

    @Override
    public void startsAllTimers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.info("Starting timers...");
        for (ApplicationTimer applicationTimer: applicationTimerMap.values()) {
            initializeTimer(applicationTimer);
        }
        stopWatch.stop();
        LOGGER.info("Finished starting timers. {} ms", stopWatch.getElapsedTimeMs());
    }

    @Override
    public void stopAllTimers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        LOGGER.info("Stopping timers...");
        for (ApplicationTimer applicationTimer: applicationTimerMap.values()) {
            initializeTimer(applicationTimer);
        }
        stopWatch.stop();
        LOGGER.info("Finished stopping timers. {} ms", stopWatch.getElapsedTimeMs());
    }

    @Override
    public void executeTimer(String timerId) {
        if (initialized.get()) {
            ApplicationTimer applicationTimer = applicationTimerMap.get(timerId);
            if (applicationTimer != null) {
                LOGGER.info("Executing timer {}", timerId);
                applicationTimer.poll();
            } else {
                LOGGER.debug("Timer {} cannot be executed, Timer nonexistent", timerId);
            }
        }
    }

    public void initializeTimer(ApplicationTimer applicationTimer) {
        try {
            LOGGER.info("Initializing timer: {}", applicationTimer.getTimerId());
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            applicationTimer.startTimer();

            stopWatch.stop();
            LOGGER.info("Finished initialized timer {}. Timer schedule {}. Initialized in {} ms",
                    applicationTimer.getTimerId(), applicationTimer.getScheduleExpression() ,stopWatch.getElapsedTimeMs());
        } catch (Exception ex) {
            LOGGER.error("Failed to initialize Timer {}", applicationTimer.getTimerId(), ex);
        }
    }

    @Override
    public Collection<ApplicationTimer> getAllTimers() {
        return applicationTimerMap.values();
    }

    @Override
    public Map<String, ApplicationTimer> getTimerMap() {
        return applicationTimerMap;
    }
}
