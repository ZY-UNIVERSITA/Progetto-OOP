package com.zysn.passwordmanager.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.FileManager;

public class ServiceManager {
    
    private List<Service> services;

    public ServiceManager() {
        this.services = new ArrayList<>();
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
     * @return true if addition was successful, false otherwise
     */
    public boolean addService(Service service) {
        return services.add(service);
    }
    
    /**
     * Remove the specific service.
     * @param service the name of service to remove
     */
    public void removeService(String serviceName) {
        services.removeIf(service -> service.getName().equals(serviceName));
    }
    
    /**
     * Search for the service by letter or keyword.
     * @param searchTerm the letter or keyword of service to search
     * @return the list of services that meets the requirements
     */
    public List<Service> searchService(String searchTerm) {
        List<Service> res = new ArrayList<>();
        for (Service service : services) {
            if (service.getName().contains(searchTerm)) {
                res.add(service);
            }
        }
        return res;
    }

    public void modifyService(String serviceName, Service newService) {}
    public void loadServices(SecretKeySpec key, CryptoManager cryptoManager, FileManager fileManager) {}
    public void saveServices(SecretKeySpec key, CryptoManager cryptoManager, FileManager fileManager) {}
}
