package com.zysn.passwordmanager.model.authentication.registration.impl;

import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationStepHandler;
import com.zysn.passwordmanager.model.enums.AesAlgorithm;
import com.zysn.passwordmanager.model.enums.AlgorithmType;
import com.zysn.passwordmanager.model.enums.CryptoLength;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

/**
 * Handles the creation and configuration of the registration algorithm.
 * This class implements the {@link RegistrationStepHandler} interface.
 */
public class RegistrationConfigCreationHandler implements RegistrationStepHandler {

    private final AlgorithmConfig algorithmConfig;
    private final GridPane pane;

    /**
     * Constructs a new RegistrationConfigCreationHandler.
     *
     * @param algorithmConfig The algorithm configuration object.
     * @param pane            The GridPane containing UI components for algorithm
     *                        configuration.
     */
    public RegistrationConfigCreationHandler(final AlgorithmConfig algorithmConfig, final GridPane pane) {
        this.algorithmConfig = algorithmConfig;
        this.pane = pane;
    }

    /**
     * Handles the current registration step.
     * Generates a salt and populates the algorithm derivation configuration.
     */
    @Override
    public void handleStep() {
        final byte[] salt = this.createSalt();
        algorithmConfig.setSalt(salt);
        this.populateAlgorithmDerivationConfig();
    }

    /**
     * Creates a salt based on the algorithm type.
     *
     * @return A byte array representing the generated salt.
     */
    private byte[] createSalt() {
        if (algorithmConfig.getAlgorithmType().equals(AlgorithmType.ENCRYPTION_ALGORITHM.getParameter())) {
            return CryptoUtils.generateSalt(CryptoLength.OPTIMAL_PASSWORD_LENGTH.getParameter());
        } else {
            final int ivLength = Integer.parseInt(AesAlgorithm.GCM_IV_LENGTH.getParameter());
            return CryptoUtils.generateSalt(ivLength);
        }
    }

    /**
     * Populates the algorithm derivation configuration based on UI component
     * values.
     */
    private void populateAlgorithmDerivationConfig() {
        pane.getChildren().stream()
                .filter(node -> node.getId() != null && !node.getId().isEmpty())
                .forEach(node -> {
                    final String nodeId = node.getId();
                    String nodeValue = null;

                    if (node instanceof Spinner<?>) {
                        nodeValue = ((Spinner<?>) node).getValue().toString();
                    } else if (node instanceof ComboBox<?>) {
                        nodeValue = ((ComboBox<?>) node).getValue().toString();
                    }

                    algorithmConfig.addNewParameter(nodeId, nodeValue);
                });
    }
}
