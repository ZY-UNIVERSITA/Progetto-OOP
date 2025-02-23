package com.zysn.passwordmanager.model.authentication.keystore.api;

import com.zysn.passwordmanager.model.account.entity.impl.UserAuthKey;
import com.zysn.passwordmanager.model.authentication.keystore.impl.KeyStoreConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;

/**
 * Service interface for KeyStore configuration.
 * 
 * This interface defines methods for generating and encrypting KeyStore configurations
 */
public interface KeyStoreConfigService {

    /**
     * Generates a secure encryption key for the KeyStore configuration.
     * 
     * @param keyStoreConfig The KeyStore configuration to be updated.
     */
    public void generateKeyStoreConfigKey(KeyStoreConfig keyStoreConfig);

    /**
     * Generates secure salts for the KeyStore configuration.
     * 
     * @param keyStoreConfig The KeyStore configuration to be updated.
     */
    public void generateKeyStoreConfigSalt(KeyStoreConfig keyStoreConfig);

    /**
     * Generates a KeyStore entry for the given user authentication key.
     * 
     * @param userAuthKey The user authentication key to be updated.
     */
    public void generateKeyStoreEntry(UserAuthKey userAuthKey);

    /**
     * Encrypts the given configuration data using the provided key and algorithm.
     * 
     * @param data The data to be encrypted.
     * @param key The encryption key.
     * @param algorithmConfig The algorithm configuration.
     * @return The encrypted data.
     */
    public byte[] encryptConfig(byte[] data, byte[] key, AlgorithmConfig algorithmConfig);

    /**
     * Decrypts the given configuration data using the provided key and algorithm.
     * 
     * @param data The data to be decrypted.
     * @param key The decryption key.
     * @param algorithmConfig The algorithm configuration.
     * @return The decrypted KeyStore configuration.
     */
    public KeyStoreConfig decryptConfig(byte[] data, byte[] key, AlgorithmConfig algorithmConfig);
}
