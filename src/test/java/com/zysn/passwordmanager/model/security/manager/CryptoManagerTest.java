package com.zysn.passwordmanager.model.security.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;

public class CryptoManagerTest {
    private CryptoManager cryptoManager;
    private char[] source;
    private AlgorithmConfig algorithmConfig;

    @BeforeEach
    void setup() {
        this.cryptoManager = new CryptoManager();
    }

    @Test
    void testDecrypt() {

    }

    @Test
    void testDeriveMasterKey() {
        this.source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        this.algorithmConfig = new AlgorithmConfig();

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
        String keySizeValue = "128";
        algorithmConfig.addNewParameter(keySizeName, keySizeValue);

        byte[] salt = new byte[] { 72, 92, -108, -126, 80, 92, 114, -96, 13, -93, 69, 96, -89, -25, 34, -102, 77, -20, -8, -41,
                92, 87, 17, 35, 75, -11, -87, -87, -54, 22, 110, 8 };

        algorithmConfig.setSalt(salt);

        SecretKeySpec masterKey = this.cryptoManager.deriveMasterKey(source, algorithmConfig);
        String key = "VR5cRn1Mb1IkMIu8ofpj5+LJMr1+GDQ3KM3nAkES2JTQ8Mt6uOh9igu+ezGkBiYMpQ2ZZuUL8EftATwyLkvjgOUd7Dh0numcoPXzlRm5DHDwC1XyOMJ/xXAQd4b8+0+zDq9SVL1GIAglXUioHjRGgQDGq9/f0hU3io24qJEcYCs=";

        assertEquals(key, Base64.getEncoder().encodeToString(masterKey.getEncoded()), "The key are not equal");
    }

    @Test
    void testEncrypt() {

    }
}
