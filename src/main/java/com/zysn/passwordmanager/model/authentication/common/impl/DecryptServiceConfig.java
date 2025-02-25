package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;

/**
 * The DecryptServiceConfig class is an extension of the
 * AuthenticationStepAbstract class.
 * It provides functionality to decrypt the service configuration during the
 * authentication process.
 */
public class DecryptServiceConfig extends AuthenticationStepAbstract {
    private final ServiceCryptoConfigManager serviceCryptoConfigManager;

    /**
     * Constructs a DecryptServiceConfig object with the specified session manager
     * and service crypto config manager.
     * 
     * @param sessionManager            the SessionManager to manage user sessions
     * @param serviceCryptoConfigManager the ServiceCryptoConfigManager to manage the
     *                                  service crypto configuration
     */
    public DecryptServiceConfig(final SessionManager sessionManager,
            final ServiceCryptoConfigManager serviceCryptoConfigManager) {
        super(sessionManager);

        this.serviceCryptoConfigManager = serviceCryptoConfigManager;
    }

    /**
     * Executes the step of decrypting the service configuration.
     * Delegates the decryption process to the ServiceCryptoConfigManager.
     */
    @Override
    public void executeStep() {
        this.serviceCryptoConfigManager.decryptConfig();
    }
}
