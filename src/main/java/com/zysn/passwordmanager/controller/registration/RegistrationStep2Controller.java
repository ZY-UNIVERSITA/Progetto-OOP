/**
 * Sample Skeleton for 'Step2.fxml' Controller Class
 */

package com.zysn.passwordmanager.controller.registration;

import com.zysn.passwordmanager.model.enums.AlgorithmType;

/**
 * Controller for the second step of registration.
 * It extends {@link RegistrationConfigCreationController}.
 */
public class RegistrationStep2Controller extends RegistrationConfigCreationController {

    /**
     * Handles the current step of registration.
     * Sets the password derivation configuration.
     * 
     * @return true if the step was handled successfully, false otherwise
     */
    @Override
    public boolean handleStep() {
        this.getData().setPasswordDerivationConfig(algorithmConfig);

        return super.handleStep();
    }

    /**
     * Initializes the data required for this controller.
     * Sets the algorithm type and initializes data from the superclass.
     */
    public void initializeData() {
        super.algorithmType = AlgorithmType.KEY_DERIVATION_ALGORITHM;

        super.initializeData();
    }
}
