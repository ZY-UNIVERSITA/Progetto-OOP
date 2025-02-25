package com.zysn.passwordmanager.controller.scene.api;

/**
 * A step handler interface that extends the {@link GenericController} interface.
 *
 * @param <S> the source type
 * @param <T> the data type
 */
public interface StepHandler<S, T> extends GenericController<S, T> {

    /**
     * Handles a step in the process.
     *
     * @return true if the step was handled successfully, false otherwise
     */
    public boolean handleStep();
}
