package com.zysn.passwordmanager.model.security.config;

import java.util.Map;
import java.util.NoSuchElementException;

public class AlgorithmConfig {
    private String algorithmType;
    private String algorithmName;
    private Map<String, String> parameters;

    public AlgorithmConfig(String algorithmType, String algorithmName, Map<String, String> parameters) {
        this.algorithmType = algorithmType;
        this.algorithmName = algorithmName;
        this.parameters = parameters;
    }

    /**
     * Add a parameter to the list of parameters.
     * 
     * @param name  the name of the parameter.
     * @param value the value of the parameter.
     * @throws IllegalArgumentException if name and/or value are null.
     * @throws IllegalStateException    if the parameter is already present.
     */
    public void addNewParameter(String name, String value) {
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
     * @throws NoSuchElementException if the parameter with the specified name is not
     *                               present.
     */
    public void removeParameterByName(String name) {
        if (!this.getParameters().containsKey(name)) {
            throw new NoSuchElementException("The parameter " + name + " is not present.");
        }

        this.getParameters().remove(name);
    }

    /**
     * Retrieves the value of a parameter by its name.
     *
     * @param name The name of the parameter to retrieve.
     * @return The value of the parameter.
     * @throws IllegalStateException if the parameter with the given name is not
     *                               present.
     */
    public String getParameterValueByName(String name) {
        if (!this.getParameters().containsKey(name)) {
            throw new IllegalStateException("The parameter " + name + " is not present.");
        }

        return this.getParameters().get(name);
    }

    /**
     * Updates the value of the parameter with the specified name.
     *
     * @param name  the name of the parameter to update
     * @param value the new value of the parameter
     * @throws IllegalStateException if the parameter with the specified name is not
     *                               present
     */
    public void updateParameter(String name, String value) {
        if (!this.getParameters().containsKey(name)) {
            throw new NoSuchElementException("The parameter " + name + " is not present.");
        }

        this.getParameters().replace(name, value);
    }

    /* GETTER SETTER */
    public String getAlgorithmType() {
        return algorithmType;
    }

    public void setAlgorithmType(String algorithmType) {
        this.algorithmType = algorithmType;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    /* EQUALS */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((algorithmType == null) ? 0 : algorithmType.hashCode());
        result = prime * result + ((algorithmName == null) ? 0 : algorithmName.hashCode());
        result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
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
        AlgorithmConfig other = (AlgorithmConfig) obj;
        if (algorithmType == null) {
            if (other.algorithmType != null)
                return false;
        } else if (!algorithmType.equals(other.algorithmType))
            return false;
        if (algorithmName == null) {
            if (other.algorithmName != null)
                return false;
        } else if (!algorithmName.equals(other.algorithmName))
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
        return "AlgorithmConfig [algorithmType=" + algorithmType + ", algorithmName=" + algorithmName + ", parameters="
                + parameters + "]";
    }
}
