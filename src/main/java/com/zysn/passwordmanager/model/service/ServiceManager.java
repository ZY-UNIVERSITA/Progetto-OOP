package com.zysn.passwordmanager.model.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.account.entity.impl.UserAccount;
import com.zysn.passwordmanager.model.enums.ExtensionsConstant;
import com.zysn.passwordmanager.model.enums.PathsConstant;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.manager.CryptoManager;
import com.zysn.passwordmanager.model.utils.crypto.CryptoUtils;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;
import com.zysn.passwordmanager.model.utils.file.api.FileManager;
import com.zysn.passwordmanager.model.utils.file.impl.DefaultFileManager;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;
import com.zysn.passwordmanager.model.utils.security.impl.PasswordGenerator;

/**
 * Singleton class that manages a list of services.
 * Ensures that only one instance of ServiceManager exists throughout the application.
 * Use {@link #getInstance()} to access the instance.
 */
public class ServiceManager implements MustBeDestroyed {
    private static ServiceManager instance;
    private List<Service> services;
    private UserAccount user;
    private CryptoManager cryptoManager;
    private FileManager fileManager;
    private AlgorithmConfig servicesListEncryptionConfig;
    private String fileName;

    private ServiceManager() {
        this.services = new ArrayList<>();
        this.fileManager = new DefaultFileManager(PathsConstant.SERVICE, ExtensionsConstant.ENC);
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

    // Setter methods to inject dependencies
    public void setUserAccount (UserAccount user) {
        this.user = user;
    }

    public void setCryptoManager (CryptoManager crypto) {
        this.cryptoManager = crypto;
    }

    public void setAlgorithmConfig(AlgorithmConfig servicesListEncryptionConfig) {
        this.servicesListEncryptionConfig = servicesListEncryptionConfig;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public CryptoManager getCryptoManager () {
        return this.cryptoManager;
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
     * Updates the details of an existing service with the details of service passed as an argument.
     *
     * @param serviceName the current name of the service to be modified
     * @param newService the service with new details
     * @return {@code true} if the service was successfully updated, {@code false} otherwise
     */
    public boolean modifyService(String serviceName, Service newService) {
        for (int i = 0; i < services.size(); i++) {
            Service service = services.get(i);
            if (service.getName().equals(serviceName)) {
                String password = getDecryptedPassword(newService);
                Service updatedService = new ServiceBuilder(this.user, this.cryptoManager)
                        .setName(newService.getName())
                        .setUsername(newService.getUsername())
                        .setEmail(newService.getEmail())
                        .setEncryptionConfig(newService.getEncryptionConfig())
                        .setPassword(password.getBytes())
                        .setInfo(newService.getInfo())
                        .build();

                services.set(i, updatedService);
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a decrypted password from the specified service.
     * @param service the service from which the password is decrypted
     * @return the decrypted password as string
     */
    public String getDecryptedPassword(Service service) {
        byte[] decryptedBytes = this.cryptoManager.decrypt(
            service.getPassword(),
            new SecretKeySpec(this.user.getMasterKey(), service.getEncryptionConfig().getAlgorithmName()),
            service.getEncryptionConfig()
            );
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * Generates a random password based on given criteria.
     *
     * @param length         password length
     * @param useSpecialChar include special characters
     * @param useNumbers     include numbers
     * @param useUpperCase   include uppercase letters
     * @param useLowerCase   include lowercase letters
     * @return generated password as a char array
     */
    public char[] generatePassword(int length, boolean useSpecialChar, boolean useNumbers, boolean useUpperCase, boolean useLowerCase) {
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword(length, useSpecialChar, useNumbers, useUpperCase, useLowerCase);
    }

    /**
     * Loads the services from a file and decrypts them.
     * 
     * @return {@code true} if the services were successfully loaded and decrypted, {@code false} otherwise
     */
    public boolean loadServices() {
        byte[] encryptedData = fileManager.loadData(fileName);
        if (encryptedData == null) {
            return false;
        }

        byte[] decryptedData = cryptoManager.decrypt(encryptedData, new SecretKeySpec(user.getMasterKey(), servicesListEncryptionConfig.getAlgorithmName()), servicesListEncryptionConfig);
        if (decryptedData == null) {
            return false;
        }

        this.services = EncodingUtils.deserializeData(decryptedData, new TypeReference<List<Service> >() {});
        
        if (this.services == null) {
            return false;
        }

        return true;
    }

    /**
     * Saves the services to a file after encrypting the data.
     */
    public void saveServices() {
        byte[] servicesList = EncodingUtils.serializeData(this.services);

        try {    
            byte[] cipherText = cryptoManager.encrypt(servicesList, new SecretKeySpec(user.getMasterKey(), servicesListEncryptionConfig.getAlgorithmName()), servicesListEncryptionConfig);

            this.fileManager.saveData(fileName, cipherText);
        } finally {
            CryptoUtils.cleanMemory(servicesList);
        }
    }

    @Override
    public void destroy() {
        this.services.forEach(MustBeDestroyed::destroy);
        this.services.clear();
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
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((cryptoManager == null) ? 0 : cryptoManager.hashCode());
        result = prime * result
                + ((servicesListEncryptionConfig == null) ? 0 : servicesListEncryptionConfig.hashCode());
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
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
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (cryptoManager == null) {
            if (other.cryptoManager != null)
                return false;
        } else if (!cryptoManager.equals(other.cryptoManager))
            return false;
        if (servicesListEncryptionConfig == null) {
            if (other.servicesListEncryptionConfig != null)
                return false;
        } else if (!servicesListEncryptionConfig.equals(other.servicesListEncryptionConfig))
            return false;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        return true;
    }   
}
