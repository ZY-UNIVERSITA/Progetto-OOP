package com.zysn.passwordmanager.model.service;

import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;

/**
 * Represents a single Service.
 * Stores details about a particular service (e.g., Facebook, Gmail) along with
 * its credentials.
 */
public class Service implements MustBeDestroyed {

    private String name;
    private String username;
    private String email;
    private byte[] encryptedPassword;
    private AlgorithmConfig encryptionConfig;
    private String info;

    public Service(final String name, final String username, final String email, final byte[] password,
            final AlgorithmConfig encryptionConfig,
            final String info) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.encryptedPassword = password;
        this.encryptionConfig = encryptionConfig;
        this.info = info;
    }

    public Service() {
        
    }

    @Override
    public void destroy() {
        CryptoUtils.destroy(this::getName, this::setName);
        CryptoUtils.destroy(this::getUsername, this::setUsername);
        CryptoUtils.destroy(this::getEmail, this::setEmail);
        CryptoUtils.destroy(this::getEncryptedPassword, this::setEncryptedPassword);
        CryptoUtils.destroy(this::getEncryptionConfig, this::setEncryptionConfig);
        CryptoUtils.destroy(this::getInfo, this::setInfo);
    }

    /* GETTER */
    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public byte[] getPassword() {
        return this.encryptedPassword;
    }

    public AlgorithmConfig getEncryptionConfig() {
        return this.encryptionConfig;
    }

    public String getInfo() {
        return this.info;
    }

    @Override
    public String toString() {
        return "Service [name=" + name + ", username=" + username + ", email=" + email + ", info=" + info + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Service other = (Service) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        return true;
    }

    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setEncryptedPassword(final byte[] encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public void setEncryptionConfig(final AlgorithmConfig encryptionConfig) {
        this.encryptionConfig = encryptionConfig;
    }

    public void setInfo(final String info) {
        this.info = info;
    }

}
