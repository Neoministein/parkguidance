package com.neo.parkguidance.core.api.timer;

import java.util.Collection;
import java.util.Map;

/**
 * This interface defines the functionality of an application timer which periodically executes a {@link ApplicationTimer}
 */
public interface ApplicationTimerService {

    /**
     * Adds the application timer to the service which allows it to be run by the timer
     *
     * @param applicationTimer the {@link ApplicationTimer} to add
     */
    void registerTimer(ApplicationTimer applicationTimer);

    /**
     * Manually execute all {@link ApplicationTimer}
     */
    void executeAllTimers();

    /**
     * Starts all timers
     */
    void startsAllTimers();

    /**
     * Stops all running timers
     */
    void stopAllTimers();

    /**
     * Manually executes the specific {@link ApplicationTimer}
     *
     * @param timerId the id of the timer
     */
    void executeTimer(String timerId);

    /**
     * Returns all active timers
     *
     * @return all current timers
     */
    Collection<ApplicationTimer> getAllTimers();

    /**
     * Returns a map with all currently registered timers. The key is the timer id.
     *
     * @return all current timers
     */
    Map<String, ApplicationTimer> getTimerMap();

}
