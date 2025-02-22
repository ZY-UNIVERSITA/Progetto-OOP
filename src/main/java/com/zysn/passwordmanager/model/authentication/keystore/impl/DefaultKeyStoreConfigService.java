package com.zysn.passwordmanager.model.authentication.keystore.impl;

import java.util.function.Consumer;

import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.account.entity.impl.UserAuthKey;
import com.zysn.passwordmanager.model.authentication.keystore.api.KeyStoreConfigService;
import com.zysn.passwordmanager.model.enums.CryptoLength;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.PasswordGenerator;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

/**
 * Service implementation for KeyStore configuration.
 * 
 * This service provides methods for generating and encrypting KeyStore configurations
 */
public class DefaultKeyStoreConfigService implements KeyStoreConfigService {

    private final PasswordGenerator passwordGenerator;
    private final CryptoManager cryptoManager;

    /**
     * Constructs a new DefaultKeyStoreConfigService.
     */
    public DefaultKeyStoreConfigService() {
        this.passwordGenerator = new PasswordGenerator();
        this.cryptoManager = new CryptoManager();
    }

    /**
     * Generates a secure encryption key for the KeyStore configuration.
     * 
     * @param keyStoreConfig The KeyStore configuration to be updated.
     */
    @Override
    public void generateKeyStoreConfigKey(final KeyStoreConfig keyStoreConfig) {
        this.passwordGenerator.generateSecureUsablePassword(keyStoreConfig::setKeyStoreEncryptionKey);
    }

    /**
     * Generates secure salts for the KeyStore configuration.
     * 
     * @param keyStoreConfig The KeyStore configuration to be updated.
     */
    @Override
    public void generateKeyStoreConfigSalt(final KeyStoreConfig keyStoreConfig) {
        final int minSaltLength = CryptoLength.MINIMUM_PASSWORD_LENGTH.getParameter();
        final int optimalSaltLength = CryptoLength.OPTIMAL_PASSWORD_LENGTH.getParameter();

        this.passwordGenerator.generateSecureUsablePassword(optimalSaltLength, keyStoreConfig::setSaltWithPasswordDerived);
        this.passwordGenerator.generateSecureUsablePassword(minSaltLength, keyStoreConfig::setSaltWithTotpEncryptionKey);
        this.passwordGenerator.generateSecureUsablePassword(minSaltLength, keyStoreConfig::setSaltForHKDF);
        this.passwordGenerator.generateSecureUsablePassword(minSaltLength, keyStoreConfig::setServiceDecryptionSalt);
    }

    /**
     * Generates a KeyStore entry for the given user authentication key.
     * 
     * @param userAuthKey The user authentication key to be updated.
     */
    @Override
    public void generateKeyStoreEntry(final UserAuthKey userAuthKey) {
        this.generateTotpEncryptionKey(userAuthKey::setTotpEncryptionKey);
        this.generateTotpKey(userAuthKey::setTotpKey);
    }

    /**
     * Encrypts the given configuration data using the provided key and algorithm.
     * 
     * @param data The data to be encrypted.
     * @param key The encryption key.
     * @param algorithmConfig The algorithm configuration.
     * @return The encrypted data.
     */
    @Override
    public byte[] encryptConfig(final byte[] data, final byte[] key, final AlgorithmConfig algorithmConfig) {
        return this.cryptoManager.encrypt(data, new SecretKeySpec(key, algorithmConfig.getAlgorithmName()), algorithmConfig);
    }

    /**
     * Decrypts the given configuration data using the provided key and algorithm.
     * 
     * @param data The data to be decrypted.
     * @param key The decryption key.
     * @param algorithmConfig The algorithm configuration.
     * @return The decrypted KeyStore configuration.
     */
    @Override
    public KeyStoreConfig decryptConfig(final byte[] data, final byte[] key, final AlgorithmConfig algorithmConfig) {
        final byte[] decryptedData = this.cryptoManager.decrypt(data, new SecretKeySpec(key, algorithmConfig.getAlgorithmName()), algorithmConfig);
        final KeyStoreConfig keyStoreConfig = EncodingUtils.deserializeData(decryptedData, new TypeReference<KeyStoreConfig>() {});
        return keyStoreConfig;
    }

    /**
     * Generates a TOTP encryption key.
     * 
     * @param setterFunction The function to set the TOTP encryption key.
     */
    private void generateTotpEncryptionKey(final Consumer<byte[]> setterFunction) {
        this.passwordGenerator.generateSecureUsablePassword(setterFunction);
    }

    /**
     * Generates a TOTP key.
     * 
     * @param setterFunction The function to set the TOTP key.
     */
    private void generateTotpKey(final Consumer<byte[]> setterFunction) {
        final int totpLength = CryptoLength.OPTIMAL_PASSWORD_LENGTH.getParameter();
        final byte[] totpKey = CryptoUtils.generateSalt(totpLength);
        setterFunction.accept(totpKey);
    }
}
