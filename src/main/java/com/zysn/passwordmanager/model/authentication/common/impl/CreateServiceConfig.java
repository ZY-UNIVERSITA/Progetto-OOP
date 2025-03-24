package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;

/**
 * This class handles the creation of the service configuration as part of the authentication process.
 * It extends the {@code AuthenticationStepAbstract} class and uses a {@code ServiceCryptoConfigManager} to manage the service configuration.
 */
public class CreateServiceConfig extends AuthenticationStepAbstract {

    private final ServiceCryptoConfigManager serviceCryptoConfigManager;

    /**
     * Constructor that initializes the {@code CreateServiceConfig} instance.
     * 
     * @param sessionManager The session manager to use.
     * @param serviceCryptoConfigManager The service cryptographic configuration manager to use for managing the service configuration.
     */
    public CreateServiceConfig(final SessionManager sessionManager, final ServiceCryptoConfigManager serviceCryptoConfigManager) {
        super(sessionManager);
        this.serviceCryptoConfigManager = serviceCryptoConfigManager;
    }

    /**
     * Executes the step necessary to create the service configuration.
     */
    @Override
    public void executeStep() {
        this.serviceCryptoConfigManager.createServiceConfig();
    }   
}
