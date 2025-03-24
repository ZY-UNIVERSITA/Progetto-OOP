/**
 * Sample Skeleton for 'Step4.fxml' Controller Class
 */

package com.zysn.passwordmanager.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;

import com.zysn.passwordmanager.controller.error.api.ErrorHandler;
import com.zysn.passwordmanager.controller.error.impl.DefaultErrorHandler;
import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.controller.scene.api.StepHandler;
import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * Controller for the fourth step of registration.
 * It extends {@link ControllerAbstract} and implements {@link StepHandler}.
 * 
 * @param <Pane>              the type of the root element managed by this
 *                            controller
 * @param <CollectedUserData> the type of data collected during registration
 */
public class RegistrationStep4Controller extends ControllerAbstract<Pane, CollectedUserData>
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
     * VBox for adding 2FA options. Value injected by FXMLLoader.
     */
    @FXML
    private VBox add2FAVBox;

    /**
     * Button for opting out of 2FA. Value injected by FXMLLoader.
     */
    @FXML
    private Button no2FAButton;

    /**
     * Button for opting in for 2FA. Value injected by FXMLLoader.
     */
    @FXML
    private Button yes2FAButton;

    private ErrorHandler errorHandler;

    private boolean twoFactorResult;

    /**
     * Handles the current step of registration.
     * Ensures that the user has decided whether or not to enable 2FA security.
     * 
     * @return true if the user has made a decision, false otherwise
     */
    @Override
    public boolean handleStep() {
        if (!this.twoFactorResult) {
            errorHandler.showError("Error", "You must decide whether or not to enable 2FA security.");
        }

        return this.twoFactorResult;
    }

    /**
     * Initializes the data required for this controller.
     * Sets up the error handler and initializes the two-factor authentication
     * result.
     */
    public void initializeData() {
        this.errorHandler = new DefaultErrorHandler();
        this.twoFactorResult = false;
    }

    /**
     * Handles the action to enable 2FA.
     * Sets the enabled 2FA flag in {@link CollectedUserData}.
     *
     * @param event the action event
     */
    @FXML
    void add2FA(final ActionEvent event) {
        this.twoFactorResult = true;
        super.getData().setEnabled2FA(true);
    }

    /**
     * Handles the action to disable 2FA.
     * Sets the enabled 2FA flag in {@link CollectedUserData}.
     *
     * @param event the action event
     */
    @FXML
    void dontAdd2FA(final ActionEvent event) {
        this.twoFactorResult = true;
        super.getData().setEnabled2FA(false);
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     * Ensures that all FXML fields are correctly injected.
     */
    @FXML
    void initialize() {
        assert add2FAVBox != null : "fx:id=\"add2FAVBox\" was not injected: check your FXML file 'Step4.fxml'.";
        assert no2FAButton != null : "fx:id=\"no2FAButton\" was not injected: check your FXML file 'Step4.fxml'.";
        assert yes2FAButton != null : "fx:id=\"yes2FAButton\" was not injected: check your FXML file 'Step4.fxml'.";
    }
}
