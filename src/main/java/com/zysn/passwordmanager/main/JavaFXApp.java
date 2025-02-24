package com.zysn.passwordmanager.main;

import com.zysn.passwordmanager.controller.navigation.api.GenericNavigator;
import com.zysn.passwordmanager.controller.navigation.impl.SceneNavigator;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultAccountManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.login.api.LoginService;
import com.zysn.passwordmanager.model.authentication.login.impl.DefaultLoginService;
import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationService;
import com.zysn.passwordmanager.model.authentication.registration.impl.DefaultRegistrationService;
import com.zysn.passwordmanager.model.service.ServiceManager;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Sample JavaFX application.
 */
public final class JavaFXApp extends Application {
    private AccountManager accountManager;
    private GenericNavigator<Stage, AccountManager> viewNavigator;

    private SessionManager sessionManager;
    private ServiceManager serviceManager;
    
    private LoginService loginService;
    private RegistrationService registrationService;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application.
     * @throws Exception if an error occurs during the application startup.
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        this.sessionManager = new DefaultSessionManager();

        this.loginService = new DefaultLoginService(this.sessionManager);
        this.registrationService = new DefaultRegistrationService(this.sessionManager);
        
        this.serviceManager = ServiceManager.getInstance();

        try {
            // Create managers and the view navigator
            this.accountManager = new DefaultAccountManager(this.sessionManager, this.serviceManager, this.registrationService, this.loginService);
            this.viewNavigator = new SceneNavigator(primaryStage, accountManager);

            // Load the FXML file representing the entry point of the app
            this.viewNavigator.navigateTo("/layouts/login/Login.fxml", "Login");
        } catch (Exception e) {
            System.err.println("An error occurred while trying to start the application.");
        }
    }
}
