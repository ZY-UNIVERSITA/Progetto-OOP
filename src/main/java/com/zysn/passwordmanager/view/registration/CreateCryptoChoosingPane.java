package com.zysn.passwordmanager.view.registration;

import java.util.Map;
import java.util.Optional;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigListParameters;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

/**
 * Pane for choosing cryptographic algorithms.
 * 
 * <p>
 * This class utilizes {@link GridPane}, {@link AlgorithmConfigListParameters},
 * {@link Label}, {@link Spinner},
 * {@link ComboBox}, and other relevant classes.
 * </p>
 */
public class CreateCryptoChoosingPane {
    private int row;

    private final GridPane pane;

    /**
     * Constructor for the pane.
     * 
     * @param pane the {@link GridPane} to be used
     */
    public CreateCryptoChoosingPane(final GridPane pane) {
        this.row = 0;
        this.pane = pane;
    }

    /**
     * Creates the view for choosing cryptographic algorithms.
     * 
     * @param parameter a {@link Map} containing algorithm parameters
     */
    public void createCryptoChoosingView(final Map<String, AlgorithmConfigListParameters> parameter) {
        this.resetGrid();
        parameter.forEach(this::createAlgorithmConfiguration);
    }

    /**
     * Resets the grid pane to its initial state.
     */
    private void resetGrid() {
        this.row = 0;
        pane.getChildren().clear();
    }

    /**
     * Creates the configuration for an algorithm.
     * 
     * @param parameterName the name of the parameter
     * @param parameter     the {@link AlgorithmConfigListParameters} object containing
     *                      parameter details
     */
    private void createAlgorithmConfiguration(final String parameterName, final AlgorithmConfigListParameters parameter) {
        final Label label = new Label(parameter.getName());
        pane.add(label, 0, row);

        final String parameterType = parameter.getType();

        Control control = null;

        if (parameterType.equals("numerical")) {
            final int defaultValue = Integer.valueOf(parameter.getDefault_value());
            final int minValue = parameter.getMin_value();
            final int maxValue = Optional.ofNullable(parameter.getMax_value()).orElse(defaultValue * 10);

            final Spinner<Integer> spinner = new Spinner<>(minValue, maxValue, defaultValue);
            spinner.setEditable(true);

            control = spinner;
        } else if (parameterType.equals("multiple_choose")) {
            final ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().setAll(parameter.getOptions());
            comboBox.setValue(parameter.getDefault_value());

            control = comboBox;
        }

        control.setId(parameterName);
        pane.add(control, 1, row);
        row++;
    }
}
