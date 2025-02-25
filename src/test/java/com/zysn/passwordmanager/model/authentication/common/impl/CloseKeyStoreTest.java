package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Security;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;

public class CloseKeyStoreTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private DefaultKeyStoreManager keyStoreManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        byte[] password = "test prova".getBytes();
        this.sessionManager.getKeyStoreConfig().setKeyStoreEncryptionKey(password);

        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);
        
        this.authenticationStep = new CloseKeyStore(sessionManager, keyStoreManager);
    }

    @Test
    void testExecuteStep() throws KeyStoreException {
        this.keyStoreManager.createNewKeyStore();

        KeyStore keyStore = this.keyStoreManager.getKeyStore();
        keyStore.setKeyEntry("test entry", new SecretKeySpec(new byte[] { 1, 2, 3 }, "AES"), null, null);

        this.authenticationStep.executeStep();

        assertNull(this.keyStoreManager.getKeyStore(), "The key store has not been deleted.");
        assertFalse(keyStore.aliases().hasMoreElements(), "The key store still contains at least an element.");
    }
}
