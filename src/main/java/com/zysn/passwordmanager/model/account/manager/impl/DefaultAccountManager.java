package com.zysn.passwordmanager.model.account.manager.impl;

import java.util.ArrayList;
import java.util.List;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.login.api.LoginService;
import com.zysn.passwordmanager.model.authentication.registration.api.RegistrationService;
import com.zysn.passwordmanager.model.service.ServiceManager;
import com.zysn.passwordmanager.model.utils.security.api.MustBeDestroyed;

/**
 * DefaultAccountManager class that implements the AccountManager interface
 * to manage user accounts with a default implementation.
 */
public class DefaultAccountManager implements AccountManager {
    private SessionManager sessionManager;
    private ServiceManager serviceManager;
    private final RegistrationService registrationService;
    private final LoginService loginService;

    /**
     * Constructs a DefaultAccountManager with the provided session manager,
     * service manager, registration service, and login service.
     *
     * @param sessionManager      The session manager for managing user sessions.
     * @param serviceManager      The service manager for managing services.
     * @param registrationService The registration service for registering users.
     * @param loginService        The login service for logging in users.
     */
    public DefaultAccountManager(final SessionManager sessionManager, final ServiceManager serviceManager,
            final RegistrationService registrationService,
            final LoginService loginService) {
        this.sessionManager = sessionManager;
        this.serviceManager = serviceManager;
        this.registrationService = registrationService;
        this.loginService = loginService;
    }

    /**
     * Registers a new user with the provided collected user data.
     *
     * @param collectedUserData The data collected from the user required for
     *                          registration.
     */
    @Override
    public void register(final CollectedUserData collectedUserData) {
        this.registrationService.register(collectedUserData);
        
        collectedUserData.destroy();
    }

    /**
     * Logs in an existing user with the provided collected user data.
     *
     * @param collectedUserData The data collected from the user required for login.
     */
    @Override
    public void login(final CollectedUserData collectedUserData) {
        this.loginService.login(collectedUserData);

        collectedUserData.destroy();
    }

    /**
     * Logs out the currently logged-in user.
     */
    public void logout() {
        List<MustBeDestroyed> destroyElements = new ArrayList<>();
        destroyElements.add(this.sessionManager.getKeyStoreConfig());
        destroyElements.add(this.sessionManager.getServiceConfig());
        destroyElements.add(this.sessionManager.getUserAccount());
        destroyElements.add(this.sessionManager.getUserAuthInfo());
        destroyElements.add(this.sessionManager.getUserAuthKey());
        destroyElements.add(this.serviceManager);

        destroyElements.forEach(MustBeDestroyed::destroy);

    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(final SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(final ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}