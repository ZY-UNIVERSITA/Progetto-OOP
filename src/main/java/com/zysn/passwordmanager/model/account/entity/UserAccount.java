package com.zysn.passwordmanager.model.account.entity;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

public class UserAccount {
    private String username;
    private AlgorithmConfig algorithmConfig;
    private SecretKeySpec masterKey;
    
    /* CONSTRUCTOR */
    public UserAccount(String username, AlgorithmConfig algorithmConfig, SecretKeySpec masterKey) {
        this.username = username;
        this.algorithmConfig = algorithmConfig;
        this.masterKey = masterKey;
    }

    /* GETTER AND SETTER */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AlgorithmConfig getAlgorithmConfig() {
        return algorithmConfig;
    }

    public void setAlgorithmConfig(AlgorithmConfig algorithmConfig) {
        this.algorithmConfig = algorithmConfig;
    }

    public SecretKeySpec getMasterKey() {
        return masterKey;
    }

    public void setMasterKey(SecretKeySpec masterKey) {
        this.masterKey = masterKey;
    }
}
