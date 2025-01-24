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

    /* EQUALS */

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        result = prime * result + ((algorithmConfig == null) ? 0 : algorithmConfig.hashCode());
        result = prime * result + ((masterKey == null) ? 0 : masterKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserAccount other = (UserAccount) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        if (algorithmConfig == null) {
            if (other.algorithmConfig != null)
                return false;
        } else if (!algorithmConfig.equals(other.algorithmConfig))
            return false;
        if (masterKey == null) {
            if (other.masterKey != null)
                return false;
        } else if (!masterKey.equals(other.masterKey))
            return false;
        return true;
    }

    /* TO STRING */

    @Override
    public String toString() {
        return "UserAccount [username=" + username + ", algorithmConfig=" + algorithmConfig + ", masterKey=" + masterKey
                + "]";
    }    
}
