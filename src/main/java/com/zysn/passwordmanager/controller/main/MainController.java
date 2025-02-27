package com.zysn.passwordmanager.controller.main;

import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.service.ServiceManager;

public class MainController extends ControllerAbstract<Stage, AccountManager> {
    
    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> servicesListView;

    @FXML
    private Button addServiceButton;

    @FXML
    private Button logoutButton;

    private ServiceManager serviceManager = ServiceManager.getInstance();
    private ObservableList<String> serviceNames = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        serviceNames.setAll(serviceManager.getServices().stream().map(Service::getName).toList());
        servicesListView.setItems(serviceNames);

        servicesListView.setOnMouseClicked(event -> handleServiceClick());
    }

    private void handleServiceClick() {
        String selectedService = servicesListView.getSelectionModel().getSelectedItem();
            if (selectedService != null) {
                Service service = serviceManager.selectService(selectedService);
                if (service != null) {
                    this.getNavigator().navigateTo("/layouts/service/ServiceManager.fxml", "Service", service);
                }
            }
    }   

    @FXML
    private void handleSearch(KeyEvent event) {
        String searchText = searchField.getText();
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (Service service : serviceManager.searchService(searchText)) {
            filteredList.add(service.getName());
        }
        servicesListView.setItems(filteredList);
    }

    @FXML
    private void handleAddService(ActionEvent event) {
        this.getNavigator().navigateTo("/layouts/main/Add.fxml", "Add Service");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        this.getData().getServiceManager().saveServices();

        this.getData().logout();
        
        this.getNavigator().navigateTo("/layouts/login/Login.fxml", "Login");
    }
}
