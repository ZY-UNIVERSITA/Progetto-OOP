package com.zysn.passwordmanager.model.authentication.registration.api;

import com.zysn.passwordmanager.model.account.entity.impl.CollectedUserData;

/**
 * RegistrationService is an interface that provides methods to register user data.
 * Implementations of this interface should handle the processing and storage of user registration information.
 */
public interface RegistrationService {
    
    /**
     * Registers the provided user data.
     * This method is responsible for accepting, processing, and storing the collected user data.
     *
     * @param collectedUserData The data collected from the user that needs to be registered.
     */
    public void register(CollectedUserData collectedUserData);
}

