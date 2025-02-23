package com.zysn.passwordmanager.model.authentication.common.api;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;

/**
 * AuthenticationCollectingStepAbstract is an abstract class that extends AuthenticationStepAbstract.
 * It serves as a base class for authentication steps that require collected user data.
 */
public abstract class AuthenticationCollectingStepAbstract extends AuthenticationStepAbstract {
    private CollectedUserData collectedUserData;

    /**
     * Constructs an AuthenticationCollectingStepAbstract with the specified session manager and collected user data.
     *
     * @param sessionManager The session manager to be used by this step.
     * @param collectedUserData The collected user data required by this step.
     */
    public AuthenticationCollectingStepAbstract(SessionManager sessionManager, CollectedUserData collectedUserData) {
        super(sessionManager);
        this.collectedUserData = collectedUserData;
    }

    /**
     * Returns the collected user data.
     *
     * @return The collected user data.
     */
    public CollectedUserData getCollectedUserData() {
        return collectedUserData;
    }

    /**
     * Sets the collected user data.
     *
     * @param collectedUserData The collected user data to be set.
     */
    public void setCollectedUserData(CollectedUserData collectedUserData) {
        this.collectedUserData = collectedUserData;
    }
}
