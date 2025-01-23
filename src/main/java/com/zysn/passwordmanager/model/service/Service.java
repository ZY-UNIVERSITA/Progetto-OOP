package com.zysn.passwordmanager.model.service;

/**
 * Represents a Service with credentials and additional information.
 */
public class Service {

    private String name;
    private String username;
    private String email;
    private byte[] encryptedPassword;
    private AlgorithmConfig encryptionConfig;
    private String info;

    public Service (String name, String username, String email, byte[] password, AlgorithmConfig encrypConf, String info) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.encryptedPassword = password;
        this.encryptionConfig = encrypConf;
        this.info = info;
    }

    /**
     * Gets the name of Service.
     * @return the name
     */
    public String getName () {
        return this.name;
    }

    /**
     * Sets a name to Service.
     * @param name the name of service
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
     * Gets the username of Service.
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets a username to Service.
     * @param username the username of service
     */
    public void setUsername (String username) {
        this.username = username;
    }

    /**
     * Gets the email of Service.
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Sets an email to Service.
     * @param email the email of service
     */
    public void setEmail (String email) {
        this.email = email;
    }

    /**
     * Gets the encrypted password of Service.
     * @return the password
     */
    public byte[] getPassword () {
        return this.encryptedPassword;
    }

    /**
     * Sets an encrypted password to Service.
     * @param password the encrypted password of service
     */
    public void setPassword (byte[] password) {
        this.encryptedPassword = password;
    }

    /**
     *  Gets the encryption algorithm configuration of Service.
     * @return the encryption algorithm configuration
     */
    public AlgorithmConfig getEncryptionConfig () {
        return this.encryptionConfig;
    }

    /**
     *  Sets an encryption algorithm configuration of Service.
     * @param algorithm the encryption algorithm configuration
     */
    public void setEncryptionConfig (AlgorithmConfig algorConf) {
        this.encryptionConfig = algorConf;
    }
    
}
