package com.zysn.passwordmanager.model.authentication.passwordlist.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigManager;
import com.zysn.passwordmanager.model.authentication.passwordlist.api.ServiceCryptoConfigService;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfig;
import com.zysn.passwordmanager.model.security.algorithm.config.impl.AlgorithmConfigFactory;
import com.zysn.passwordmanager.model.utils.encoding.EncodingUtils;

/**
 * Default implementation of the ServiceCryptoConfigManager interface.
 */
public class DefaultServiceCryptoConfigManager implements ServiceCryptoConfigManager {
    private final SessionManager sessionManager;

    private final ServiceCryptoConfigService passwordListConfigService;

    /**
     * Constructs a new DefaultServiceCryptoConfigManager with the specified session manager.
     * 
     * @param sessionManager the session manager to use
     */
    public DefaultServiceCryptoConfigManager(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        
        this.passwordListConfigService = new DefaultServiceCryptoConfigService();
    }

    /**
     * Creates a new service configuration and sets it in the session manager.
     */
    @Override
    public void createServiceConfig() {
        final ServiceCryptoConfig serviceConfig = this.passwordListConfigService.createPasswordListConfig();
        this.sessionManager.setServiceConfig(serviceConfig);
    }

    /**
     * Encrypts the current service configuration and stores the encrypted data in the session manager.
     */
    @Override
    public void encryptConfig() {
        final byte[] serializeData = EncodingUtils.serializeData(this.sessionManager.getServiceConfig());
        
        final byte[] key = this.sessionManager.getUserAuthKey().getServiceConfigKey();
        final byte[] salt = this.sessionManager.getKeyStoreConfig().getServiceDecryptionSalt();
        final AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", salt, null);

        final byte[] encryptedData = this.passwordListConfigService.encryptConfig(serializeData, key, algorithmConfig);
        this.sessionManager.getUserAuthInfo().setServiceConfigEncryptedData(encryptedData);
    }

    /**
     * Decrypts the encrypted service configuration data and sets the decrypted configuration in the session manager.
     */
    @Override
    public void decryptConfig() {
        final byte[] encryptedData = this.sessionManager.getUserAuthInfo().getServiceConfigEncryptedData();

        final byte[] key = this.sessionManager.getUserAuthKey().getServiceConfigKey();
        final byte[] salt = this.sessionManager.getKeyStoreConfig().getServiceDecryptionSalt();
        final AlgorithmConfig algorithmConfig = AlgorithmConfigFactory.createAlgorithmConfig("AES", salt, null);

        final byte[] decryptedData = this.passwordListConfigService.decryptConfig(encryptedData, key, algorithmConfig);
        
        final ServiceCryptoConfig serviceConfig = EncodingUtils.deserializeData(decryptedData, new TypeReference<ServiceCryptoConfig>() {
            
        });

        this.sessionManager.setServiceConfig(serviceConfig);
    }   
}
