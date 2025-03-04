package com.zysn.passwordmanager.controller.backup;

import java.io.File;
import org.bouncycastle.util.Arrays;

import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.backup.BackupManager;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BackupController extends ControllerAbstract<Stage, AccountManager> {

    @FXML
    private Button backButton;

    @FXML
    private Button createButton;

    @FXML
    private Button restoreButton;

    @FXML
    private Label backupManagerLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField saltField;

    private ServiceManager serviceManager;
    private FileManager fileManager;
    private BackupManager backupManager;
    private FileChooser fileChooser;

    @Override
    public void initializeData() {
        this.serviceManager = ServiceManager.getInstance();
        this.fileManager = new DefaultFileManager(PathsConstant.BACKUP, ExtensionsConstant.ENC);
        this.backupManager = new BackupManager(fileManager, super.getData().getSessionManager());

        this.fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Backup file", "*".concat(ExtensionsConstant.ENC.getParameter())));
        fileChooser.setTitle("Select a file:");
    }

    @FXML
    public void saveBackup(ActionEvent event) {
        byte[] passwordAndSalt = backupManager.createBackup(serviceManager.getServices());

        char[] password = EncodingUtils.byteToCharConverter(Arrays.copyOf(passwordAndSalt, passwordAndSalt.length - 12));
        char[] salt = EncodingUtils.byteToBase64(Arrays.copyOfRange(passwordAndSalt, passwordAndSalt.length - 12, passwordAndSalt.length));
        String passwordText = new String(password);
        String saltText = new String(salt);
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Backup Created");
        alert.setHeaderText("Password and salt that have been generated.");
        alert.setContentText("Password: " + passwordText + "\nSalt: " + saltText);
        alert.showAndWait();
    }

    @FXML
    public void returnToMain(ActionEvent event) {
        this.getNavigator().navigateTo("/layouts/main/Main.fxml", "Main");
    }

    @FXML
    public void loadBackup(ActionEvent event) {
        char[] password = passwordField.getText().toCharArray();
        byte[] salt = saltField.getText().getBytes();

        File selectedFile = fileChooser.showOpenDialog(super.getNavigator().getView());

        backupManager.restoreBackup(selectedFile, getData(), password, salt);

        super.getData().logout();

        this.getNavigator().navigateTo("/layouts/login/Login.fxml", "Login");
    }
}
