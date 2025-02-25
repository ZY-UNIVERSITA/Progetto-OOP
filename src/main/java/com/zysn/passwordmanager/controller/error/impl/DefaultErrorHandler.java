package com.zysn.passwordmanager.controller.error.impl;

import com.zysn.passwordmanager.controller.error.api.ErrorHandler;

/**
 * A default implementation of the {@link ErrorHandler} interface that uses
 * JavaFX to display error messages.
 */
public class DefaultErrorHandler implements ErrorHandler {

    /**
     * Default constructor for the error handler.
     */
    public DefaultErrorHandler() {

    }

    /**
     * Shows an error message with the specified title and message.
     *
     * @param title   the title of the error message
     * @param message the content of the error message
     */
    public void showError(final String title, final String message) {
        final javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
