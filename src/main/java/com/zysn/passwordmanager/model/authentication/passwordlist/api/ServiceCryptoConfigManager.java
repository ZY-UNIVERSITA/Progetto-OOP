package com.zysn.passwordmanager.model.authentication.passwordlist.api;

/**
 * Interface for managing cryptographic service configurations.
 */
public interface ServiceCryptoConfigManager {

    /**
     * Creates a new service configuration.
     */
    public void createServiceConfig();

    /**
     * Encrypts the service configuration.
     */
    public void encryptConfig();

    /**
     * Decrypts the service configuration.
     */
    public void decryptConfig();
}
