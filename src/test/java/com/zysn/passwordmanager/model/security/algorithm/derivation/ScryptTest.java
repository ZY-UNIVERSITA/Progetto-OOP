package com.zysn.passwordmanager.model.security.algorithm.derivation;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.HashMap;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

public class ScryptTest {
    private KeyDerivationAlgorithm keyDerivationAlgorithm;
    private char[] source;
    private byte[] salt;
    private AlgorithmConfig algorithmConfig;

    @BeforeEach
    void setup() {
        keyDerivationAlgorithm = new Scrypt();
        source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102, 77, -20, -8, -41,
                92, 87, 17, 35, 75, -11, -87, -87, -54, 22, 110, 8 };

        algorithmConfig = new AlgorithmConfig("Key Derivation Algorithm", "Scrypt", salt, new HashMap<>());

        String iterationsName = "cost_factor";
        String iterationsValue = "16384";
        algorithmConfig.addNewParameter(iterationsName, iterationsValue);

        String memoryCostName = "block_size";
        String memoryCostValue = "8";
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

        byte[] masterKeyByte = new byte[] { -107, 29, 119, 84, -17, 116, 114, 121, -127, -37, 72, 111, -77, -106, -110, -1, -51, 
            -109, 3, -106, 121, -89, -91, -70, 108, -105, -35, -101, -93, -56, -125, -27, -16, 36, 31, -13, 91, -84, 3, -69, 25, 
            -102, -122, 104, -57, 118, -58, 31, -115, -118, 117, 59, 31, -106, 39, 9, 79, 103, -21, 105, -119, 112, -99, 108, 37, 
            107, 115, -115, -10, -63, 61, 110, -91, -82, 19, 16, -38, 107, -59, -48, 73, 78, 113, 32, 107, -117, -52, -101, 66, 
            -47, -1, 6, 82, -67, 115, -43, -54, -94, -70, -1, -51, 38, -53, -33, -50, -122, -11, 96, -64, 36, -78, -101, 72, -115, 52, 
            -19, -72, 55, -13, 21, -8, -113, 28, -88, 81, 105, 95, 43 };

        assertArrayEquals(masterKeyByte, masterKey.getEncoded(),
                "Obtained value is: " + masterKey.getEncoded() + " but the real value should be: " + masterKeyByte);
    }
}
