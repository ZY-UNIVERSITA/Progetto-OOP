package com.zysn.passwordmanager.controller.scene.impl;

import com.zysn.passwordmanager.controller.scene.api.SceneControllerBase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;

import com.zysn.passwordmanager.model.service.Service;
import com.zysn.passwordmanager.model.service.ServiceManager;

public class MainController extends SceneControllerBase {
    
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

        for (Service s : serviceManager.getServices()) {
            serviceNames.add(s.getName());
        }
        servicesListView.setItems(serviceNames);

        servicesListView.setOnMouseClicked(event -> handleServiceClick());
    }

    private void handleServiceClick() {

        String selectedService = servicesListView.getSelectionModel().getSelectedItem();
            if (selectedService != null) {
                Service service = serviceManager.selectService(selectedService);
                if (service != null) {
                    this.getViewNavigator().navigateTo("/layouts/ServiceManager.fxml", "Service", service);
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

    }

    @FXML
    private void handleLogout(ActionEvent event) {
        
    }
}
