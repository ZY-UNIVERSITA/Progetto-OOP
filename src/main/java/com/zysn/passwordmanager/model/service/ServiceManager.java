package com.zysn.passwordmanager.model.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zysn.passwordmanager.model.security.algorithm.config.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.FileManager;
import com.zysn.passwordmanager.model.utils.PasswordGenerator;

/**
 * Singleton class that manages a list of services.
 * Ensures that only one instance of ServiceManager exists throughout the application.
 * Use {@link #getInstance()} to access the instance.
 */
public class ServiceManager {
    private static ServiceManager instance;
    private List<Service> services;

    private ServiceManager() {
        this.services = new ArrayList<>();
    }

    /**
     * Returns the single instance of ServiceManager.
     * If the instance does not exist, it is created.
     *
     * @return the singleton instance of ServiceManager
     */
    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    /**
     * Resets the singleton instance for testing purposes.
     * This method should only be used in unit tests.
     * 
     */
    public static void resetInstance() {
        instance = null;
    }

    /**
     * Lists all available services.
     * @return services
     */
    public List<Service> getServices() {
        return services;
    }

    /**
     * Select service by its name.
     * @param serviceName
     * @return service
     */
    public Service selectService (String serviceName) {
        for (Service ser : services) {
            if (ser.getName().equals(serviceName)) {
                return ser;
            }
        }
        return null;
    }

    /**
     * Add a new service.
     * @param service
     * @return {@code true} if addition was successful, {@code false} otherwise
     */
    public boolean addService(Service service) {
        return services.add(service);
    }
    
    /**
     * Remove the specific service.
     * @param service the name of service to remove
     * @return {@code true} if removal was successful, {@code false} otherwise
     */
    public boolean removeService(String serviceName) {
        return services.removeIf(service -> service.getName().equals(serviceName));
    }
    
    /**
     * Search for the service by letter or keyword.
     * @param searchTerm the letter or keyword of service to search
     * @return the list of services that meets the requirements
     */
    public List<Service> searchService(String searchTerm) {
        List<Service> res = new ArrayList<>();
        for (Service service : services) {
            if (service.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                res.add(service);
            }
        }
        return res;
    }

    /**
     * Modifies the details of an existing service in the list of services,
     * with the data from the provided Service object.
     *
     * @param serviceName the name of the service to modify
     * @param newService the Service object containing the new details to update
     * @return {@code true} if the service was found and modified, {@code false} if no service with the specified name was found
     */
    public boolean modifyService(String serviceName, Service newService) {
        for (Service service : services) {
            if (service.getName().equals(serviceName)) {
                service.setName(newService.getName());
                service.setUsername(newService.getUsername());
                service.setEmail(newService.getEmail());
                service.setPassword(newService.getPassword());
                service.setEncryptionConfig(newService.getEncryptionConfig());
                service.setInfo(newService.getInfo());
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a password based on the provided criteria and assigns it to a service.
     * 
     * @param serviceName The name of the service.
     * @param length The desired length of the generated password.
     * @param useSpecialChar Whether or not to include special characters.
     * @param useNumbers Whether or not to include digits.
     * @param useUpperCase Whether or not to include uppercase letters.
     * @param useLowerCase Whether or not to include lowercase letters.
     * @return {@code true} if the password was successfully generated and added to the service, {@code false} otherwise.
     */
    public boolean generateAndAddPasswordToService(String serviceName, int length, boolean useSpecialChar, boolean useNumbers, boolean useUpperCase, boolean useLowerCase) {
        PasswordGenerator generator = new PasswordGenerator();
        char[] generatedPassword = generator.generatePassword(length, useSpecialChar, useNumbers, useUpperCase, useLowerCase);
        
        Service service = selectService(serviceName);
        if (service != null) {
            service.setPassword(new String(generatedPassword).getBytes());
            return true;
        }
        return false;
    }

    /**
     * Loads the services from a file and decrypts them using the provided key.
     * 
     * @param key the secret key used for decryption
     * @param cryptoManager the CryptoManager instance used for decryption
     * @param fileManager the FileManager instance used to read the services file
     * @return {@code true} if the services were successfully loaded and decrypted, {@code false} otherwise
     */
    public boolean loadServices(SecretKeySpec key, CryptoManager cryptoManager, FileManager fileManager) {
        byte[] encryptedData = fileManager.loadServicesFile("services.dat".toCharArray());
        if (encryptedData == null) {
            return false;
        }

        byte[] decryptedData = cryptoManager.decrypt(encryptedData, key, new AlgorithmConfig("AES", "CBC"));
        if (decryptedData == null) {
            return false;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Service> loadedServices = objectMapper.readValue(decryptedData, new TypeReference<List<Service>>(){});
            this.services = loadedServices;
        } catch (IOException e) {
            System.err.println("Error deserializing services data: " + e.getMessage());
            return false;
        }
        
        return true;
    }

    /**
     * Saves the services to a file after encrypting the data.
     * 
     * @param key the secret key used for encryption
     * @param cryptoManager the CryptoManager instance used for encryption
     * @param fileManager the FileManager instance used to write the services file
     * @return {@code true} if the services were successfully encrypted and saved, {@code false} otherwise
     */
    public boolean saveServices(SecretKeySpec key, CryptoManager cryptoManager, FileManager fileManager) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            byte[] encryptedData = objectMapper.writeValueAsBytes(services);
            byte[] cipherText = cryptoManager.encrypt(encryptedData, key, new AlgorithmConfig("AES", "CBC"));
            
            return fileManager.saveServicesFile(cipherText);
        } catch (IOException e) {
            System.err.println("Error serializing services data: " + e.getMessage());
            return false;
        }
    }
    

    @Override
    public String toString() {
        return "ServiceManager [services=" + services + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((services == null) ? 0 : services.hashCode());
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
        ServiceManager other = (ServiceManager) obj;
        if (services == null) {
            if (other.services != null)
                return false;
        } else if (!services.equals(other.services))
            return false;
        return true;
    }
    
}
