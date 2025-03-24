/**
 * Sample Skeleton for 'MainRegistrationPage.fxml' Controller Class
 */

package com.zysn.passwordmanager.controller.registration;

import java.net.URL;
import java.util.ResourceBundle;

import com.zysn.passwordmanager.controller.navigation.api.GenericNavigator;
import com.zysn.passwordmanager.controller.navigation.impl.StepNavigator;
import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.controller.scene.api.GenericController;
import com.zysn.passwordmanager.controller.scene.api.StepHandler;
import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main controller for the registration process.
 * 
 * <p>
 * This class extends {@link ControllerAbstract} with types {@link Stage} and
 * {@link AccountManager}.
 * </p>
 */
public class MainRegistrationController extends ControllerAbstract<Stage, AccountManager> {

    private static final int MAX_STEPS = 5;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="backButton"
    private Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="contentPane"
    private StackPane contentPane; // Value injected by FXMLLoader

    @FXML // fx:id="nextButton"
    private Button nextButton; // Value injected by FXMLLoader

    @FXML // fx:id="stepLabel"
    private Label stepLabel; // Value injected by FXMLLoader

    private int currentStep;
    private CollectedUserData collectedUserData;
    private GenericNavigator<Pane, CollectedUserData> navigator;
    private StepHandler<Pane, CollectedUserData> controller;

    /**
     * Initializes the registration data and setup initial state.
     */
    @Override
    public void initializeData() {
        this.currentStep = 1;
        this.collectedUserData = new CollectedUserData();
        this.navigator = new StepNavigator<>(contentPane, collectedUserData);
        this.updateStepView();
    }

    /**
     * Handles the action event for the back button.
     *
     * @param event the action event
     */
    @FXML
    void handleBack(final ActionEvent event) {
        if (currentStep > 1) {
            currentStep--;
            updateStepView();
        }
    }

    /**
     * Handles the action event for the next button.
     *
     * @param event the action event
     */
    @FXML
    void handleNext(final ActionEvent event) {
        if (!this.controller.handleStep()) {
            return;
        }

        if (currentStep < MAX_STEPS) {
            handleCurrentStep();
        } else {
            navigateToMain();
        }
    }

    /**
     * Called by the FXMLLoader when initialization is complete.
     */
    @FXML
    void initialize() {
        assert backButton != null
                : "fx:id=\"backButton\" was not injected: check your FXML file 'MainRegistrationPage.fxml'.";
        assert contentPane != null
                : "fx:id=\"contentPane\" was not injected: check your FXML file 'MainRegistrationPage.fxml'.";
        assert nextButton != null
                : "fx:id=\"nextButton\" was not injected: check your FXML file 'MainRegistrationPage.fxml'.";
        assert stepLabel != null
                : "fx:id=\"stepLabel\" was not injected: check your FXML file 'MainRegistrationPage.fxml'.";
    }

    /**
     * Handles specific actions for the current step.
     */
    private void handleCurrentStep() {
        if (currentStep == 4) {
            super.getData().register(collectedUserData);

            if (!this.collectedUserData.isEnabled2FA()) {
                navigateToMain();
            }
        } else {
            currentStep++;
            updateStepView();
        }
    }
    
    /**
     * Navigates to the main page.
     */
    private void navigateToMain() {
        this.getNavigator().navigateTo("/layouts/main/Main.fxml", "Main");
        this.collectedUserData.destroy();
    }

    /**
     * Updates the view to reflect the current step.
     */
    private void updateStepView() {
        final String path = "/layouts/registration/Step" + currentStep + ".fxml";
        final GenericController<Pane, CollectedUserData> tempNavigator = this.navigator.navigateTo(path,
                "Step " + currentStep);

        if (tempNavigator instanceof StepHandler<Pane, CollectedUserData>) {
            this.controller = (StepHandler<Pane, CollectedUserData>) tempNavigator;
        }

        stepLabel.setText("Step " + currentStep + " of " + MAX_STEPS);
    }
}
