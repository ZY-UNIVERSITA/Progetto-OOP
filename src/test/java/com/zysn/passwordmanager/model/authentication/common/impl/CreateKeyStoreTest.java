package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyStoreException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;

public class CreateKeyStoreTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private DefaultKeyStoreManager keyStoreManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        byte[] key = "password".getBytes();
        this.sessionManager.getUserAuthKey().setPasswordDerivedKey(key);

        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);

        this.authenticationStep = new CreateKeyStore(sessionManager, keyStoreManager);
    }

    @Test
    void testExecuteStep() throws KeyStoreException {
        this.authenticationStep.executeStep();

        assertNotNull(this.keyStoreManager.getKeyStore(), "The key store has not been created.");
        assertTrue(this.keyStoreManager.getKeyStore().aliases().hasMoreElements(), "The key store has 0 entries.");
        
        assertNotNull(this.sessionManager.getUserAuthKey().getTotpKey(), "The totp key is null.");
    }
}
