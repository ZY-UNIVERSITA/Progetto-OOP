package com.zysn.passwordmanager.model.authentication.passwordlist.impl;

import java.util.UUID;
import java.util.function.Consumer;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigService;
import com.zysn.passwordmanager.model.enums.AesAlgorithm;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

/**
 * Service implementation for managing password list configuration.
 */
public class DefaultServiceCryptoConfigService implements ServiceCryptoConfigService {
    private final CryptoManager cryptoManager;

    /**
     * Constructs a DefaultPasswordListConfigService and initializes the
     * CryptoManager.
     */
    public DefaultServiceCryptoConfigService() {
        this.cryptoManager = new CryptoManager();
    }

    /**
     * Creates a new ServiceCryptoConfig for the password list.
     *
     * @return the generated ServiceCryptoConfig
     */
    public ServiceCryptoConfig createPasswordListConfig() {
        final ServiceCryptoConfig serviceConfig = new ServiceCryptoConfig();

        this.generateFileName(serviceConfig::setFileName);
        this.generateHkdfSalt(serviceConfig::setSaltForHKDF);
        this.generateEncryptionSalt(serviceConfig::setSaltForServiceEncryption);

        return serviceConfig;
    }

    /**
     * Encrypts the given configuration data.
     *
     * @param data            the data to encrypt
     * @param key             the encryption key
     * @param algorithmConfig the algorithm configuration
     * @return the encrypted data
     */
    @Override
    public byte[] encryptConfig(final byte[] data, final byte[] key, final AlgorithmConfig algorithmConfig) {
        return this.cryptoManager.encrypt(data, new SecretKeySpec(key, algorithmConfig.getAlgorithmName()),
                algorithmConfig);
    }

    /**
     * Decrypts the given configuration data.
     *
     * @param data            the data to decrypt
     * @param key             the decryption key
     * @param algorithmConfig the algorithm configuration
     * @return the decrypted data
     */
    @Override
    public byte[] decryptConfig(final byte[] data, final byte[] key, final AlgorithmConfig algorithmConfig) {
        return this.cryptoManager.decrypt(data, new SecretKeySpec(key, algorithmConfig.getAlgorithmName()),
                algorithmConfig);
    }

    /**
     * Generates a file name using random UUIDs and applies it using the provided
     * setter function.
     *
     * @param setterFunction the function to set the generated file name
     */
    private void generateFileName(final Consumer<char[]> setterFunction) {
        final String fileNameFirstPart = UUID.randomUUID().toString();
        final String fileNameSecondPart = UUID.randomUUID().toString();

        final char[] fileNameFull = fileNameFirstPart.concat("-").concat(fileNameSecondPart).toCharArray();

        setterFunction.accept(fileNameFull);
    }

    /**
     * Generates an HKDF salt of 12 bytes and applies it using the provided setter
     * function.
     *
     * @param setterFunction the function to set the generated HKDF salt
     */
    private void generateHkdfSalt(final Consumer<byte[]> setterFunction) {
        final byte[] salt = CryptoUtils.generateSalt(12);

        setterFunction.accept(salt);
    }

    /**
     * Generates an encryption salt and applies it using the provided setter
     * function.
     *
     * @param setterFunction the function to set the generated encryption salt
     */
    private void generateEncryptionSalt(final Consumer<byte[]> setterFunction) {
        final byte[] salt = CryptoUtils.generateSalt(Integer.valueOf(AesAlgorithm.GCM_IV_LENGTH.getParameter()));

        setterFunction.accept(salt);
    }
}
