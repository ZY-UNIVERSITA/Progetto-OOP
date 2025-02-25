package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

public class LoadKeyStoreTest {
    private AuthenticationStep authenticationStep;

    private DefaultKeyStoreManager keyStoreManager;
    private SessionManager sessionManager;

    @BeforeEach
    void setup() throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();
        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);

        this.sessionManager.getKeyStoreConfig().setKeyStoreEncryptionKey("prova".getBytes());
        this.sessionManager.getUserAccount().setUsername("prova");

        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.saveKeyStore();

        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);

        this.authenticationStep = new LoadKeyStore(sessionManager, keyStoreManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        assertNotNull(this.keyStoreManager.getKeyStore(), "The loaded keystore is null.");

        FileManager fileManager = new DefaultFileManager(PathsConstant.KEY_STORE, ExtensionsConstant.BCFKS);
        fileManager.deleteData("prova");
    }
}
