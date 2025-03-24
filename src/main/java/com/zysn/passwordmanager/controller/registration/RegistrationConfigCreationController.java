/**
 * Sample Skeleton for 'Step2.fxml' Controller Class
 */

package com.zysn.passwordmanager.controller.registration;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.controller.scene.api.ControllerAbstract;
import com.zysn.passwordmanager.controller.scene.api.StepHandler;
import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.authentication.registration.impl.RegistrationConfigCreationHandler;
import com.zysn.passwordmanager.model.enums.AlgorithmType;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigList;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigListParameters;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.ResourcesFileManager;
import com.zysn.passwordmanager.view.registration.CreateCryptoChoosingPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Abstract controller for creating registration configurations.
 * 
 * <p>
 * This class extends {@link ControllerAbstract} with types {@link Pane} and
 * {@link CollectedUserData}.
 * It implements {@link StepHandler} with the same types.
 * </p>
 */
public abstract class RegistrationConfigCreationController extends ControllerAbstract<Pane, CollectedUserData>
        implements StepHandler<Pane, CollectedUserData> {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="comboBoxAlgorithm"
    protected ComboBox<String> comboBoxAlgorithm; // Value injected by FXMLLoader

    @FXML // fx:id="contentBox"
    protected GridPane contentBox; // Value injected by FXMLLoader

    private List<AlgorithmConfigList> algorithmLists;

    private CreateCryptoChoosingPane cryptoChoosingPane;

    protected AlgorithmType algorithmType;

    private RegistrationConfigCreationHandler registrationConfigCreationHandler;

    protected AlgorithmConfig algorithmConfig;

    /**
     * Handles the steps of the registration process.
     * 
     * @return true if the step is handled successfully
     */
    @Override
    public boolean handleStep() {
        this.createAlgorithmConfig();
        return true;
    };

    /**
     * Initializes the data required for the registration configuration process.
     */
    @Override
    public void initializeData() {
        this.cryptoChoosingPane = new CreateCryptoChoosingPane(contentBox);
        this.loadAlgorithmList();
        this.populateComboBox();
        this.algorithmConfig = new AlgorithmConfig(null, null);
    }

    /**
     * Creates the configuration for the chosen algorithm.
     */
    protected void createAlgorithmConfig() {
        final String algorithmName = this.comboBoxAlgorithm.getValue();
        final String algorithmType = this.algorithmType.getParameter();

        this.algorithmConfig.setAlgorithmName(algorithmName);
        this.algorithmConfig.setAlgorithmType(algorithmType);

        this.registrationConfigCreationHandler = new RegistrationConfigCreationHandler(this.algorithmConfig,
                contentBox);
        this.registrationConfigCreationHandler.handleStep();
    }

    /**
     * Handles the action event for the ComboBox selection.
     * 
     * @param event the action event
     */
    @FXML
    void handleComboBoxSelection(final ActionEvent event) {
        final String chosenAlgorithm = this.comboBoxAlgorithm.getValue();

        algorithmLists.forEach(
            algorithm -> {
                if (algorithm.algorithmName().equals(chosenAlgorithm)) {
                    
                }
            }
        );

        final Map<String, AlgorithmConfigListParameters> algorithmParameters = algorithmLists.stream()
                .filter(algorithm -> algorithm.algorithmName().equals(chosenAlgorithm))
                .findFirst()
                .get()
                .parameters();

        this.cryptoChoosingPane.createCryptoChoosingView(algorithmParameters);
    }

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     */
    @FXML
    void initialize() {
        assert comboBoxAlgorithm != null
                : "fx:id=\"comboBoxAlgorithm\" was not injected: check your FXML file 'Step2.fxml'.";
        assert contentBox != null : "fx:id=\"contentBox\" was not injected: check your FXML file 'Step2.fxml'.";
    }

    /**
     * Loads the list of algorithms from a configuration file.
     */
    private void loadAlgorithmList() {
        final FileManager fileManager = new ResourcesFileManager(PathsConstant.CONFIGURATIONS, ExtensionsConstant.JSON);

        final byte[] algorithmListByte = fileManager.loadData(algorithmType.getParameter());
        algorithmLists = EncodingUtils.deserializeData(algorithmListByte,
                new TypeReference<List<AlgorithmConfigList>>() {
                });
    }

    /**
     * Populates the ComboBox with the available algorithms.
     */
    private void populateComboBox() {
        algorithmLists.forEach(algorithm -> {
            final String algorithmName = algorithm.algorithmName();

            if (this.comboBoxAlgorithm.getValue() == null) {
                this.comboBoxAlgorithm.setValue(algorithmName);
                this.handleComboBoxSelection(null);
            }
            this.comboBoxAlgorithm.getItems().addAll(algorithmName);
        });
    }
}