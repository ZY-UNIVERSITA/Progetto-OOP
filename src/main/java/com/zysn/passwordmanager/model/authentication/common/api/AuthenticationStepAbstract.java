package com.zysn.passwordmanager.model.authentication.common.api;

import com.zysn.passwordmanager.model.account.manager.api.SessionManager;

/**
 * AuthenticationStepAbstract is an abstract class that implements the AuthenticationStep interface.
 * It serves as a base class for authentication steps, providing common functionality related to session management.
 */
public abstract class AuthenticationStepAbstract implements AuthenticationStep {
    private SessionManager sessionManager;

    /**
     * Constructs an AuthenticationStepAbstract with the specified session manager.
     *
     * @param sessionManager The session manager to be used by this step.
     */
    public AuthenticationStepAbstract(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * Returns the session manager.
     *
     * @return The session manager.
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * Sets the session manager.
     *
     * @param sessionManager The session manager to be set.
     */
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
}
