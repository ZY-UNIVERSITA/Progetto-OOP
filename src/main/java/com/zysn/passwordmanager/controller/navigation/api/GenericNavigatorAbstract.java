package com.zysn.passwordmanager.controller.navigation.api;

import java.io.IOException;
import java.util.Optional;

import com.zysn.passwordmanager.controller.scene.api.GenericController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Abstract class providing a generic navigator implementation.
 *
 * @param <S> the source type
 * @param <T> the target type
 */
public abstract class GenericNavigatorAbstract<S, T> implements GenericNavigator<S, T> {
    private final S view;
    private final T data;

    /**
     * Constructor initializing view and data.
     *
     * @param view the view
     * @param data the data
     */
    public GenericNavigatorAbstract(final S view, final T data) {
        this.view = view;
        this.data = data;
    }

    /**
     * Navigates to a specified scene.
     *
     * @param pathToFile the path to the file defining the scene
     * @param title the title of the scene
     * @return a GenericController for the specified scene
     */
    public GenericController<S, T> navigateTo(final String pathToFile, final String title) {
        return this.navigateTo(pathToFile, title, null);
    }

    /**
     * Navigates to a specified scene with optional data.
     *
     * @param pathToFile the path to the file defining the scene
     * @param title the title of the scene
     * @param <U> the type of the optional data
     * @param optionalData additional data to be used in the scene
     * @return a GenericController for the specified scene
     * @throws IllegalArgumentException if pathToFile or title is null
     */
    public <U> GenericController<S, T> navigateTo(final String pathToFile, final String title, final U optionalData) {
        if (pathToFile == null || title == null) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }

        GenericController<S, T> controller = null;

        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToFile));
            final Parent root = loader.load();

            if (loader.getController() instanceof GenericController) {
                controller = loader.getController();

                controller.setNavigator(this);
                controller.setData(data);

                // Optional: to handle the presence or absence of optionalData
                Optional.ofNullable(optionalData).ifPresentOrElse(controller::initializeData,
                        controller::initializeData);
            }

            this.setView(root, title);
        } catch (final IOException e) {
            System.err.println("An error occurred while trying to change scene.");
        }

        return controller;
    }

    /**
     * Sets the view for the specified scene.
     *
     * @param root the root node of the scene
     * @param title the title of the scene
     */
    protected abstract void setView(Parent root, String title);

    /**
     * Returns the view.
     *
     * @return the view
     */
    public S getView() {
        return view;
    }

    /**
     * Returns the data.
     *
     * @return the data
     */
    protected T getData() {
        return data;
    }
}
