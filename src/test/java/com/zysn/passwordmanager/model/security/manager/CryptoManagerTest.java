package com.zysn.passwordmanager.model.security.manager;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.Crypt;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.CryptoUtils;

public class CryptoManagerTest {
    private CryptoManager cryptoManager;
    private char[] source;
    private AlgorithmConfig algorithmConfig;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        this.cryptoManager = new CryptoManager();
        this.algorithmConfig = new AlgorithmConfig();

        this.source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    }

    @Test
    void testDeriveMasterKey() {
        String algorithmName = "Argon2";
        String algorithmType = "Key Derivation Algorithm";

        this.algorithmConfig.setAlgorithmName(algorithmName);
        this.algorithmConfig.setAlgorithmType(algorithmType);
        this.algorithmConfig.setParameters(new HashMap<String, String>());

        String variantName = "variant";
        String variantValue = "argon2id";
        algorithmConfig.addNewParameter(variantName, variantValue);

        String iterationsName = "iterations";
        String iterationsValue = "3";
        algorithmConfig.addNewParameter(iterationsName, iterationsValue);

        String memoryCostName = "memory_cost";
        String memoryCostValue = "8192";
        algorithmConfig.addNewParameter(memoryCostName, memoryCostValue);

        String parallelismName = "parallelism";
        String parallelismValue = "1";
        algorithmConfig.addNewParameter(parallelismName, parallelismValue);

        String keySizeName = "key_size";
        String keySizeValue = "256";
        algorithmConfig.addNewParameter(keySizeName, keySizeValue);

        byte[] salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102, 77, -20,
                -8, -41, 92, 87, 17, 35, 75, -11, -87, -87, -54, 22, 110, 8 };

        algorithmConfig.setSalt(salt);

        byte[] expectedMasterKey = new byte[] { -34, -54, -51, 47, -105, 66, 24, -101, -14, 63, -105, -103, 63, 81, -97,
                75, 29, -6, 61, -95, -11, 1, -107, -1, -107, 52, -72, -22, -95, 119, -84, -66 };

        byte[] actualMasterKey = this.cryptoManager.deriveMasterKey(source, algorithmConfig);

        assertArrayEquals(expectedMasterKey, actualMasterKey, "The obtained key is not the same.");
    }

    @Test
    void testEncrypt() {
        byte[] sourceInByte = CryptoUtils.charToByteConverter(this.source);

        byte[] masterKey = new byte[] { -34, -54, -51, 47, -105, 66, 24, -101, -14, 63, -105, -103, 63, 81, -97,
                75, 29, -6, 61, -95, -11, 1, -107, -1, -107, 52, -72, -22, -95, 119, -84, -66 };

        byte[] iv = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96 };

        this.algorithmConfig.setSalt(iv);

        String algorithmName = "AES";
        String algorithmType = "Encryption Algorithm";

        this.algorithmConfig.setAlgorithmName(algorithmName);
        this.algorithmConfig.setAlgorithmType(algorithmType);
        this.algorithmConfig.setParameters(new HashMap<String, String>());

        String aesAlgorithmType = "AES/GCM/NoPadding";
        this.algorithmConfig.addNewParameter("variant", aesAlgorithmType);

        byte[] actualEncryptedText = this.cryptoManager.encrypt(sourceInByte, new SecretKeySpec(masterKey, "AES"),
                algorithmConfig);

        byte[] expectedEncryptedText = new byte[] { -3, -38, 24, 85, 91, 114, -21, -40, -82, -19, -106, -86, -52, 31,
                43, -123, 6, 107, -51, -70, -88, 33, 49, 34, 7, 41, 27, -4, -96, 16, 42, -118, -97, -27, 26, -71, 92,
                -83, 83, 113, -83, -38, 26, -112, -8, 49, -37, 10, 23, -121, -35, -5 };

        assertArrayEquals(expectedEncryptedText, actualEncryptedText,
                "The encryption didn't give the expected output.");
    }

    @Test
    void testDecrypt() {
        byte[] masterKey = new byte[] { -34, -54, -51, 47, -105, 66, 24, -101, -14, 63, -105, -103, 63, 81, -97,
                75, 29, -6, 61, -95, -11, 1, -107, -1, -107, 52, -72, -22, -95, 119, -84, -66 };

        byte[] iv = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96 };

        this.algorithmConfig.setSalt(iv);

        String algorithmName = "AES";
        String algorithmType = "Encryption Algorithm";

        this.algorithmConfig.setAlgorithmName(algorithmName);
        this.algorithmConfig.setAlgorithmType(algorithmType);
        this.algorithmConfig.setParameters(new HashMap<String, String>());

        String aesAlgorithmType = "AES/GCM/NoPadding";
        this.algorithmConfig.addNewParameter("variant", aesAlgorithmType);

        byte[] encryptedText = new byte[] { -3, -38, 24, 85, 91, 114, -21, -40, -82, -19, -106, -86, -52, 31,
            43, -123, 6, 107, -51, -70, -88, 33, 49, 34, 7, 41, 27, -4, -96, 16, 42, -118, -97, -27, 26, -71, 92,
            -83, 83, 113, -83, -38, 26, -112, -8, 49, -37, 10, 23, -121, -35, -5 };

        byte[] ecryptedText = this.cryptoManager.decrypt(encryptedText, new SecretKeySpec(masterKey, algorithmName), algorithmConfig);

        char[] actualDecryptedText = CryptoUtils.byteToCharConverter(ecryptedText);

        assertArrayEquals(this.source, actualDecryptedText, "The decryption didn't give an output that it is expected.");
    }
}
