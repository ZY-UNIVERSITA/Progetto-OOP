package com.zysn.passwordmanager.controller.service;

import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.service.ServiceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

/**
 * Controller for managing services in the password manager.
 */
public class ServiceManagerController extends ControllerAbstract<Stage, AccountManager> {
    
    @FXML
    private Button saveButton;

    @FXML
    private TextField serviceNameField;

    @FXML
    private Label usernameLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Label passwordLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private ToggleButton visibilityButton;

    @FXML
    private Label infoLabel;

    @FXML
    private TextArea infoArea;

    @FXML
    private Label passwordGeneratorLabel;

    @FXML
    private ChoiceBox<Integer> lengthChoiceBox;

    @FXML
    private CheckBox lowercaseCheck;

    @FXML
    private CheckBox uppercaseCheck;

    @FXML
    private CheckBox digitsCheck;

    @FXML
    private CheckBox specialCheck;

    @FXML
    private TextField passwordGeneratedField;

    @FXML
    private Button generateButton;

    private Service service;
    private ServiceManager serviceManager = ServiceManager.getInstance();

    /**
     * Initializes the controller after the FXML fields are loaded.
     */
    @FXML
    public void initialize() {
        
    }

    /**
     * Handles the save action to update a service entry.
     * @param event the action event triggered by the save button.
     */
    @FXML
    public void handleSaveAction(ActionEvent event) {
        String newName = serviceNameField.getText();
        String newUsername = usernameField.getText();
        String newEmail = emailField.getText();
        byte[] newPassword = passwordVisibleField.getText().getBytes();
        String newInfo = infoArea.getText();

        AlgorithmConfig algorithmConfig = this.service.getEncryptionConfig();

        Service newService = new Service(newName, newUsername, newEmail, newPassword, algorithmConfig, newInfo);

        serviceManager.modifyService(service.getName(), newService);

        super.getNavigator().navigateTo("/layouts/main/Main.fxml", "Main");
    }

    /**
     * Toggles password visibility between plain text and masked format.
     * @param event The triggered action event.
     */
    @FXML
    public void togglePassword(ActionEvent event) {
        if (visibilityButton.isSelected()) {
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passwordField.setText(passwordVisibleField.getText());
            passwordField.setVisible(true);
            passwordVisibleField.setVisible(false);
        }
    }

    /**
     * Generates a random password based on selected criteria.
     * @param event the action event triggered by the generate button.
     */
    @FXML
    public void generatePassword(ActionEvent event) {
        int length = lengthChoiceBox.getValue();
        boolean lowercase = lowercaseCheck.isSelected();
        boolean uppercase = uppercaseCheck.isSelected();
        boolean digits = digitsCheck.isSelected();
        boolean special = specialCheck.isSelected();

        char[] password = serviceManager.generatePassword(length, special, digits, uppercase, lowercase);
        passwordField.setText(new String(password));
    }

    /**
     * Handles the deletion of a service.
     * @param event the action event triggered by the delete button.
     */
    @FXML
    public void handleDeleteService(ActionEvent event) {
        if (serviceManager.removeService(this.service.getName())) {
            showAlert(AlertType.INFORMATION, "Success", "The service has been successfully deleted!");
            this.getNavigator().navigateTo("/layouts/main/Main.fxml", "Main");
        }
        else {
            showAlert(AlertType.ERROR, "Failed","Failed to delete the service.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Initializes the controller with the provided service data.
     */
    @Override
    public <U> void initializeData(U optionalData) {
        if (optionalData instanceof Service) {
            this.service = (Service) optionalData;
        }

        this.serviceManager = ServiceManager.getInstance();

        if (service != null) {
            serviceNameField.setText(service.getName());
            usernameField.setText(service.getUsername());
            emailField.setText(service.getEmail());
            passwordField.setText(this.serviceManager.getDecryptedPassword(service));
            passwordVisibleField.setText(passwordField.getText());
            passwordVisibleField.setVisible(false);
            infoArea.setText(service.getInfo());

            lengthChoiceBox.getItems().addAll(12, 16, 20, 24, 28, 32);
        }
    } 
}
