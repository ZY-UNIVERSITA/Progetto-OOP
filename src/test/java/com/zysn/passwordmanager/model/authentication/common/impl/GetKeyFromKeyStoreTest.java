package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;

public class GetKeyFromKeyStoreTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private KeyStoreManager keyStoreManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();
        this.sessionManager.getUserAccount().setUsername("prova");

        final byte[] key = new byte[32];
        Arrays.fill(key, (byte) 1);
        this.sessionManager.getUserAuthKey().setPasswordDerivedKey(key);

        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);

        this.authenticationStep = new GetKeyFromKeyStore(sessionManager, keyStoreManager);
    }

    @Test
    void testExecuteStep() {
        this.keyStoreManager.createKeyStoreConfig();
        this.keyStoreManager.createKeyStoreEntry();
        this.keyStoreManager.createNewKeyStore();
        this.keyStoreManager.populateNewKeyStore();
        this.keyStoreManager.saveKeyStore();

        this.sessionManager.getUserAuthKey().setTotpEncryptionKey(null);
        this.sessionManager.getUserAuthKey().setTotpKey(null);

        this.authenticationStep.executeStep();

        assertNotNull(this.sessionManager.getUserAuthKey().getTotpEncryptionKey(), "Totp encryption key is null.");
        assertNotNull(this.sessionManager.getUserAuthKey().getTotpKey(), "Totp key is null.");

        final FileManager fileManager = new DefaultFileManager(PathsConstant.KEY_STORE, ExtensionsConstant.BCFKS);
        fileManager.deleteData("prova");
    }
}
