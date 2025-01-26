package com.zysn.passwordmanager.model.security.algorithm.derivation;

import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.crypto.generators.SCrypt;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.CryptoUtils;

/**
 * This class implements the Scrypt Key Derivation Function (KDF) as defined by the KeyDerivationAlgorithm interface.
 * It generates a secret key using the Scrypt algorithm based on the provided source and configuration.
 */
public class Scrypt implements KeyDerivationAlgorithm {
    private static final String COST_FACTOR = "cost_factor";
    private static final String BLOCK_SIZE = "block_size";
    private static final String PARALLELISM = "parallelism";
    private static final String KEY_SIZE = "key_size";

    /**
     * Derives a secret key using the Scrypt KDF.
     * 
     * @param source the source password or passphrase to derive the key from.
     * @param algorithmConfig the configuration object containing algorithm parameters such as cost factor, block size, parallelism, key size, and salt.
     * @return the derived secret key as a SecretKeySpec.
     */
    @Override
    public SecretKeySpec deriveKey(char[] source, AlgorithmConfig algorithmConfig) {
        int costFactor = Integer.valueOf(algorithmConfig.getParameterValueByName(COST_FACTOR));
        int blockSize = Integer.valueOf(algorithmConfig.getParameterValueByName(BLOCK_SIZE));
        int parallelism = Integer.valueOf(algorithmConfig.getParameterValueByName(PARALLELISM));
        int keySize = Integer.valueOf(algorithmConfig.getParameterValueByName(KEY_SIZE));

        byte[] salt = algorithmConfig.getSalt();

        byte[] sourceBytes = CryptoUtils.charToByteConverter(source);

        SecretKeySpec masterKey = null;

        try {
            byte[] keyBytes = SCrypt.generate(sourceBytes, salt, costFactor, blockSize, parallelism, keySize);
            masterKey = new SecretKeySpec(keyBytes, "AES");
        } finally {
            CryptoUtils.cleanMemory(sourceBytes);
        }

        return masterKey;
    }
}
