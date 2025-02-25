package com.zysn.passwordmanager.model.authentication.common.impl;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationCollectingStepAbstract;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;

/**
 * InsertUserConfig is a step in the authentication process that collects and sets
 * the user's configuration data such as password derivation and keystore encryption configurations.
 */
public class InsertUserConfig extends AuthenticationCollectingStepAbstract {
    
    /**
     * Constructor for InsertUserConfig.
     *
     * @param sessionManager   the session manager to manage user sessions.
     * @param collectedUserData the collected user data.
     */
    public InsertUserConfig(final SessionManager sessionManager, final CollectedUserData collectedUserData) {
        super(sessionManager, collectedUserData);
    }

    /**
     * Executes the step to set the user's configuration data including password derivation
     * and keystore encryption configurations.
     */
    @Override
    public void executeStep() {
        this.setPasswordDerivationConfig();
        this.setKeyStoreConfigEncryptionConfig();
    }

    /**
     * Sets the password derivation configuration in the session manager.
     */
    private void setPasswordDerivationConfig() {
        final AlgorithmConfig algorithmConfig = super.getCollectedUserData().getPasswordDerivationConfig();
        super.getSessionManager().getUserAuthInfo().setPasswordDerivedKeyConfig(algorithmConfig);
    }

    /**
     * Sets the keystore encryption configuration in the session manager.
     */
    private void setKeyStoreConfigEncryptionConfig() {
        final AlgorithmConfig algorithmConfig = super.getCollectedUserData().getKeyStoreConfigEncryptionConfig();
        super.getSessionManager().getUserAuthInfo().setKeyStoreEncryptionConfig(algorithmConfig);
    }
}

