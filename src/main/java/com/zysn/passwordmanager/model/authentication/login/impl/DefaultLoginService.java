package com.zysn.passwordmanager.model.authentication.login.impl;

import java.util.ArrayList;
import java.util.List;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.common.impl.DecryptKeyStoreConfig;
import com.zysn.passwordmanager.model.authentication.common.impl.DecryptServiceConfig;
import com.zysn.passwordmanager.model.authentication.common.impl.DeriveKeyFromPassword;
import com.zysn.passwordmanager.model.authentication.common.impl.DeriveMasterKey;
import com.zysn.passwordmanager.model.authentication.common.impl.DeriveServiceConfigKey;
import com.zysn.passwordmanager.model.authentication.common.impl.GetKeyFromKeyStore;
import com.zysn.passwordmanager.model.authentication.common.impl.InsertUserData;
import com.zysn.passwordmanager.model.authentication.common.impl.LoadKeyStore;
import com.zysn.passwordmanager.model.authentication.common.impl.LoadUserData;
import com.zysn.passwordmanager.model.authentication.common.impl.LoadUserPassword;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.authentication.login.api.LoginService;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.impl.DefaultServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

/**
 * The DefaultLoginService class implements the LoginService interface.
 * It provides the default implementation for the user login process, managing various authentication steps.
 */
public class DefaultLoginService implements LoginService {
    private final List<AuthenticationStep> authenticationStep;
    private final KeyStoreManager keyStoreManager;
    private final ServiceCryptoConfigManager passwordListConfigManager;

    private final CryptoManager cryptoManager;
    private final SessionManager sessionManager;
    private CollectedUserData collectedUserData;

    /**
     * Constructs a DefaultLoginService object with the specified session manager.
     * Initializes the necessary managers and prepares the list of authentication steps.
     * 
     * @param sessionManager the SessionManager to manage user sessions
     */
    public DefaultLoginService(final SessionManager sessionManager) {
        this.authenticationStep = new ArrayList<>();
        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);
        this.passwordListConfigManager = new DefaultServiceCryptoConfigManager(sessionManager);

        this.cryptoManager = new CryptoManager();
        this.sessionManager = sessionManager;
    }

    /**
     * Logs in the user with the provided collected user data.
     * Executes various authentication steps to complete the login process.
     * 
     * @param collectedUserData the CollectedUserData object containing user data for login
     */
    @Override
    public void login(final CollectedUserData collectedUserData) {
        this.collectedUserData = collectedUserData;

        this.loadUserPersonalData();
        this.deriveKeyFromPassword();
        this.loadKeyStore();
        this.decryptServiceConfig();
        this.deriveMasterKey();
        this.loadUserPassword();

        this.authenticationStep.forEach(AuthenticationStep::executeStep);

        this.authenticationStep.clear();
    }

    /**
     * Adds steps to load user personal data to the authentication steps.
     */
    private void loadUserPersonalData() {
        authenticationStep.add(new InsertUserData(sessionManager, collectedUserData));
        authenticationStep.add(new LoadUserData(sessionManager));
    }

    /**
     * Adds steps to derive a key from the user's password to the authentication steps.
     */
    private void deriveKeyFromPassword() {
        authenticationStep.add(new DeriveKeyFromPassword(sessionManager, cryptoManager));
    }

    /**
     * Adds steps to load the key store to the authentication steps.
     */
    private void loadKeyStore() {
        authenticationStep.add(new DecryptKeyStoreConfig(sessionManager, keyStoreManager));
        authenticationStep.add(new LoadKeyStore(sessionManager, keyStoreManager));
        authenticationStep.add(new GetKeyFromKeyStore(sessionManager, keyStoreManager));
    }

    /**
     * Adds steps to decrypt the service configuration to the authentication steps.
     */
    private void decryptServiceConfig() {
        authenticationStep.add(new DeriveServiceConfigKey(sessionManager, cryptoManager));
        authenticationStep.add(new DecryptServiceConfig(sessionManager, passwordListConfigManager));
    }

    /**
     * Adds a step to derive the master key to the authentication steps.
     */
    private void deriveMasterKey() {
        authenticationStep.add(new DeriveMasterKey(sessionManager, cryptoManager));
    }

    /**
     * Adds a step to load the user's password to the authentication steps.
     */
    private void loadUserPassword() {
        authenticationStep.add(new LoadUserPassword(sessionManager, false));
    }
}
