package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStepAbstract;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;

/**
 * The GetKeyFromKeyStore class is an extension of the
 * AuthenticationStepAbstract class.
 * It provides functionality to retrieve keys from the key store during the
 * authentication process.
 */
public class GetKeyFromKeyStore extends AuthenticationStepAbstract {

    private final KeyStoreManager keyStoreManager;

    /**
     * Constructs a GetKeyFromKeyStore object with the specified session manager and
     * key store manager.
     * 
     * @param sessionManager  the SessionManager to manage user sessions
     * @param keyStoreManager the KeyStoreManager to manage the key store
     */
    public GetKeyFromKeyStore(final SessionManager sessionManager, final KeyStoreManager keyStoreManager) {
        super(sessionManager);

        this.keyStoreManager = keyStoreManager;
    }

    /**
     * Executes the step of retrieving keys from the key store.
     * Delegates the key retrieval process to the KeyStoreManager.
     */
    @Override
    public void executeStep() {
        this.keyStoreManager.getKeyStoreKeys();
    }
}
