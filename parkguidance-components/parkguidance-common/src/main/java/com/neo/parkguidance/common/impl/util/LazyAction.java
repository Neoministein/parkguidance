package com.neo.parkguidance.common.impl.util;

import com.neo.parkguidance.common.api.action.Action;
import com.neo.parkguidance.common.impl.exception.InternalLogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LazyAction {

    private static final Logger LOGGER =  LoggerFactory.getLogger(LazyAction.class);

    private static final int DEFAULT_STARTING_TIME = 100;

    private LazyAction() {}

    /**
     * Executes the given action for n amounts of times if the action fails
     *
     * @param actionToExecute the action to Execute
     * @param retries the amount of retries available
     *
     * @throws InternalLogicException if it fails and no more retries are available
     */
    public static <T> T call(Action<T> actionToExecute, int retries, int maxWaitTimeInMilli) {
        return call(actionToExecute, retries, 0, maxWaitTimeInMilli);
    }

    /**
     * Executes the given action for n amounts of times if the action fails
     *
     * @param actionToExecute the action to Execute
     * @param retries the amount of retries available
     *
     * @throws InternalLogicException if it fails and no more retries are available
     */
    public static <T> T call(Action<T> actionToExecute, int retries) {
        return call(actionToExecute, retries, 0, DEFAULT_STARTING_TIME);
    }

    protected static <T> T call(Action<T> actionToExecute, int retries, int count, int startingTime) {
        try {
            return actionToExecute.run();
        } catch (InternalLogicException ex) {
            LOGGER.warn("Failed to execute action {} -> retrying {} times", ex.getMessage() ,retries - count);
            if (retries <= count) {
                throw new InternalLogicException("Lazy action cannot be fulfilled request");
            }
            wait(count, startingTime);
            call(actionToExecute, retries, count + 1, startingTime);
        }
        return null;
    }

    protected static void wait(int count , int startingTime) {
        long waitFor = (long) (startingTime * (Math.pow(2, count)));

        try {
            Thread.sleep(waitFor);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
