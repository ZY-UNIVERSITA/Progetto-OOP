package com.zysn.passwordmanager.model.security.algorithm.derivation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

public class Argon2Test {
    private KeyDerivationAlgorithm keyDerivationAlgorithm;
    private char[] source;
    private byte[] salt;
    private AlgorithmConfig algorithmConfig;

    @BeforeEach
    void setup() {
        keyDerivationAlgorithm = new Argon2();
        source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102, 77, -20, -8, -41,
                92, 87, 17, 35, 75, -11, -87, -87, -54, 22, 110, 8 };

        algorithmConfig = new AlgorithmConfig("Key Derivation Algorithm", "Argon2", salt, new HashMap<>());

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
        String keySizeValue = "128";
        algorithmConfig.addNewParameter(keySizeName, keySizeValue);

    }

    @Test
    void testDeriveKey() {
        SecretKeySpec masterKey = keyDerivationAlgorithm.deriveKey(source, algorithmConfig);

        byte[] masterKeyByte = new byte[] { 85, 30, 92, 70, 125, 76, 111, 82, 36, 48, -117, -68, -95, -6, 99, -25, -30,
                -55, 50, -67, 126, 24, 52, 55, 40, -51, -25, 2, 65, 18, -40, -108, -48, -16, -53, 122, -72, -24, 125,
                -118, 11, -66, 123, 49, -92, 6, 38, 12, -91, 13, -103, 102, -27, 11, -16, 71, -19, 1, 60, 50, 46, 75,
                -29, -128, -27, 29, -20, 56, 116, -98, -23, -100, -96, -11, -13, -107, 25, -71, 12, 112, -16, 11, 85,
                -14, 56, -62, 127, -59, 112, 16, 119, -122, -4, -5, 79, -77, 14, -81, 82, 84, -67, 70, 32, 8, 37, 93,
                72, -88, 30, 52, 70, -127, 0, -58, -85, -33, -33, -46, 21, 55, -118, -115, -72, -88, -111, 28, 96, 43 };

        assertArrayEquals(masterKeyByte, masterKey.getEncoded(),
                "Obtained value is: " + masterKey.getEncoded() + " but the real value should be: " + masterKeyByte);
    }

}
