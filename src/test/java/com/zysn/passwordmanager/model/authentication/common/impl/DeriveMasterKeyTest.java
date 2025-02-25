package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

public class DeriveMasterKeyTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private CryptoManager cryptoManager;

    @BeforeEach
    void setup() {
        this.sessionManager = new DefaultSessionManager();

        byte[] password1 = new byte[] { 1, 2, 3 };
        this.sessionManager.getUserAuthKey().setPasswordDerivedKey(password1);

        byte[] password2 = new byte[] { 4, 5, 6 };
        this.sessionManager.getKeyStoreConfig().setKeyStoreEncryptionKey(password2);

        byte[] password3 = new byte[] { 7, 8, 9 };
        this.sessionManager.getUserAuthKey().setTotpEncryptionKey(password3);

        byte[] salt = new byte[] { 10, 11, 12 };
        this.sessionManager.getServiceConfig().setSaltForHKDF(salt);

        this.cryptoManager = new CryptoManager();

        this.authenticationStep = new DeriveMasterKey(sessionManager, cryptoManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        assertNotNull(this.sessionManager.getUserAccount().getMasterKey(), "The master key is null.");
    }
}
