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
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.authentication.login.api.LoginService;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.impl.DefaultServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

public class DefaultLoginService implements LoginService {
private List<AuthenticationStep> authenticationStep;
    private KeyStoreManager keyStoreManager;
    private ServiceCryptoConfigManager passwordListConfigManager;

    private CryptoManager cryptoManager;
    private SessionManager sessionManager;
    private CollectedUserData collectedUserData;
    
    public DefaultLoginService(SessionManager sessionManager) {
        this.authenticationStep = new ArrayList<>();
        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);
        this.passwordListConfigManager = new DefaultServiceCryptoConfigManager(sessionManager);

        this.cryptoManager = new CryptoManager();
        this.sessionManager = sessionManager;
    }

    @Override
    public void login(CollectedUserData collectedUserData) {
        this.collectedUserData = collectedUserData;

        this.loginStep();

        this.authenticationStep.forEach(AuthenticationStep::executeStep);
    }    

    private void loginStep() {
        authenticationStep.add(new InsertUserData(sessionManager, collectedUserData));
        authenticationStep.add(new LoadUserData(sessionManager));
        authenticationStep.add(new DeriveKeyFromPassword(sessionManager, cryptoManager));
        authenticationStep.add(new DecryptKeyStoreConfig(sessionManager, keyStoreManager));
        authenticationStep.add(new LoadKeyStore(sessionManager, keyStoreManager));
        authenticationStep.add(new GetKeyFromKeyStore(sessionManager, keyStoreManager));
        authenticationStep.add(new DeriveServiceConfigKey(sessionManager, cryptoManager));
        authenticationStep.add(new DecryptServiceConfig(sessionManager, passwordListConfigManager));
        authenticationStep.add(new DeriveMasterKey(sessionManager, cryptoManager));
    }
    
    
}
