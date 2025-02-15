package com.zysn.passwordmanager.controller.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

import com.zysn.passwordmanager.controller.scene.api.SceneController;
import com.zysn.passwordmanager.model.account.manager.AccountManager;
import com.zysn.passwordmanager.model.service.Service;

/**
 * The ViewNavigator class is responsible for managing the navigation between different scenes in the application.
 */
public class ViewNavigator {
    private Stage stage;
    private AccountManager accountManager;

    /**
     * Constructs a new ViewNavigator with the specified stage and account manager.
     *
     * @param stage the primary stage of the application
     * @param accountManager the account manager to be used in the scenes
     */
    public ViewNavigator(Stage stage, AccountManager accountManager) {
        this.stage = stage;
        this.accountManager = accountManager;
    }

    /**
     * Navigates to a new scene specified by the path to the file and the scene title.
     *
     * @param pathToFile the path to the FXML file of the new scene
     * @param sceneTitle the title of the new scene
     * @throws IllegalArgumentException if the pathToFile or sceneTitle is null
     */
    public void navigateTo(Path pathToFile, String sceneTitle) {
        if (pathToFile == null || sceneTitle == null) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }

        navigateTo(pathToFile.toString(), sceneTitle);
    }

    /**
     * Navigates to a new scene specified by the path to the file (as a string) and the scene title.
     *
     * @param pathToFile the path to the FXML file of the new scene (as a string)
     * @param sceneTitle the title of the new scene
     * @throws IllegalArgumentException if the pathToFile or sceneTitle is null
     */
    public void navigateTo(String pathToFile, String sceneTitle) {
        if (pathToFile == null || sceneTitle == null) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToFile));
            Parent root = loader.load();

            // Pass the AccountManager to the controller
            if (loader.getController() instanceof SceneController) {
                SceneController controller = loader.getController();
                
                controller.setViewNavigator(this);
                controller.setAccountManager(accountManager);
            }

            stage.setScene(new Scene(root));
            stage.setTitle(sceneTitle);
            stage.show();
        } catch (IOException e) {
            System.err.println("An error occurred while trying to change scene.");
        }
    }

    /**
     * Navigates to a new scene specified by the path to the FXML file, the scene title, and a Service object to the target scene's controller.
     *
     * @param pathToFile the path to the FXML file of the new scene (as a string)
     * @param sceneTitle the title of the new scene
     * @param service the Service object to be passed to the target controller
     * @throws IllegalArgumentException if the pathToFile or sceneTitle is null
     */
    public void navigateTo(String pathToFile, String sceneTitle, Service service) {
        if (pathToFile == null || sceneTitle == null) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pathToFile));
            Parent root = loader.load();

            if (loader.getController() instanceof SceneController) {
                SceneController controller = loader.getController();
                controller.setViewNavigator(this);
                controller.setAccountManager(accountManager);
                
                // Pass the Service to ServiceManagerView
                if (controller instanceof ServiceManagerController) {
                    ServiceManagerController serviceController = (ServiceManagerController) controller;
                    serviceController.setService(service);
                }
            }

            stage.setScene(new Scene(root));
            stage.setTitle(sceneTitle);
            stage.show();
        } catch (IOException e) {
            System.err.println("An error occurred while trying to change scene.");
        }
    }
}
