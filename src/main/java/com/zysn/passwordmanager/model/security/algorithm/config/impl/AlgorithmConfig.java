package com.zysn.passwordmanager.model.security.algorithm.config.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;

/**
 * Algorithm configuration class for creating configuration for algorithm.
 */
public class AlgorithmConfig {
    private String algorithmName;
    private String algorithmType;
    private byte[] salt;
    private Map<String, String> parameters;

    public AlgorithmConfig(final String algorithmName, final String algorithmType, final byte[] salt, final Map<String, String> parameters) {
        this.algorithmType = algorithmType;
        this.algorithmName = algorithmName;
        this.salt = salt;
        this.parameters = parameters;
    }

    public AlgorithmConfig(final String algorithmName, final String algorithmType, final byte[] salt) {
        this(algorithmName, algorithmType, salt, new HashMap<>());
    }

    public AlgorithmConfig(final String algorithmName, final String algorithmType) {
        this(algorithmName, algorithmType, null);
    }

    public AlgorithmConfig(final DefaultAlgorithmConfigBuilder algorithmConfigBuilder) {
        this(algorithmConfigBuilder.algorithmName, algorithmConfigBuilder.algorithmType, algorithmConfigBuilder.salt,
                algorithmConfigBuilder.parameters);
    }

    public AlgorithmConfig() {

    }

    /**
     * Add a parameter to the list of parameters.
     * 
     * @param name  the name of the parameter.
     * @param value the value of the parameter.
     * @throws IllegalArgumentException if name and/or value are null.
     * @throws IllegalStateException    if the parameter is already present.
     */
    public void addNewParameter(final String name, final String value) {
        if (name == null || value == null) {
            throw new IllegalArgumentException("Name or value cannot be null.");
        }

        if (this.getParameters().containsKey(name)) {
            throw new IllegalStateException("The parameter " + name + " is already present.");
        }

        this.getParameters().put(name, value);
    }

    /**
     * Removes a parameter from the set of parameters if it is present.
     *
     * @param name the name of the parameter to remove.
     * @throws NoSuchElementException if the parameter with the specified name is
     *                                not present.
     */
    public void removeParameterByName(final String name) {
        if (!this.getParameters().containsKey(name)) {
            throw new NoSuchElementException("The parameter " + name + " is not present.");
        }

        this.getParameters().remove(name);
    }

    /**
     * Updates the value of the parameter with the specified name.
     *
     * @param name  the name of the parameter to update
     * @param value the new value of the parameter
     * @throws IllegalStateException if the parameter with the specified name is not
     *                               present
     */
    public void updateParameter(final String name, final String value) {
        if (!this.getParameters().containsKey(name)) {
            throw new NoSuchElementException("The parameter " + name + " is not present.");
        }

        this.getParameters().replace(name, value);
    }

    /**
     * Retrieves the value of a parameter by its name.
     *
     * @param name The name of the parameter to retrieve.
     * @return The value of the parameter.
     * @throws IllegalStateException if the parameter with the given name is not
     *                               present.
     */
    public String getParameterValueByName(final String name) {
        if (!this.getParameters().containsKey(name)) {
            throw new IllegalStateException("The parameter " + name + " is not present.");
        }

        return this.getParameters().get(name);
    }

    /**
     * Removes the current cryptographic configurations.
     * This method cleans the memory of the salt value,
     * resets the algorithm name and type to null,
     * and clears the parameters.
     */
    public void destroy() {
        // Clear and set null for salt
        if (this.getSalt() != null) {
            CryptoUtils.cleanMemory(this.getSalt());
            this.setSalt(null);
        }

        // Null for algorithm name and type
        this.setAlgorithmName(null);
        this.setAlgorithmType(null);

        // Clear and set null for parameters
        this.getParameters().clear();
        this.setParameters(null);
    }

    /* GETTER and SETTER */
    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(final String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(final String algorithmType) {
        this.algorithmType = algorithmType;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(final byte[] salt) {
        this.salt = salt;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(final Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /* EQUALS */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((algorithmName == null) ? 0 : algorithmName.hashCode());
        result = prime * result + ((algorithmType == null) ? 0 : algorithmType.hashCode());
        result = prime * result + Arrays.hashCode(salt);
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
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
        final AlgorithmConfig other = (AlgorithmConfig) obj;
        if (algorithmName == null) {
            if (other.algorithmName != null)
                return false;
        } else if (!algorithmName.equals(other.algorithmName))
            return false;
        if (algorithmType == null) {
            if (other.algorithmType != null)
                return false;
        } else if (!algorithmType.equals(other.algorithmType))
            return false;
        if (!Arrays.equals(salt, other.salt))
            return false;
        if (parameters == null) {
            if (other.parameters != null)
                return false;
        } else if (!parameters.equals(other.parameters))
            return false;
        return true;
    }

    /* TO STRING */
    @Override
    public String toString() {
        return "AlgorithmConfig [algorithmName=" + algorithmName + ", algorithmType=" + algorithmType + ", salt="
                + Arrays.toString(salt) + ", parameters=" + parameters + "]";
    }
}