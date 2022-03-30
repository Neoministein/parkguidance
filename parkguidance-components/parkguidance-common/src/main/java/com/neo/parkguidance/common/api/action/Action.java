package com.neo.parkguidance.common.api.action;

import com.neo.parkguidance.common.impl.exception.InternalLogicException;

public interface Action<T> {

    /**
     * The action to run
     *
     * @throws {@link InternalLogicException} on action failure
     *
     * @return returns {@link T} on completion
     */
    T run();
}
