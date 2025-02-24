package com.zysn.passwordmanager.controller.error.api;

/**
 * An error handler interface for displaying error messages.
 */
public interface ErrorHandler {

    /**
     * Shows an error message with the specified title and message.
     *
     * @param title the title of the error message
     * @param message the content of the error message
     */
    public void showError(String title, String message);
}
