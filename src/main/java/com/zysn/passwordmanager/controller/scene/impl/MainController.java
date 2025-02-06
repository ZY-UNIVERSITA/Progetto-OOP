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
    private ListView<Service> servicesListView;

    @FXML
    private Button addServiceButton;

    private ObservableList<Service> services = FXCollections.observableArrayList();


    @FXML
    public void initialize() {

        ServiceManager serviceManager = ServiceManager.getInstance();
        services.addAll(serviceManager.getServices());

        servicesListView.setItems(services);

        servicesListView.setCellFactory(new Callback<ListView<Service>, ListCell<Service>>() {
            @Override
            public ListCell<Service> call(ListView<Service> param) {
                return new ListCell<Service>() {
                    @Override
                    protected void updateItem(Service item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });

        servicesListView.setOnMouseClicked(event -> {
            Service selectedService = servicesListView.getSelectionModel().getSelectedItem();
            if (selectedService != null) {
                handleServiceClick(selectedService);
            }
        });
    }

    private void handleServiceClick(Service service) {
        this.getViewNavigator().navigateTo("/layouts/ServiceManager.fxml", "Service");
    }

    @FXML
    private void handleSearch(KeyEvent event) {

        String searchText = searchField.getText().toLowerCase();
        ObservableList<Service> filteredList = FXCollections.observableArrayList();

        filteredList = ServiceManager.searchService(searchText);

        servicesListView.setItems(filteredList);
    }

    @FXML
    private void handleAddService(ActionEvent event) {

    }
}
