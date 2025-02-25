package com.zysn.passwordmanager.controller.service;

import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;


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
    private ServiceManager serviceManager;

    @FXML
    public void initialize() {
        
    }

    @FXML
    public void handleSaveAction(ActionEvent event) {
        String newName = serviceNameField.getText();
        String newUsername = usernameField.getText();
        String newEmail = emailField.getText();
        byte[] newPassword = passwordField.getText().getBytes();
        String newInfo = infoArea.getText();

        byte[] salt = CryptoUtils.generateSalt(12);
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", salt, null);

        Service newService = new Service(newName, newUsername, newEmail, newPassword, algorithmConfig, newInfo);

        serviceManager.modifyService(service.getName(), newService);
    }

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

    @FXML
    public void generatePassword(ActionEvent event) {
        int length = lengthChoiceBox.getValue();
        boolean lowercase = lowercaseCheck.isSelected();
        boolean uppercase = uppercaseCheck.isSelected();
        boolean digits = digitsCheck.isSelected();
        boolean special = specialCheck.isSelected();

        char[] password = serviceManager.generatePassword(length, special, digits, uppercase, lowercase);
        //passwordGeneratedField.setText((String)password);
        //passwordField.setText(password);
    }

    @FXML
    public void handleDeleteService(ActionEvent event) {
        serviceManager.removeService(this.service.getName());
    }

    @Override
    public <U> void initializeData(U optionalData) {
        if (optionalData instanceof Service) {
            this.service = (Service) optionalData;
        }

        if (service != null) {
            serviceNameField.setText(service.getName());
            usernameField.setText(service.getUsername());
            emailField.setText(service.getEmail());
            passwordField.setText(String.valueOf(EncodingUtils.byteToCharConverter(service.getPassword())));
            passwordVisibleField.setVisible(false);
            infoArea.setText(service.getInfo());

            lengthChoiceBox.getItems().addAll(12, 16, 20, 24, 28, 32);
        }

        this.serviceManager = ServiceManager.getInstance();
    }

    
}
