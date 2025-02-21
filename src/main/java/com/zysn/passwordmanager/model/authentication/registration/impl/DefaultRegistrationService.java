package com.zysn.passwordmanager.model.authentication.registration.impl;

import java.util.ArrayList;
import java.util.List;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.common.impl.CloseKeyStore;
import com.zysn.passwordmanager.model.authentication.common.impl.CreateKeyStore;
import com.zysn.passwordmanager.model.authentication.common.impl.CreatePasswordListConfig;
import com.zysn.passwordmanager.model.authentication.common.impl.DeriveKeyFromPassword;
import com.zysn.passwordmanager.model.authentication.common.impl.DeriveMasterKey;
import com.zysn.passwordmanager.model.authentication.common.impl.DerivePasswordListKey;
import com.zysn.passwordmanager.model.authentication.common.impl.EncryptKeyStoreConfig;
import com.zysn.passwordmanager.model.authentication.common.impl.EncryptPasswordListConfig;
import com.zysn.passwordmanager.model.authentication.common.impl.InsertUserConfig;
import com.zysn.passwordmanager.model.authentication.common.impl.InsertUserData;
import com.zysn.passwordmanager.model.authentication.common.impl.SaveKeyStore;
import com.zysn.passwordmanager.model.authentication.common.impl.SaveUserData;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.impl.DefaultServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationService;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

/**
 * DefaultRegistrationService is a concrete implementation of the
 * RegistrationService interface.
 * It handles the complete process of registering user data, including
 * processing, storage, encryption,
 * and authentication steps.
 */
public class DefaultRegistrationService implements RegistrationService {

    private final List<AuthenticationStep> authenticationStep;
    private final KeyStoreManager keyStoreManager;
    private final ServiceCryptoConfigManager serviceCryptoConfigManager;
    private final CryptoManager cryptoManager;
    private final SessionManager sessionManager;
    private CollectedUserData collectedUserData;

    /**
     * Constructs a DefaultRegistrationService with the specified session manager.
     *
     * @param sessionManager The session manager to be used by this service.
     */
    public DefaultRegistrationService(final SessionManager sessionManager) {
        this.authenticationStep = new ArrayList<>();
        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);
        this.serviceCryptoConfigManager = new DefaultServiceCryptoConfigManager(sessionManager);
        this.cryptoManager = new CryptoManager();
        this.sessionManager = sessionManager;
    }

    /**
     * Registers the provided user data.
     * This method processes the collected user data by executing various
     * authentication and encryption steps.
     *
     * @param collectedUserData The data collected from the user that needs to be
     *                          registered.
     */
    @Override
    public void register(final CollectedUserData collectedUserData) {
        this.collectedUserData = collectedUserData;

        this.insertingData();
        this.getKeyFromPassword();
        this.createKeyStore();
        this.createServiceConfig();
        this.encryptConfigData();
        this.saveData();
        this.createMasterKey();

        this.authenticationStep.forEach(AuthenticationStep::executeStep);

        this.collectedUserData.setTotpKey(this.sessionManager.getUserAuthKey().getTotpKey());
    }

    /**
     * Adds steps for inserting user data into the authentication process.
     */
    private void insertingData() {
        authenticationStep.add(new InsertUserData(sessionManager, collectedUserData));
        authenticationStep.add(new InsertUserConfig(sessionManager, collectedUserData));
    }

    /**
     * Adds steps for deriving a key from the user's password in the authentication
     * process.
     */
    private void getKeyFromPassword() {
        authenticationStep.add(new DeriveKeyFromPassword(sessionManager, cryptoManager));
    }

    /**
     * Adds steps for creating a key store in the authentication process.
     */
    private void createKeyStore() {
        authenticationStep.add(new CreateKeyStore(sessionManager, keyStoreManager));
    }

    /**
     * Adds steps for creating and configuring a service configuration in the
     * authentication process.
     */
    private void createServiceConfig() {
        authenticationStep.add(new CreatePasswordListConfig(sessionManager, serviceCryptoConfigManager));
        authenticationStep.add(new DerivePasswordListKey(sessionManager, cryptoManager));
    }

    /**
     * Adds steps for encrypting configuration data in the authentication process.
     */
    private void encryptConfigData() {
        authenticationStep.add(new EncryptKeyStoreConfig(sessionManager, keyStoreManager));
        authenticationStep.add(new EncryptPasswordListConfig(sessionManager, serviceCryptoConfigManager));
    }

    /**
     * Adds steps for saving data in the authentication process.
     */
    private void saveData() {
        authenticationStep.add(new SaveKeyStore(sessionManager, keyStoreManager));
        authenticationStep.add(new SaveUserData(sessionManager));
        authenticationStep.add(new CloseKeyStore(sessionManager, keyStoreManager));
    }

    /**
     * Adds steps for deriving a master key in the authentication process.
     */
    private void createMasterKey() {
        authenticationStep.add(new DeriveMasterKey(sessionManager, cryptoManager));
    }
}
