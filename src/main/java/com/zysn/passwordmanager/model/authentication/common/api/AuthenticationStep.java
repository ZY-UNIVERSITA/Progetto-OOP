package com.zysn.passwordmanager.model.authentication.common.api;

/**
 * AuthenticationStep is an interface that defines a step in the authentication process.
 * Implementations of this interface should provide the logic for executing a specific step in the overall 
 * authentication sequence.
 */
public interface AuthenticationStep {
    
    /**
     * Executes the authentication step.
     * This method contains the actions that need to be performed as part of the authentication process.
     */
    public void executeStep();    
}
