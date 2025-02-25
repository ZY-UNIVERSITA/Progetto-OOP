/**
 * Sample Skeleton for 'Step3.fxml' Controller Class
 */

package com.zysn.passwordmanager.controller.registration;

import com.zysn.passwordmanager.model.enums.AlgorithmType;

/**
 * Controller for the third step of registration.
 * It extends {@link RegistrationConfigCreationController}.
 */
public class RegistrationStep3Controller extends RegistrationConfigCreationController {

    /**
     * Handles the current step of registration.
     * Sets the key store encryption configuration.
     * 
     * @return true if the step was handled successfully, false otherwise
     */
    @Override
    public boolean handleStep() {
        this.getData().setKeyStoreConfigEncryptionConfig(algorithmConfig);

        return super.handleStep();
    }

    /**
     * Initializes the data required for this controller.
     * Sets the algorithm type and initializes data from the superclass.
     */
    public void initializeData() {
        super.algorithmType = AlgorithmType.ENCRYPTION_ALGORITHM;

        super.initializeData();
    }
}
