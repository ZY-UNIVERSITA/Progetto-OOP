package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;

/**
 * The DecryptKeyStoreConfig class is an extension of the
 * AuthenticationStepAbstract class.
 * It provides functionality to decrypt the key store configuration during the
 * authentication process.
 */
public class DecryptKeyStoreConfig extends AuthenticationStepAbstract {
    private final KeyStoreManager keyStoreManager;

    /**
     * Constructs a DecryptKeyStoreConfig object with the specified session manager
     * and key store manager.
     * 
     * @param sessionManager  the SessionManager to manage user sessions
     * @param keyStoreManager the KeyStoreManager to manage the key store
     */
    public DecryptKeyStoreConfig(final SessionManager sessionManager, final KeyStoreManager keyStoreManager) {
        super(sessionManager);
        this.keyStoreManager = keyStoreManager;
    }

    /**
     * Executes the step of decrypting the key store configuration.
     * Delegates the decryption process to the KeyStoreManager.
     */
    @Override
    public void executeStep() {
        this.keyStoreManager.decryptConfig();
    }
}
