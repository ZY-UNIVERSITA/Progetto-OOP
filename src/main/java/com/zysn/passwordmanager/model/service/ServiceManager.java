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

    // DA AGGIORNARE
    /**
     * Updates the details of an existing service in the list based on the provided parameters.
     * If a service with the specified name is found, it is replaced with a new instance
     * containing the updated details.
     *
     * @param serviceName the current name of the service to be modified
     * @param newName the new name to set for the service
     * @param newUsername the new username associated with the service
     * @param newEmail the new email associated with the service
     * @param newPassword the new password to store for the service
     * @param newInfo additional information or notes about the service
     * @return {@code true} if the service was found and successfully updated, {@code false} if no service with the specified name was found
     */
    public boolean modifyService(String serviceName, Service newService) {
        for (int i = 0; i < services.size(); i++) {
            Service service = services.get(i);
            if (service.getName().equals(serviceName)) {
                Service updatedService = new ServiceBuilder(this.user, this.cryptoManager)
                        .setName(newService.getName())
                        .setUsername(newService.getUsername())
                        .setEmail(newService.getEmail())
                        .setEncryptionConfig(newService.getEncryptionConfig())
                        .setPassword(newService.getPassword())
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

    // DA AGGIORNARE IL JAVADOC
    /**
     * Loads the services from a file and decrypts them using the provided key.
     * 
     * @param key the secret key used for decryption
     * @param cryptoManager the CryptoManager instance used for decryption
     * @param fileManager the FileManager instance used to read the services file
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

    // DA AGGIORNARE IL JAVADOC
    /**
     * Saves the services to a file after encrypting the data.
     * 
     * @param key the secret key used for encryption
     * @param cryptoManager the CryptoManager instance used for encryption
     * @param fileManager the FileManager instance used to write the services file
     * @return {@code true} if the services were successfully encrypted and saved, {@code false} otherwise
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
