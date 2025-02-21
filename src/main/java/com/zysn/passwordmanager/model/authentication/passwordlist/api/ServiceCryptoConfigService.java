package com.zysn.passwordmanager.model.authentication.passwordlist.api;

import com.zysn.passwordmanager.model.authentication.passwordlist.impl.ServiceCryptoConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;

/**
 * Interface for managing password list configurations.
 */
public interface ServiceCryptoConfigService {

    /**
     * Creates a new ServiceCryptoConfig for the password list.
     *
     * @return the generated ServiceCryptoConfig
     */
    public ServiceCryptoConfig createPasswordListConfig();

    /**
     * Encrypts the given configuration data.
     *
     * @param data the data to encrypt
     * @param key the encryption key
     * @param algorithmConfig the algorithm configuration
     * @return the encrypted data
     */
    public byte[] encryptConfig(byte[] data, byte[] key, AlgorithmConfig algorithmConfig);

    /**
     * Decrypts the given configuration data.
     *
     * @param data the data to decrypt
     * @param key the decryption key
     * @param algorithmConfig the algorithm configuration
     * @return the decrypted data
     */
    public byte[] decryptConfig(byte[] data, byte[] key, AlgorithmConfig algorithmConfig);
}
