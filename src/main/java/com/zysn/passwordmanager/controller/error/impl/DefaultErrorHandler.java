package com.zysn.passwordmanager.controller.error.impl;

import com.zysn.passwordmanager.controller.error.api.ErrorHandler;

public class DefaultErrorHandler implements ErrorHandler {
    public DefaultErrorHandler() {

    }

    public void showError(String title, String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
