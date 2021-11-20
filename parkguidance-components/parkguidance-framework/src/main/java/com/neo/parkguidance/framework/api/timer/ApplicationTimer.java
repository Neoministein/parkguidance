package com.neo.parkguidance.framework.api.timer;

import javax.ejb.ScheduleExpression;

/**
 * This inteface defines
 *
 * An implementation should not have a {@link javax.annotation.PostConstruct} and let initialize be handled by the
 * {@link ApplicationTimerService}
 */
public interface ApplicationTimer {

    /**
     * Return the Timer ID.
     *
     * @return The Timer ID
     */
    String getTimerId();

    /**
     * Returns the actual schedule expression of the timer.
     *
     * return newExpression A calendar-based timeout expression for an enterprise bean timer
     */
    ScheduleExpression getScheduleExpression();

    /**
     * Returns true, if the timer is currently enabled.
     *
     * @return true, if the timer is currently enabled.
     */
    boolean isEnabled();

    /**
     * This method starts the Application timer.
     */
    void startTimer();

    /**
     * This method stops the Application timer.
     */
    void stopTimer();

    /**
     * This method will be invoked periodically in configured time intervals.
     */
    void poll();
}
