package com.zysn.passwordmanager.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.FileManager;

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

    public boolean loadServices(SecretKeySpec key, CryptoManager cryptoManager, FileManager fileManager) { return true; }
    public boolean saveServices(SecretKeySpec key, CryptoManager cryptoManager, FileManager fileManager) { return true; }

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
