package com.zysn.passwordmanager.model.account.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.zysn.passwordmanager.model.account.entity.impl.UserAccount;
import com.zysn.passwordmanager.model.account.entity.impl.UserAuthInfo;
import com.zysn.passwordmanager.model.account.entity.impl.UserAuthKey;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.keystore.impl.KeyStoreConfig;
import com.zysn.passwordmanager.model.authentication.passwordlist.impl.ServiceCryptoConfig;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;

/**
 * DefaultSessionManager class that implements the SessionManager interface
 * to manage user sessions with a default implementation.
 */
public class DefaultSessionManager implements SessionManager, MustBeDestroyed {
    private UserAccount userAccount;
    private UserAuthInfo userAuthInfo;
    private UserAuthKey userAuthKey;
    private KeyStoreConfig keyStoreConfig;
    private ServiceCryptoConfig serviceConfig;

    /**
     * Constructs a DefaultSessionManager with default values.
     */
    public DefaultSessionManager() {
        this.userAccount = new UserAccount();
        this.userAuthInfo = new UserAuthInfo();
        this.userAuthKey = new UserAuthKey();
        this.keyStoreConfig = new KeyStoreConfig();
        this.serviceConfig = new ServiceCryptoConfig();
    }


    /**
     * Destroy current session data.
     */
    @Override
    public void destroy() {
        final List<MustBeDestroyed> destroyElements = new ArrayList<>();
        destroyElements.add(this.getKeyStoreConfig());
        destroyElements.add(this.getServiceConfig());
        destroyElements.add(this.getUserAccount());
        destroyElements.add(this.getUserAuthInfo());
        destroyElements.add(this.getUserAuthKey());

        destroyElements.forEach(MustBeDestroyed::destroy);

    }

    /* GETTER AND SETTER */
    public UserAuthInfo getUserAuthInfo() {
        return userAuthInfo;
    }

    public void setUserAuthInfo(final UserAuthInfo userAuthInfo) {
        this.userAuthInfo = userAuthInfo;
    }

    public UserAuthKey getUserAuthKey() {
        return userAuthKey;
    }

    public void setUserAuthKey(final UserAuthKey userAuthKey) {
        this.userAuthKey = userAuthKey;
    }

    public KeyStoreConfig getKeyStoreConfig() {
        return keyStoreConfig;
    }

    public void setKeyStoreConfig(final KeyStoreConfig keyStoreConfig) {
        this.keyStoreConfig = keyStoreConfig;
    }

    public ServiceCryptoConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(final ServiceCryptoConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(final UserAccount userAccount) {
        this.userAccount = userAccount;
    }

}
