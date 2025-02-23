package com.zysn.passwordmanager.model.account.entity.impl;

import com.zysn.passwordmanager.model.account.entity.api.UserAccountAbstract;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

/**
 * The CollectedUserData class extends UserAccountAbstract and provides additional
 * functionality specific to user data collection.
 */
public class CollectedUserData extends UserAccountAbstract {

    private byte[] password;

    private byte[] confirmPassword;

    private AlgorithmConfig passwordDerivationConfig;

    private AlgorithmConfig keyStoreConfigEncryptionConfig;

    private boolean enabled2FA;

    private byte[] totpKey;

    /**
     * Default constructor for CollectedUserData.
     */
    public CollectedUserData() {
    }

    /**
     * Destroys sensitive data stored in the object and disables 2FA.
     * It is called to securely erase user data and ensure that no sensitive
     * information remains in memory.
     */
    @Override
    public void destroy() {
        super.destroy();
        
        this.setEnabled2FA(false);

        CryptoUtils.destroy(this::getPassword, this::setPassword);
        CryptoUtils.destroy(this::getConfirmPassword, this::setConfirmPassword);
        CryptoUtils.destroy(this::getPasswordDerivationConfig, this::setPasswordDerivationConfig);
        CryptoUtils.destroy(this::getKeyStoreConfigEncryptionConfig, this::setKeyStoreConfigEncryptionConfig);
        CryptoUtils.destroy(this::getTotpKey, this::setTotpKey);
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(final byte[] password) {
        this.password = password;
    }

    public byte[] getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(final byte[] confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public AlgorithmConfig getPasswordDerivationConfig() {
        return passwordDerivationConfig;
    }

    public void setPasswordDerivationConfig(final AlgorithmConfig passwordDerivationConfig) {
        this.passwordDerivationConfig = passwordDerivationConfig;
    }

    public AlgorithmConfig getKeyStoreConfigEncryptionConfig() {
        return keyStoreConfigEncryptionConfig;
    }

    public void setKeyStoreConfigEncryptionConfig(final AlgorithmConfig keyStoreConfigEncryptionConfig) {
        this.keyStoreConfigEncryptionConfig = keyStoreConfigEncryptionConfig;
    }

    public boolean isEnabled2FA() {
        return enabled2FA;
    }

    public void setEnabled2FA(final boolean enabled2fa) {
        enabled2FA = enabled2fa;
    }

    public byte[] getTotpKey() {
        return totpKey;
    }

    public void setTotpKey(final byte[] totpKey) {
        this.totpKey = totpKey;
    }
}
