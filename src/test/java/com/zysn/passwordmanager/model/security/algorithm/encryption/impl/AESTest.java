package com.zysn.passwordmanager.model.security.algorithm.encryption.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.security.Security;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.encryption.api.EncryptionAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.encryption.patterns.EncryptionAlgorithmFactory;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

public class AESTest {
    private String originalData = "Prova di criptazione";
    private char[] dataInChar;
    private byte[] dataToEncrypt;
    private byte[] key;
    private AlgorithmConfig algorithmConfig;
    private byte[] iv;
    private SecretKeySpec masterKey;

    @BeforeEach
    void setup() {
        Security.addProvider(new BouncyCastleProvider());

        dataInChar = this.originalData.toCharArray();
        dataToEncrypt = this.originalData.getBytes();

        this.iv = new byte[] { 12, 121, -1, 25, 88, -54, 10, 55, -41, -41, 111, -100 };

        this.key = new byte[] { 105, 117, 98, 41, -112, -20, -32, 52, -17, 66, 49, 3, -67, -2, 54, -12, -121, -38, -27,
                -45, -1, -59, 36, -35, -53, -100, -111, -103, -91, 63, -78, -88 };

        this.masterKey = new SecretKeySpec(key, "AES");

        String algorithmName = "AES";
        String algorithmType = "Encryption Algorithm";

        this.algorithmConfig = new AlgorithmConfig(algorithmName, algorithmType);
        this.algorithmConfig.setSalt(iv);

        this.algorithmConfig.addNewParameter("variant", "AES/GCM/NoPadding");
    }

    @Test
    void testEncrypt() {
        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithmFactory
                .createAlgorithm(this.algorithmConfig.getAlgorithmName());

        byte[] exptectedEncryptedData = new byte[] { -62, -7, -106, -4, 35, -83, 29, -48, -122, 63, -21, -110, -109, 22,
                -78, -118, -52, 13, -128, -99, 39, -114, -41, 68, -101, -16, 120, 99, 61, -8, 11, -93, -72, 109, 2,
                -19 };

        byte[] actualEncryptedData = encryptionAlgorithm.encrypt(dataToEncrypt, masterKey, algorithmConfig);

        assertArrayEquals(exptectedEncryptedData, actualEncryptedData,
                "The algorithm didn't create the right encrypted data.");
    }

    @Test
    void testDecrypt() {
        byte[] encryptedData = new byte[] { -62, -7, -106, -4, 35, -83, 29, -48, -122, 63, -21, -110, -109, 22, -78,
                -118, -52, 13, -128, -99, 39, -114, -41, 68, -101, -16, 120, 99, 61, -8, 11, -93, -72, 109, 2, -19 };

        EncryptionAlgorithm encryptionAlgorithm = EncryptionAlgorithmFactory
                .createAlgorithm(this.algorithmConfig.getAlgorithmName());

        byte[] decryptedData = encryptionAlgorithm.decrypt(encryptedData, masterKey, algorithmConfig);

        char[] actualDecryptedData = EncodingUtils.byteToCharConverter(decryptedData);

        char[] expectedDecryptedData = this.dataInChar;

        assertArrayEquals(expectedDecryptedData, actualDecryptedData,
                "The algorithm didn't create the right decrypted data.");
    }
}
