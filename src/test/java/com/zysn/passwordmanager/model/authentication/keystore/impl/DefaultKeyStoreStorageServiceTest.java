package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.Security;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class DefaultKeyStoreStorageServiceTest {

    private DefaultKeyStoreStorageService keyStoreStorageService;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        keyStoreStorageService = new DefaultKeyStoreStorageService();
    }

    @Test
    void testCloseKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("BCFKS", "BC");
        keyStore.load(null, "password".toCharArray());

        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection("password".toCharArray());
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(new byte[16], "AES"));
        keyStore.setEntry("alias1", skEntry, protParam);
        keyStore.setEntry("alias2", skEntry, protParam);

        assertTrue(keyStore.aliases().hasMoreElements() == true, "There are not entries.");

        keyStoreStorageService.closeKeyStore(keyStore);

        assertTrue(keyStore.aliases().hasMoreElements() == false, "The entries has not been eliminated.");
    }

    @Test
    void testLoadKeyStore() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("BCFKS", "BC");
        keyStore.load(null, "password".toCharArray());

        KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection("password".toCharArray());
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(new SecretKeySpec(new byte[16], "AES"));
        keyStore.setEntry("alias1", skEntry, protParam);
        keyStore.setEntry("alias2", skEntry, protParam);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        keyStore.store(outputStream, "password".toCharArray());

        KeyStore loadKeyStore = keyStoreStorageService.loadKeyStore(outputStream.toByteArray(), "password".toCharArray());

        assertNotNull(loadKeyStore, "The keystore has not been loaded.");
    }

    @Test
    void testSaveKeyStore(@TempDir Path tempDir) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("BCFKS", "BC");
        keyStore.load(null, "password".toCharArray());

        Path filePath = tempDir.resolve("test.keystore");
        keyStoreStorageService.saveKeyStore(filePath, keyStore, "password".toCharArray());

        assertNotNull(filePath.toFile(), "The key store has been saved.");
    }
}
