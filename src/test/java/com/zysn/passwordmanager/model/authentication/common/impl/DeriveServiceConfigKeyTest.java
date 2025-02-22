package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

public class DeriveServiceConfigKeyTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private CryptoManager cryptoManager;

    @BeforeEach
    void setup() {
        this.sessionManager = new DefaultSessionManager();
        this.cryptoManager = new CryptoManager();

        this.sessionManager.getUserAuthKey().setPasswordDerivedKey("password".getBytes());
        this.sessionManager.getKeyStoreConfig().setSaltForHKDF("salt".getBytes());

        this.authenticationStep = new DeriveServiceConfigKey(sessionManager, cryptoManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        assertNotNull(this.sessionManager.getUserAuthKey().getServiceConfigKey(), "The key has not been generated.");
    }
}
