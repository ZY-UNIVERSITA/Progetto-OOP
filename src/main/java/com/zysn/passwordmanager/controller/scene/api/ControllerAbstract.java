package com.zysn.passwordmanager.controller.scene.api;

import com.zysn.passwordmanager.controller.navigation.api.GenericNavigator;

/**
 * An abstract controller class that implements the {@link GenericController} interface.
 *
 * @param <S> the source type
 * @param <T> the data type
 */
public abstract class ControllerAbstract<S, T> implements GenericController<S, T> {
    private GenericNavigator<S, T> navigator;
    private T data;

    /**
     * Default constructor for the abstract controller.
     */
    public ControllerAbstract() {
        
    }

    /**
     * Initializes the data with optional additional data.
     *
     * @param <U> the type of the optional additional data
     * @param optionalData the optional additional data to initialize
     */
    @Override
    public <U> void initializeData(final U optionalData) {
           
    }

    /**
     * Initializes the data.
     */
    @Override
    public void initializeData() {
        
    }

    /**
     * Sets the data for the controller.
     *
     * @param data the data to be set
     */
    @Override
    public void setData(final T data) {
        this.data = data;
    }

    /**
     * Sets the navigator for the controller.
     *
     * @param navigator the {@link GenericNavigator} to be set
     */
    @Override
    public void setNavigator(final GenericNavigator<S, T> navigator) {
        this.navigator = navigator;
    }

    /**
     * Gets the navigator for the controller.
     *
     * @return the {@link GenericNavigator}
     */
    public GenericNavigator<S, T> getNavigator() {
        return navigator;
    }

    /**
     * Gets the data for the controller.
     *
     * @return the data
     */
    public T getData() {
        return data;
    }
}
