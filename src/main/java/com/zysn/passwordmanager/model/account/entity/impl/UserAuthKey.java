package com.zysn.passwordmanager.model.account.entity.impl;

import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;

/**
 * This class represents a user's authentication key which includes various encrypted components.
 * It implements the MustBeDestroyed interface to ensure secure destruction of sensitive data.
 */
public class UserAuthKey implements MustBeDestroyed {

    private byte[] password;

    private byte[] passwordDerivedKey;

    private byte[] totpEncryptionKey;

    private byte[] totpKey;

    private byte[] serviceConfigKey;

    /**
     * Default constructor.
     */
    public UserAuthKey() {
    }

    /**
     * Destroys the sensitive data stored in this class by securely overwriting it.
     */
    @Override
    public void destroy() {
        CryptoUtils.destroy(this::getPassword, this::setPassword);
        CryptoUtils.destroy(this::getPasswordDerivedKey, this::setPasswordDerivedKey);
        CryptoUtils.destroy(this::getTotpEncryptionKey, this::setTotpEncryptionKey);
        CryptoUtils.destroy(this::getTotpKey, this::setTotpKey);
        CryptoUtils.destroy(this::getServiceConfigKey, this::setServiceConfigKey);
    }

    public byte[] getPassword() {
        return password;
    }

    public void setPassword(final byte[] password) {
        this.password = password;
    }

    public byte[] getPasswordDerivedKey() {
        return passwordDerivedKey;
    }

    public char[] getPasswordDerivedKeyChar() {
        return EncodingUtils.byteToBase64(this.getPasswordDerivedKey());
    }

    public void setPasswordDerivedKey(final byte[] passwordDerivedKey) {
        this.passwordDerivedKey = passwordDerivedKey;
    }

    public byte[] getTotpEncryptionKey() {
        return totpEncryptionKey;
    }

    public char[] getTotpEncryptionKeyChar() {
        return EncodingUtils.byteToCharConverter(this.getTotpEncryptionKey());
    }

    public void setTotpEncryptionKey(final byte[] totpEncryptionKey) {
        this.totpEncryptionKey = totpEncryptionKey;
    }

    public byte[] getTotpKey() {
        return totpKey;
    }

    public void setTotpKey(final byte[] totpKey) {
        this.totpKey = totpKey;
    }

    public byte[] getServiceConfigKey() {
        return serviceConfigKey;
    }

    public void setServiceConfigKey(final byte[] serviceConfigKey) {
        this.serviceConfigKey = serviceConfigKey;
    }
}
