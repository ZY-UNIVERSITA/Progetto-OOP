package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.security.KeyStore;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DefaultKeyStoreCreatorTest {
    private DefaultKeyStoreCreator keyStoreCreator;
    private char[] password;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.keyStoreCreator = new DefaultKeyStoreCreator();
        this.password = "password key store".toCharArray();
    }

    @Test
    void testCreateKeyStore() {
        KeyStore keyStore = this.keyStoreCreator.createKeyStore(this.password);

        assertNotNull(keyStore, "The key store is null.");
        assertEquals("BCFKS", keyStore.getType(), "The type of this key store is not BCFKS.");
    }
}
