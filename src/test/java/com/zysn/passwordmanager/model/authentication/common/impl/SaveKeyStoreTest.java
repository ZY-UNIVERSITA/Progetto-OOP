package com.zysn.passwordmanager.model.authentication.common.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.account.manager.impl.DefaultSessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationStep;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.DefaultKeyStoreManager;
import com.zysn.passwordmanager.model.enums.PathsConstant;

public class SaveKeyStoreTest {
    private AuthenticationStep authenticationStep;

    private SessionManager sessionManager;
    private KeyStoreManager keyStoreManager;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.sessionManager = new DefaultSessionManager();

        String username = "test username";
        this.sessionManager.getUserAccount().setUsername(username);

        byte[] keyStorePassword = "key store password".getBytes();
        this.sessionManager.getKeyStoreConfig().setKeyStoreEncryptionKey(keyStorePassword);

        this.keyStoreManager = new DefaultKeyStoreManager(sessionManager);
        this.keyStoreManager.createNewKeyStore();

        this.authenticationStep = new SaveKeyStore(sessionManager, keyStoreManager);
    }

    @Test
    void testExecuteStep() {
        this.authenticationStep.executeStep();

        Path path = Paths.get(System.getProperty(PathsConstant.USER_ROOT.getParameter()), PathsConstant.KEY_STORE.getParameter());

        assertNotNull(path.resolve("test username.bcfks").toFile().exists(), "no");

        path.resolve("test username.bcfks").toFile().delete();
    }
}
