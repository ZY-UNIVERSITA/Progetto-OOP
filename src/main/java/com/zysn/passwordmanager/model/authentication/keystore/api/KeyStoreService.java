package com.zysn.passwordmanager.model.authentication.keystore.api;

/**
 * KeyStoreService interface defines the contract for services that handle the generation
 * of various encryption keys and salts used within a keystore configuration.
 */
public interface KeyStoreService {

    /**
     * Generates the keystore encryption key.
     *
     * @return true if the encryption key has been generated successfully, false otherwise
     */
    public boolean generateKeyStoreEncryptionKey();

    /**
     * Generates the TOTP encryption key.
     *
     * @return true if the TOTP encryption key has been generated successfully, false otherwise
     */
    public boolean generateTotpEncryptionKey();

    /**
     * Generates the TOTP key.
     *
     * @return true if the TOTP key has been generated successfully, false otherwise
     */
    public boolean generateTotpKey();

    /**
     * Generates salts for password-derived and TOTP encryption key storage.
     *
     * @return true if the salts have been generated successfully, false otherwise
     */
    public boolean generateSalt();
}
