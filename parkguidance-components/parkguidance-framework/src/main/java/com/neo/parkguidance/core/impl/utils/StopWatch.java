package com.neo.parkguidance.core.impl.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A very basic class to time elapsed time between a start and a stop call.
 */
public final class StopWatch {
    /**
     * Start date of when the stop watch was started.
     */
    Date start = null;
    /**
     * Stop date of when the stop watch was started.
     */
    Date stop = null;

    /**
     * create trivial object to count a start and stop time point.
     */
    public StopWatch() {}

    /**
     * sets the start date and resets the stop date.
     */
    public void start() {
        start = new Date();
        stop = null;
    }

    /**
     * sets the stop date.
     */
    public void stop() {
        stop = new Date();
    }

    /**
     * Get the time elapsed between the call to start and stop.
     *
     * @param desiredTimeUnit the desired time unit for the result (e.g. ms, seconds)
     * @return the time passed in the desired time unit between the call to start and stop.
     */
    public long getElapsedTime(TimeUnit desiredTimeUnit) {
        // (a) if the user is not using the apis properly the value he gets will be a dummy one
        if (start == null || stop == null) {
            return Long.MIN_VALUE;
        }

        // (b) compute the time difference
        long timeDiffMs = stop.getTime() - start.getTime();
        return desiredTimeUnit.convert(timeDiffMs, TimeUnit.MILLISECONDS);
    }

    /**
     * The elpased time between start and stop call in ms.
     *
     * @return the elapased time in ms.
     */
    public long getElapsedTimeMs() {
        return getElapsedTime(TimeUnit.MILLISECONDS);
    }
}
