package com.zysn.passwordmanager.model.authentication.login.api;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;

/**
 * The LoginService interface provides a method for user login.
 */
public interface LoginService {
    
    /**
     * Logs in the user with the provided collected user data.
     * 
     * @param collectedUserData the CollectedUserData object containing user data for login
     */
    public void login(CollectedUserData collectedUserData);
}

