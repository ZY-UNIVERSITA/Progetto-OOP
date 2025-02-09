package com.zysn.passwordmanager.model.service;

import com.zysn.passwordmanager.model.security.config.AlgorithmConfig;

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

    public Service () {

    }

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

    /**
     * Gets the additional information of Service.
     * @return the info
     */
    public String getInfo () {
        return this.info;
    }

    /**
     * Add an additional information to Service.
     * @param info the info of service
     */
    public void setInfo (String info) {
        this.info = info;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Service other = (Service) obj;
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
    
}
