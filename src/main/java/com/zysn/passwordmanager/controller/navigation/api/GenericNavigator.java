package com.zysn.passwordmanager.controller.navigation.api;

import com.zysn.passwordmanager.controller.scene.api.GenericController;

/**
 * GenericNavigator is an interface for navigating to different scenes or views.
 *
 * @param <S> the source type
 * @param <T> the target type
 */
public interface GenericNavigator<S, T> {

    /**
     * Navigates to a specified scene.
     *
     * @param pathToFile the path to the file defining the scene
     * @param sceneTitle the title of the scene
     * @return a GenericController for the specified scene
     */
    public GenericController<S, T> navigateTo(String pathToFile, String sceneTitle);

    /**
     * Navigates to a specified scene with optional data.
     *
     * @param pathToFile the path to the file defining the scene
     * @param sceneTitle the title of the scene
     * @param <U> the type of the optional data
     * @param optionalData additional data to be used in the scene
     * @return a GenericController for the specified scene
     */
    public <U> GenericController<S, T> navigateTo(String pathToFile, String sceneTitle, U optionalData);

    /**
     * Return the view associated with the navigator.
     * @return the view.
     */
    public S getView();
}
