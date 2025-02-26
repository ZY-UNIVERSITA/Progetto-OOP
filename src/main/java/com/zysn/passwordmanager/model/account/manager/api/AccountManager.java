package com.zysn.passwordmanager.model.account.manager.api;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.service.ServiceManager;

/**
 * AccountManager interface to manage user accounts and their sessions.
 */
public interface AccountManager {

    /**
     * Registers a new user with the provided collected user data.
     *
     * @param collectedUserData The data collected from the user required for registration.
     */
    public void register(CollectedUserData collectedUserData);

    /**
     * Logs in an existing user with the provided collected user data.
     *
     * @param collectedUserData The data collected from the user required for login.
     */
    public void login(CollectedUserData collectedUserData);

    /**
     * Logs out the currently logged-in user.
     *
     * @return true if the logout was successful, false otherwise.
     */
    public void logout();

    /**
     * Gets the session manager associated with the user account.
     *
     * @return The session manager for the user account.
     */
    public SessionManager getSessionManager();

    /**
     * Gets the service manager associated with the user account.
     *
     * @return The service manager for the user account.
     */
    public ServiceManager getServiceManager();
}

