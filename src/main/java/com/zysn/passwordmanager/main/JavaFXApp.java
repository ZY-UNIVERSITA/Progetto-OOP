package com.zysn.passwordmanager.main;

import com.zysn.passwordmanager.controller.navigation.ViewNavigator;
import com.zysn.passwordmanager.model.account.manager.AccountManager;
import com.zysn.passwordmanager.model.account.manager.SessionManager;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Sample JavaFX application.
 */
public final class JavaFXApp extends Application {
    private AccountManager accountManager;
    private ViewNavigator viewNavigator;

    private CryptoManager cryptoManager;
    private FileManager fileManager;
    private SessionManager sessionManager;
    private ServiceManager serviceManager;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application.
     * @throws Exception if an error occurs during the application startup.
     */
    @Override
    public void start(final Stage primaryStage) throws Exception {
        this.cryptoManager = new CryptoManager();
        this.fileManager = new FileManager();
        this.sessionManager = new SessionManager();
        this.serviceManager = ServiceManager.getInstance();

        try {
            // Create managers and the view navigator
            this.accountManager = new AccountManager(cryptoManager, fileManager, sessionManager, serviceManager);
            this.viewNavigator = new ViewNavigator(primaryStage, accountManager);

            // Load the FXML file representing the entry point of the app
            this.viewNavigator.navigateTo("/layouts/login/Login.fxml", "Login");
        } catch (Exception e) {
            System.err.println("An error occurred while trying to start the application.");
        }
    }
}
