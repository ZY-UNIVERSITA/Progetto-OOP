package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;

/**
 * This class handles the saving of the key store as part of the authentication
 * process.
 * It extends the {@code AuthenticationStepAbstract} class and uses a
 * {@code KeyStoreManager} to manage the saving process.
 */
public class SaveKeyStore extends AuthenticationStepAbstract {

    private final KeyStoreManager keyStoreManager;

    /**
     * Constructor that initializes the {@code SaveKeyStore} instance.
     * 
     * @param sessionManager  The session manager to use.
     * @param keyStoreManager The key store manager to use for managing the key
     *                        store saving process.
     */
    public SaveKeyStore(final SessionManager sessionManager, final KeyStoreManager keyStoreManager) {
        super(sessionManager);
        this.keyStoreManager = keyStoreManager;
    }

    /**
     * Executes the step necessary to save the key store.
     */
    @Override
    public void executeStep() {
        this.keyStoreManager.saveKeyStore();
    }
}
