package com.zysn.passwordmanager.model.security.manager;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.security.Security;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;

public class CryptoManagerTest {
    private CryptoManager cryptoManager;
    private byte[] source;
    private AlgorithmConfig algorithmConfig;
    private byte[] salt;

    private byte[] expectedMasterKey;
    private byte[] iv;

    private byte[] expectedEncryptedSource;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.cryptoManager = new CryptoManager();
        this.algorithmConfig = new AlgorithmConfig();

        this.source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes();

        this.salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102, 77, -20,
                -8, -41, 92, 87, 17, 35, 75, -11, -87, -87, -54, 22, 110, 8 };

        this.expectedMasterKey = new byte[] { 48, 21, 102, 23, -20, 126, -101, 75, -110, 40, -39, -127, 14, 81, 67, -40,
                9, -81, -104, 28, -24, 46, -88, -19, 6, 119, 42, 125, -22, 66, -110, 20 };

        this.iv = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96 };

        this.expectedEncryptedSource = new byte[] { 4, -50, -58, -88, -56, -96, 47, 105, 85, -86, -127, 83, -101, 71,
                116, -122, -48, -127, -54, -3, 96, -28, 16, -1, -109, -86, -55, 98, 30, -54, -92, -8, 23, -59, -91, -40,
                74, 17, 24, 106, 0, -60, 42, 28, 69, -14, -9, -99, 57, -119, 20, 27 };
    }

    @Test
    void testDeriveMasterKey() {
        final String algorithmName = "Argon2";

        this.algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig(algorithmName, this.salt, null);

        final byte[] actualMasterKey = this.cryptoManager.deriveMasterKey(source, algorithmConfig);

        assertArrayEquals(expectedMasterKey, actualMasterKey,
                "The obtained key is not the same as expected.");
    }

    @Test
    void testEncrypt() {
        final String algorithmName = "AES";

        this.algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig(algorithmName, iv, null);

        final byte[] actualEncryptedText = this.cryptoManager.encrypt(source, new SecretKeySpec(expectedMasterKey, "AES"),
                algorithmConfig);

        assertArrayEquals(this.expectedEncryptedSource, actualEncryptedText,
                "The encryption didn't give the expected output.");
    }

    @Test
    void testDecrypt() {
        final String algorithmName = "AES";

        this.algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig(algorithmName, iv, null);

        final byte[] decryptedText = this.cryptoManager.decrypt(expectedEncryptedSource,
                new SecretKeySpec(expectedMasterKey, algorithmName),
                algorithmConfig);

        assertArrayEquals(this.source, decryptedText, "The decryption didn't give an output that it is expected.");
    }
}
