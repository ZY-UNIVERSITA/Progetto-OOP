package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;

/**
 * This class handles the encryption of the service configuration as part of the
 * authentication process.
 * It extends the {@code AuthenticationStepAbstract} class and uses a
 * {@code ServiceCryptoConfigManager} to manage the encryption.
 */
public class EncryptServiceConfig extends AuthenticationStepAbstract {

    private final ServiceCryptoConfigManager serviceCryptoConfigManager;

    /**
     * Constructor that initializes the {@code EncryptServiceConfig} instance.
     * 
     * @param sessionManager             The session manager to use.
     * @param serviceCryptoConfigManager The service cryptographic configuration
     *                                   manager to use for managing the encryption.
     */
    public EncryptServiceConfig(final SessionManager sessionManager,
            final ServiceCryptoConfigManager serviceCryptoConfigManager) {
        super(sessionManager);
        this.serviceCryptoConfigManager = serviceCryptoConfigManager;
    }

    /**
     * Executes the step necessary to encrypt the service configuration.
     */
    @Override
    public void executeStep() {
        this.serviceCryptoConfigManager.encryptConfig();
    }
}
