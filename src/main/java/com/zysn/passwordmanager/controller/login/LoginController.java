package com.zysn.passwordmanager.controller.login;

import java.net.URL;
import java.util.ResourceBundle;

import com.zysn.passwordmanager.controller.error.api.ErrorHandler;
import com.zysn.passwordmanager.controller.error.impl.DefaultErrorHandler;
import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.authentication.login.api.LoginStepHandler;
import com.zysn.passwordmanager.model.authentication.login.impl.GeneralLoginHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for handling the login functionality.
 * 
 * <p>
 * This class extends {@link ControllerAbstract} with types {@link Stage} and
 * {@link AccountManager}.
 * </p>
 */
public class LoginController extends ControllerAbstract<Stage, AccountManager> {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="mainVBox"
    private VBox mainVBox; // Value injected by FXMLLoader

    @FXML // fx:id="passwordField"
    private PasswordField passwordField; // Value injected by FXMLLoader

    @FXML // fx:id="usernameField"
    private TextField usernameField; // Value injected by FXMLLoader

    private CollectedUserData collectedUserData;

    private LoginStepHandler loginStepHandler;

    private ErrorHandler errorHandler;

    /**
     * Initializes data for the login process.
     */
    @Override
    public void initializeData() {
        this.collectedUserData = new CollectedUserData();
        this.loginStepHandler = new GeneralLoginHandler(collectedUserData);
        this.errorHandler = new DefaultErrorHandler();
    }

    /**
     * Initializes the controller class.
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'Login.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'Login.fxml'.";
        assert usernameField != null : "fx:id=\"usernameField\" was not injected: check your FXML file 'Login.fxml'.";
    }

    /**
     * Handles the login action.
     *
     * @param event the action event triggered by the login button
     */
    @FXML
    void handleLogin(final ActionEvent event) {
        this.collectUserData();

        try {
            this.loginStepHandler.handleStep();

            super.getData().login(collectedUserData);

            this.chooseNextView();

        } catch (final IllegalArgumentException e) {
            if (this.getData().getSessionManager().getUserAccount().getMasterKey() == null) {
                errorHandler.showError("Errors", "Username or password are wrong.");
            } else {
                errorHandler.showError("Errors", e.getMessage());
            }
        }
    }

    /**
     * Handles the registration action.
     *
     * @param event the action event triggered by the registration button
     */
    @FXML
    void handleRegister(final ActionEvent event) {
        this.getNavigator().navigateTo("/layouts/registration/MainRegistrationPage.fxml", "Registration");
    }

    /**
     * Chooses the next view based on the user authentication information.
     */
    private void chooseNextView() {
        if (this.getData().getSessionManager().getUserAuthInfo().isEnabled2FA()) {
            this.getNavigator().navigateTo("/layouts/login/Login2FA.fxml", "Main");
        } else {
            this.getNavigator().navigateTo("/layouts/main/Main.fxml", "Main");
        }
        
        this.collectedUserData.destroy();
    }

    /**
     * Collects user data from the login fields.
     */
    private void collectUserData() {
        this.collectedUserData.setUsername(this.usernameField.getText());
        this.collectedUserData.setPassword(this.passwordField.getText().getBytes());
    }
}
