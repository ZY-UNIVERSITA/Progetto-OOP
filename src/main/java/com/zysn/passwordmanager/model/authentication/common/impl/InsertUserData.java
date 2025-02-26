package com.zysn.passwordmanager.model.authentication.common.impl;

import org.bouncycastle.util.Arrays;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;
import com.zysn.passwordmanager.model.account.manager.api.SessionManager;
import com.zysn.passwordmanager.model.authentication.common.api.AuthenticationCollectingStepAbstract;

/**
 * InsertUserData is a step in the authentication process that collects and sets
 * the user's data such as username, password, and 2FA status.
 */
public class InsertUserData extends AuthenticationCollectingStepAbstract {
    
    /**
     * Constructor for InsertUserData.
     *
     * @param sessionManager   the session manager to manage user sessions.
     * @param collectedUserData the collected user data.
     */
    public InsertUserData(final SessionManager sessionManager, final CollectedUserData collectedUserData) {
        super(sessionManager, collectedUserData);
    }

    /**
     * Executes the step to set the user's data including username, password, and 2FA status.
     */
    @Override
    public void executeStep() {
        this.setUsername();
        this.setPassword();
        this.set2FA();
    }
    
    /**
     * Sets the username in the session manager.
     */
    private void setUsername() {
        final String username = super.getCollectedUserData().getUsername();
        super.getSessionManager().getUserAuthInfo().setUsername(username);
        super.getSessionManager().getUserAccount().setUsername(username);
    }

    /**
     * Sets the password in the session manager.
     */
    private void setPassword() {
        final byte[] password = Arrays.copyOf(super.getCollectedUserData().getPassword(), super.getCollectedUserData().getPassword().length);
        super.getSessionManager().getUserAuthKey().setPassword(password);
    }

    /**
     * Sets the 2FA status in the session manager.
     */
    private void set2FA() {
        final boolean status2FA = super.getCollectedUserData().isEnabled2FA();
        super.getSessionManager().getUserAuthInfo().setEnabled2FA(status2FA);
    }
}

