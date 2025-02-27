package com.zysn.passwordmanager.controller.main;

import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.service.ServiceBuilder;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddController extends ControllerAbstract<Stage, AccountManager> {

    @FXML
    private TextField serviceNameField;
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private TextField passwordField;
    
    @FXML
    private TextArea infoArea;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button saveButton;

    private Service service;  
    private CryptoManager cryptoManager;
    private ServiceManager serviceManager = ServiceManager.getInstance();

    @Override
    public void initializeData() {
        this.cryptoManager = new CryptoManager();
    }

    @FXML
    private void handleSaveAction() {
        String serviceName = serviceNameField.getText();
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String info = infoArea.getText();

        byte[] iv = CryptoUtils.generateSalt(12);
        AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", iv, null);
        
        if (serviceName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error", "All required fields must be filled in!");
            return;
        }
        
        service = new ServiceBuilder(super.getData().getSessionManager().getUserAccount(), cryptoManager)
                .setName(serviceName)
                .setUsername(username)
                .setEmail(email)
                .setEncryptionConfig(algorithmConfig)
                .setPassword(password.getBytes())
                .setInfo(info)
                .build();
        
        if (serviceManager.addService(service)) {
            showAlert(AlertType.INFORMATION, "Success", "The service has been successfully saved!");

            this.getNavigator().navigateTo("/layouts/main/Main.fxml", "Main");
        }
        else {
            showAlert(AlertType.ERROR, "Failed","Failed to save the service.");
        }
    }
    
    @FXML
    private void handleCancelAction() {
        serviceNameField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        infoArea.clear();
        
        this.getNavigator().navigateTo("/layouts/main/Main.fxml", "Add Service");
    }
    

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
