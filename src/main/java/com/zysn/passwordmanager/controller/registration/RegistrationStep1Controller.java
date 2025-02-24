/**
 * Sample Skeleton for 'Step1.fxml' Controller Class
 */

package com.zysn.passwordmanager.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;

import com.zysn.passwordmanager.controller.error.api.ErrorHandler;
import com.zysn.passwordmanager.controller.error.impl.DefaultErrorHandler;
import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.controller.scene.api.StepHandler;
import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationStepHandler;
import com.zysn.passwordmanager.model.authentication.registration.impl.RegistrationStep1Handler;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Controller for the first step of registration.
 * It extends {@link ControllerAbstract} and implements {@link StepHandler}.
 * 
 * @param <Pane>              the type of the root element managed by this
 *                            controller
 * @param <CollectedUserData> the type of data collected during registration
 */
public class RegistrationStep1Controller extends ControllerAbstract<Pane, CollectedUserData>
        implements StepHandler<Pane, CollectedUserData> {

    /**
     * ResourceBundle that was given to the FXMLLoader.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * URL location of the FXML file that was given to the FXMLLoader.
     */
    @FXML
    private URL location;

    /**
     * Password field for confirming the password. Value injected by FXMLLoader.
     */
    @FXML
    private PasswordField confirmPasswordField;

    /**
     * Password field for entering the password. Value injected by FXMLLoader.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Text field for entering the username. Value injected by FXMLLoader.
     */
    @FXML
    private TextField usernameField;

    private ErrorHandler errorHandler;

    private RegistrationStepHandler registrationStepHandler;

    /**
     * Initializes the data required for this controller.
     * Focus is set to the username field once initialized.
     */
    @Override
    public void initializeData() {
        this.registrationStepHandler = new RegistrationStep1Handler(getData());
        
        this.errorHandler = new DefaultErrorHandler();
        Platform.runLater(() -> this.usernameField.requestFocus());
    }

    /**
     * Handles the current step of registration.
     * Collects data and validates input.
     * 
     * @return true if the step was handled successfully, false otherwise
     */
    @Override
    public boolean handleStep() {
        boolean handleStatus = false;
        this.collectData();

        try {
            this.validateInput();
            handleStatus = true;
        } catch (final IllegalArgumentException e) {
            this.errorHandler.showError("Errors", e.getMessage());
        }

        return handleStatus;
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     * Ensures that all FXML fields are correctly injected.
     */
    @FXML
    void initialize() {
        assert confirmPasswordField != null
                : "fx:id=\"confirmPasswordField\" was not injected: check your FXML file 'Step1.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'Step1.fxml'.";
        assert usernameField != null : "fx:id=\"usernameField\" was not injected: check your FXML file 'Step1.fxml'.";
    }

    /**
     * Collects data from the input fields and stores it in
     * {@link CollectedUserData}.
     */
    private void collectData() {
        final String username = usernameField.getText();
        final byte[] password = passwordField.getText().getBytes();
        final byte[] confirmPassword = confirmPasswordField.getText().getBytes();

        super.getData().setUsername(username);
        super.getData().setPassword(password);
        super.getData().setConfirmPassword(confirmPassword);
    }

    /**
     * Validates the input data. Throws an exception if validation fails.
     * This method is primarily responsible for handling the validation logic.
     */
    private void validateInput() {
        this.registrationStepHandler.handleStep();
    }
}
