package com.zysn.passwordmanager.controller.login;

import java.net.URL;
import java.util.ResourceBundle;

import com.zysn.passwordmanager.controller.scene.api.SceneControllerBase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Sample Skeleton for 'Login.fxml' Controller Class
 */
public class LoginController extends SceneControllerBase {
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

    @FXML
    void handleLogin(ActionEvent event) {

    }

    @FXML
    void handleRegister(ActionEvent event) {
        this.getViewNavigator().navigateTo("/layouts/registration/Register.fxml", "Registration");
    }

    @FXML
    void handleRestore(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert mainVBox != null : "fx:id=\"mainVBox\" was not injected: check your FXML file 'Login.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'Login.fxml'.";
        assert usernameField != null : "fx:id=\"usernameField\" was not injected: check your FXML file 'Login.fxml'.";
    }

}
