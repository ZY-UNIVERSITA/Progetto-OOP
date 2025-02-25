package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;

/**
 * This class handles the encryption of the key store configuration as part of
 * the authentication process.
 * It extends the {@code AuthenticationStepAbstract} class and uses a
 * {@code KeyStoreManager} to manage the encryption.
 */
public class EncryptKeyStoreConfig extends AuthenticationStepAbstract {

    private final KeyStoreManager keyStoreManager;

    /**
     * Constructor that initializes the {@code EncryptKeyStoreConfig} instance.
     * 
     * @param sessionManager  The session manager to use.
     * @param keyStoreManager The key store manager to use for managing the
     *                        encryption.
     */
    public EncryptKeyStoreConfig(final SessionManager sessionManager, final KeyStoreManager keyStoreManager) {
        super(sessionManager);
        this.keyStoreManager = keyStoreManager;
    }

    /**
     * Executes the step necessary to encrypt the key store configuration.
     */
    @Override
    public void executeStep() {
        this.keyStoreManager.encryptConfig();
    }
}
