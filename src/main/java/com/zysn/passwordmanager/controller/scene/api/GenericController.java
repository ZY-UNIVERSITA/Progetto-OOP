package com.zysn.passwordmanager.controller.scene.api;

import com.zysn.passwordmanager.controller.navigation.api.GenericNavigator;

/**
 * A generic controller interface for managing data and navigation.
 *
 * @param <S> the source type
 * @param <T> the data type
 */
public interface GenericController<S, T> {

    /**
     * Sets the navigator for the controller.
     *
     * @param navigator the navigator to be set
     */
    public void setNavigator(GenericNavigator<S, T> navigator);

    /**
     * Sets the data for the controller.
     *
     * @param data the data to be set
     */
    public void setData(T data);

    /**
     * Initializes the data.
     */
    public void initializeData();

    /**
     * Initializes the data with optional additional data.
     *
     * @param <U> the type of the optional additional data
     * @param optionalData the optional additional data to initialize
     */
    public <U> void initializeData(U optionalData);
}

