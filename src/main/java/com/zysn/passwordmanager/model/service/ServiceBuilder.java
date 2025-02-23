package com.zysn.passwordmanager.model.service;

import com.zysn.passwordmanager.model.account.entity.UserAccount;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;

/**
 * A builder class for constructing a Service object.
 * Allows setting various properties of a service.
 * The password is encrypted using the user's master key and encryption configuration during the build process.
 */
public class ServiceBuilder {
    private String name;
    private String username;
    private String email;
    private byte[] encryptedPassword;
    private AlgorithmConfig encryptionConfig;
    private String info;
    private final UserAccount user;
    private final CryptoManager cryptoManager;

    public ServiceBuilder(UserAccount user, CryptoManager crypto) {
        if (user == null) {
            throw new IllegalArgumentException("UserAccount cannot be null.");
        }
        if (crypto == null) {
            throw new IllegalArgumentException("CryptoManager cannot be null.");
        }
        this.user = user;
        this.cryptoManager = crypto;
        this.encryptionConfig = user.getAlgorithmConfig();
    }

    public ServiceBuilder setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
        return this;
    }

    public ServiceBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public ServiceBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public ServiceBuilder setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        this.encryptedPassword = cryptoManager.encrypt(password.getBytes(), user.getMasterKey(), encryptionConfig);
        return this;
    }

    public ServiceBuilder setInfo(String info) {
        this.info = info;
        return this;
    }

    public Service build() {
        if (name == null || name.isBlank()) {
            throw new IllegalStateException("Name must be set before building.");
        }
        if (encryptedPassword == null || encryptedPassword.length == 0) {
            throw new IllegalStateException("Password must be set before building.");
        }
        return new Service(name, username, email, encryptedPassword, encryptionConfig, info);
    }
}
