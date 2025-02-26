package com.zysn.passwordmanager.model.account.entity.impl;

import com.zysn.passwordmanager.model.account.entity.api.UserAccountAbstract;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

/**
 * This class represents the authentication information for a user.
 * It extends UserAccountAbstract and provides additional configuration
 * for password-derived keys and keystore encryption.
 */
public class UserAuthInfo extends UserAccountAbstract {

    private AlgorithmConfig passwordDerivedKeyConfig;

    private AlgorithmConfig keyStoreEncryptionConfig;

    private byte[] keyStoreConfigEncryptedData;

    private byte[] serviceConfigEncryptedData;

    private boolean enabled2FA;

    /**
     * Constructs a new UserAuthInfo with the specified username.
     * 
     * @param username the username for this authentication information
     */
    public UserAuthInfo(final String username) {
        super(username);
    }

    /**
     * Default constructor for UserAuthInfo.
     */
    public UserAuthInfo() {

    }

    /**
     * Destroys this UserAuthInfo instance, clearing sensitive data.
     * This method overrides the destroy method in UserAccountAbstract.
     */
    @Override
    public void destroy() {
        super.destroy();

        CryptoUtils.destroy(this::getPasswordDerivedKeyConfig, this::setPasswordDerivedKeyConfig);
        CryptoUtils.destroy(this::getKeyStoreEncryptionConfig, this::setKeyStoreEncryptionConfig);
        CryptoUtils.destroy(this::getKeyStoreConfigEncryptedData, this::setKeyStoreConfigEncryptedData);
        CryptoUtils.destroy(this::getServiceConfigEncryptedData, this::setServiceConfigEncryptedData);

        this.setEnabled2FA(false);
    }

    public AlgorithmConfig getPasswordDerivedKeyConfig() {
        return passwordDerivedKeyConfig;
    }

    public void setPasswordDerivedKeyConfig(final AlgorithmConfig passwordDerivedKeyConfig) {
        this.passwordDerivedKeyConfig = passwordDerivedKeyConfig;
    }

    public AlgorithmConfig getKeyStoreEncryptionConfig() {
        return keyStoreEncryptionConfig;
    }

    public void setKeyStoreEncryptionConfig(final AlgorithmConfig keyStoreEncryptionConfig) {
        this.keyStoreEncryptionConfig = keyStoreEncryptionConfig;
    }

    public byte[] getKeyStoreConfigEncryptedData() {
        return keyStoreConfigEncryptedData;
    }

    public void setKeyStoreConfigEncryptedData(final byte[] keyStoreConfigEncryptedData) {
        this.keyStoreConfigEncryptedData = keyStoreConfigEncryptedData;
    }

    public byte[] getServiceConfigEncryptedData() {
        return serviceConfigEncryptedData;
    }

    public void setServiceConfigEncryptedData(final byte[] serviceConfigEncryptedData) {
        this.serviceConfigEncryptedData = serviceConfigEncryptedData;
    }

    public boolean isEnabled2FA() {
        return enabled2FA;
    }

    public void setEnabled2FA(final boolean enabled2fa) {
        enabled2FA = enabled2fa;
    }    
}
