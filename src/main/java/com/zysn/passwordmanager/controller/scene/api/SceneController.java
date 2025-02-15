package com.zysn.passwordmanager.controller.scene.api;

import com.zysn.passwordmanager.controller.navigation.ViewNavigator;
import com.zysn.passwordmanager.model.account.manager.AccountManager;

/**
 * SceneController interface for managing scene transitions
 * and interactions with account manager.
 */
public interface SceneController {

    /**
     * Sets the ViewNavigator for the scene.
     * @param viewNavigator the ViewNavigator to be set
     */
    void setViewNavigator(ViewNavigator viewNavigator);

    /**
     * Sets the AccountManager for the scene.
     * @param accountManager the AccountManager to be set
     */
    void setAccountManager(AccountManager accountManager);
}
