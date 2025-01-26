package com.zysn.passwordmanager.model.security.algorithm.derivation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

public class BCryptTest {
    private KeyDerivationAlgorithm keyDerivationAlgorithm;
    private char[] source;
    private byte[] salt;
    private AlgorithmConfig algorithmConfig;

    @BeforeEach
    void setup() {
        keyDerivationAlgorithm = new BCrypt();
        source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102 };

        algorithmConfig = new AlgorithmConfig("Key Derivation Algorithm", "Bcrypt", salt, new HashMap<>());

        String iterationsName = "cost";
        String iterationsValue = "12";
        algorithmConfig.addNewParameter(iterationsName, iterationsValue);

    }

    @Test
    void testDeriveKey() {
        SecretKeySpec masterKey = keyDerivationAlgorithm.deriveKey(source, algorithmConfig);

        byte[] masterKeyByte = new byte[] { -43, -44, 54, 52, 108, -27, -46, -42, 123, -125, 84, 77, 54, -47, 37, 22,
                117, 104, 108, 32, -21, -114, -42, -91 };

        assertArrayEquals(masterKeyByte, masterKey.getEncoded(),
                "Obtained value is: " + masterKey.getEncoded() + " but the real value should be: " + masterKeyByte);
    }
}
