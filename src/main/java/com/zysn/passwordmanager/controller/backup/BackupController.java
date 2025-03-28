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

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for managing backup of services in the password manager.
 */
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

    /**
     * Initializes the controller with the provided service data.
     */
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

    /**
     * Handles the save action to create a backup.
     * @param event the action event triggered by the create button.
     */
    @FXML
    public void saveBackup(ActionEvent event) {
        byte[] passwordAndSalt = backupManager.createBackup(serviceManager.getServices());

        char[] password = EncodingUtils.byteToCharConverter(Arrays.copyOf(passwordAndSalt, passwordAndSalt.length - 16));
        char[] salt = EncodingUtils.byteToBase64(Arrays.copyOfRange(passwordAndSalt, passwordAndSalt.length - 16, passwordAndSalt.length));
        String passwordText = new String(password);
        String saltText = new String(salt);
        
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Backup Created");
        alert.setHeaderText("Password and salt that have been generated.");
        alert.setContentText("Password: " + passwordText + "\nSalt: " + saltText);

        Button copyButton = new Button("Copy");
        copyButton.setOnAction(e -> {
            copyToClipboard("Password: " + passwordText + "\nSalt: " + saltText);
            alert.setHeaderText("Copied to clipboard!");
        });

        VBox dialogPane = new VBox();
        dialogPane.getChildren().addAll(new Label("Password: " + passwordText + "\nSalt: " + saltText), copyButton);
        alert.getDialogPane().setContent(dialogPane);

        alert.showAndWait();
    }

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
    }

    /**
     * Handles the cancel of an operation.
     * @param event the action event triggered by the back button.
     */
    @FXML
    public void returnToMain(ActionEvent event) {
        this.getNavigator().navigateTo("/layouts/main/Main.fxml", "Main");
    }

    /**
     * Handles the restore action to restore a backup.
     * @param event the action event triggered by the restore button.
     */
    @FXML
    public void loadBackup(ActionEvent event) {
        char[] password = passwordField.getText().toCharArray();
        char[] salt = saltField.getText().toCharArray();
        byte[] saltBytes = EncodingUtils.base64ToByte(salt);

        File selectedFile = fileChooser.showOpenDialog(super.getNavigator().getView());

        backupManager.restoreBackup(selectedFile, getData(), password, saltBytes);

        super.getData().logout();

        this.getNavigator().navigateTo("/layouts/login/Login.fxml", "Login");
    }
}
