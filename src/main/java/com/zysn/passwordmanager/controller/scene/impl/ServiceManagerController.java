package com.zysn.passwordmanager.controller.scene.impl;

import com.zysn.passwordmanager.controller.scene.api.SceneControllerBase;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.service.ServiceManager;

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


public class ServiceManagerController extends SceneControllerBase {
    
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


    public void setService (Service service) {
        this.service = service;
    }

    @FXML
    public void initialize() {
        if (service != null) {
            serviceNameField.setText(service.getName());
            usernameField.setText(service.getUsername());
            emailField.setText(service.getEmail());
            //passwordField.setText((String)service.getPassword());
            passwordVisibleField.setVisible(false);
            infoArea.setText(service.getInfo());

            lengthChoiceBox.getItems().addAll(12, 16, 20, 24, 28, 32);
        }

        this.serviceManager = ServiceManager.getInstance();
    }

    @FXML
    public void handleSaveAction(ActionEvent event) {

    }

    @FXML
    public void togglePassword(ActionEvent event) {

    }

    @FXML
    public void generatePassword(ActionEvent event) {

    }

    @FXML
    public void handleDeleteService(ActionEvent event) {
        
    }
}
