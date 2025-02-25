package com.zysn.passwordmanager.controller.login;

import java.net.URL;
import java.util.ResourceBundle;

import com.zysn.passwordmanager.controller.error.api.ErrorHandler;
import com.zysn.passwordmanager.controller.error.impl.DefaultErrorHandler;
import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.security.totp.api.TOTPAuthentication;
import com.zysn.passwordmanager.model.security.totp.impl.DefaultTotpAuthentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for handling the 2FA login functionality.
 * 
 * <p>
 * This class extends {@link ControllerAbstract} with types {@link Stage} and
 * {@link AccountManager}.
 * </p>
 */
public class Login2FAController extends ControllerAbstract<Stage, AccountManager> {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="mainVBox"
    private VBox mainVBox; // Value injected by FXMLLoader

    @FXML // fx:id="twoFactorAuthenticationCode"
    private TextField twoFactorAuthenticationCode; // Value injected by FXMLLoader

    private ErrorHandler errorHandler;

    private TOTPAuthentication totpAuthentication;

    /**
     * Initializes data for the 2FA login process.
     */
    @Override
    public void initializeData() {
        this.errorHandler = new DefaultErrorHandler();
        this.totpAuthentication = new DefaultTotpAuthentication(
                this.getData().getSessionManager().getUserAuthKey().getTotpKey());
    }

    /**
     * Handles the action to navigate back to the login page.
     *
     * @param event the action event triggered by the back to login button
     */
    @FXML
    void backToLogin(final ActionEvent event) {
        this.getNavigator().navigateTo("/layouts/login/Login.fxml", "Login");
    }

    /**
     * Handles the 2FA login action.
     *
     * @param event the action event triggered by the login button
     */
    @FXML
    void handleLogin(final ActionEvent event) {
        if (!totpAuthentication.validateCode(twoFactorAuthenticationCode.getText().toCharArray())) {
            errorHandler.showError("Wrong code", "Your inserted code is wrong");
        } else {
            this.getNavigator().navigateTo("/layouts/Main.fxml", "Main");
        }
    }

    /**
     * Initializes the controller class.
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'Login2FA.fxml'.";
        assert twoFactorAuthenticationCode != null
                : "fx:id=\"twoFactorAuthenticationCode\" was not injected: check your FXML file 'Login2FA.fxml'.";
    }
}
