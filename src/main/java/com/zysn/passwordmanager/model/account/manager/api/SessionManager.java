package com.zysn.passwordmanager.model.account.manager.api;

import com.zysn.passwordmanager.model.account.entity.impl.UserAccount;
import com.zysn.passwordmanager.model.account.entity.impl.UserAuthInfo;
import com.zysn.passwordmanager.model.account.entity.impl.UserAuthKey;
import com.zysn.passwordmanager.model.authentication.keystore.impl.KeyStoreConfig;
import com.zysn.passwordmanager.model.authentication.passwordlist.impl.ServiceCryptoConfig;

/**
 * SessionManager interface to manage user session data and configurations.
 */
public interface SessionManager {

    /**
     * Gets the user authentication information for the session.
     *
     * @return The user authentication information.
     */
    public UserAuthInfo getUserAuthInfo();

    /**
     * Sets the user authentication information for the session.
     *
     * @param userAuthInfo The user authentication information.
     */
    public void setUserAuthInfo(UserAuthInfo userAuthInfo);

    /**
     * Gets the user authentication key for the session.
     *
     * @return The user authentication key.
     */
    public UserAuthKey getUserAuthKey();

    /**
     * Sets the user authentication key for the session.
     *
     * @param userAuthKey The user authentication key.
     */
    public void setUserAuthKey(UserAuthKey userAuthKey);

    /**
     * Gets the key store configuration for the session.
     *
     * @return The key store configuration.
     */
    public KeyStoreConfig getKeyStoreConfig();

    /**
     * Sets the key store configuration for the session.
     *
     * @param keyStoreConfig The key store configuration.
     */
    public void setKeyStoreConfig(KeyStoreConfig keyStoreConfig);

    /**
     * Gets the service cryptographic configuration for the session.
     *
     * @return The service cryptographic configuration.
     */
    public ServiceCryptoConfig getServiceConfig();
    
    /**
     * Sets the service cryptographic configuration for the session.
     *
     * @param serviceConfig The service cryptographic configuration.
     */
    public void setServiceConfig(ServiceCryptoConfig serviceConfig);

    /**
     * Gets the user account associated with the session.
     *
     * @return The user account.
     */
    public UserAccount getUserAccount();

    /**
     * Sets the user account associated with the session.
     *
     * @param userAccount The user account.
     */
    public void setUserAccount(UserAccount userAccount);
}
