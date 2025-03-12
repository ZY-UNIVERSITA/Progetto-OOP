package com.zysn.passwordmanager.controller.main;

import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.service.ServiceManager;

/**
 * Controller for the main view of the password manager application.
 */
public class MainController extends ControllerAbstract<Stage, AccountManager> {
    
    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> servicesListView;

    @FXML
    private Button addServiceButton;

    @FXML
    private HBox buttonsHBox;

    @FXML
    private Button logoutButton;

    @FXML
    private Button backupButton;

    private ServiceManager serviceManager = ServiceManager.getInstance();
    private ObservableList<String> serviceNames = FXCollections.observableArrayList();

    /**
     * Initializes the controller after the FXML fields are loaded.
     */
    @FXML
    public void initialize() {
        serviceNames.setAll(serviceManager.getServices().stream().map(Service::getName).toList());
        servicesListView.setItems(serviceNames);

        servicesListView.setOnMouseClicked(event -> handleServiceClick());
    }

    /**
     * Handles the selection of a service from the list.
     */
    private void handleServiceClick() {
        int selectedIndex = servicesListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Service service = serviceManager.getServices().get(selectedIndex);
            if (service != null) {
                this.getNavigator().navigateTo("/layouts/service/ServiceManager.fxml", "Service", service);
            }
        }
            
    }  

    /**
     * Handles the search operation, filtering services based on input text.
     * @param event The triggered key event.
     */
    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText();
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (Service service : serviceManager.searchService(searchText)) {
            filteredList.add(service.getName());
        }
        servicesListView.setItems(filteredList);
    }

    /**
     * Handles navigation to the add service screen.
     * @param event the action event triggered by the add service button.
     */
    @FXML
    private void handleAddService(ActionEvent event) {
        this.getNavigator().navigateTo("/layouts/main/Add.fxml", "Add Service");
    }

    /**
     * Handles navigation to the backup manager screen.
     * @param event the action event triggered by the backup button.
     */
    @FXML
    private void handleBackup(ActionEvent event) {
        this.getNavigator().navigateTo("/layouts/backup/Backup.fxml", "Backup Manager");
    }

    /**
     * Handles the logout process, saving services and redirecting to login.
     * @param event the action event triggered by the logout button.
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        this.getData().getServiceManager().saveServices();

        this.getData().logout();
        
        this.getNavigator().navigateTo("/layouts/login/Login.fxml", "Login");
    }
}
