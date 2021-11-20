package com.neo.parkguidance.framework.impl.timer;

import com.neo.parkguidance.framework.api.timer.ApplicationTimer;
import com.neo.parkguidance.framework.api.timer.ApplicationTimerService;
import com.neo.parkguidance.framework.impl.event.ApplicationPreReadyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

public abstract class AbstractApplicationTimer implements ApplicationTimer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationTimer.class);

    private final ScheduleExpression scheduleExpression;

    private Timer currentTimer = null;

    @Resource
    TimerService timerService;

    @Inject
    ApplicationTimerService applicationTimerService;

    /**
     * Creates a new {@link AbstractApplicationTimer} which will be fired every 30 seconds
     */
    protected AbstractApplicationTimer() {
        this(new ScheduleExpression().hour("*").minute("*").second("*/30"));
    }

    protected AbstractApplicationTimer(ScheduleExpression scheduleExpression) {
        this.scheduleExpression = scheduleExpression;
    }

    @Timeout
    public void timeout(Timer timer) {
        poll();
    }

    public void init(@Observes ApplicationPreReadyEvent event) {
        applicationTimerService.registerTimer(this);
    }

    @Override
    public String getTimerId() {
        return this.getClass().getName();
    }

    @Override
    public ScheduleExpression getScheduleExpression() {
        return scheduleExpression;
    }

    @Override
    public boolean isEnabled() {
        return currentTimer != null;
    }

    @Override
    public void startTimer() {
        LOGGER.info("Starting timer");

        if (isEnabled()) {
            LOGGER.debug("Timer is already running. Restarting timer {}", getTimerId());
            stopTimer();
        }
        start();
        currentTimer = timerService.createCalendarTimer(scheduleExpression);
        LOGGER.info("Timer started");
    }

    @Override
    public void stopTimer() {
        if (isEnabled()) {
            LOGGER.info("Stopping timer {}", getTimerId());
            stop();
            try {
                currentTimer.cancel();
            } catch (NoSuchObjectLocalException ex) {
                LOGGER.error("Could not cancel the running timer {}", getTimerId(), ex);
            }

            currentTimer = null;
        } else {
            LOGGER.debug("Timer {} already stopped", getTimerId());
        }
    }

    @Override
    public void poll() {

    }

    protected void start() {

    }

    protected void stop() {

    }

}
