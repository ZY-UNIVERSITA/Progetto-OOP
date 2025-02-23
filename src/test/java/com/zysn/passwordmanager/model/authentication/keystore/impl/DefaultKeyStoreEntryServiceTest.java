package com.zysn.passwordmanager.model.authentication.keystore.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreEntryService;

public class DefaultKeyStoreEntryServiceTest {
    private KeyStore keyStore;
    private KeyStoreEntryService keyStoreEntryService;

    @BeforeEach
    public void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        keyStore = KeyStore.getInstance("BCFKS", "BC");
        keyStore.load(null, null);

        this.keyStoreEntryService = new DefaultKeyStoreEntryService();
    }

    @Test
    void testInsertKey() throws KeyStoreException {
        String alias = "Test key";
        byte[] keyToInsert = "1234567890123456".getBytes();
        char[] password = "strongPassword".toCharArray();

        keyStoreEntryService.insertKey(keyStore, alias, keyToInsert, password);

        assertNotNull(keyStore.containsAlias(alias), "The entry does not exist.");

        byte[] retrievedKey = keyStoreEntryService.retrieveKey(keyStore, alias, password);

        assertNotNull(retrievedKey, "The key is null.");
        assertArrayEquals(keyToInsert, retrievedKey, "The retrieved key is not the same as the inserted key.");

    }

    @Test
    void testRetrieveKey() {
        String alias = "Test key";
        byte[] keyToInsert = "1234567890123456".getBytes();
        char[] correctPassword = "strongPassword".toCharArray();
        char[] wrongPassword = "wrongPassword".toCharArray();

        keyStoreEntryService.insertKey(keyStore, alias, keyToInsert, correctPassword);

        byte[] retrievedKey = keyStoreEntryService.retrieveKey(keyStore, alias, wrongPassword);
        assertNull(retrievedKey, "The key should be null using a wrong password.");

        retrievedKey = keyStoreEntryService.retrieveKey(keyStore, alias, correctPassword);
        assertNotNull(retrievedKey, "The key should not be null using the correct password.");
        assertArrayEquals(keyToInsert, retrievedKey, "The retrieved key is not the same as the inserted key.");
    }
}
