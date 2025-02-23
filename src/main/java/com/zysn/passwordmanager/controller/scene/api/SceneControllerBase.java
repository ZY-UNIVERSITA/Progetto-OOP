package com.zysn.passwordmanager.controller.scene.api;

import com.zysn.passwordmanager.controller.navigation.ViewNavigator;
import com.zysn.passwordmanager.model.account.manager.AccountManager;

/**
 * AbstractSceneController provides a base implementation for SceneController
 * with common functionality for managing account manager and view navigator.
 */
public abstract class SceneControllerBase implements SceneController {
    private AccountManager accountManager;
    private ViewNavigator viewNavigator;

    /**
     * Sets the AccountManager for the scene.
     * @param accountManager the AccountManager to be set
     */
    @Override
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    /**
     * Sets the ViewNavigator for the scene.
     * @param viewNavigator the ViewNavigator to be set
     */
    @Override
    public void setViewNavigator(ViewNavigator viewNavigator) {
        this.viewNavigator = viewNavigator;
    }

    /**
     * Gets the AccountManager for the scene.
     * @return the current AccountManager
     */
    public AccountManager getAccountManager() {
        return accountManager;
    }

    /**
     * Gets the ViewNavigator for the scene.
     * @return the current ViewNavigator
     */
    public ViewNavigator getViewNavigator() {
        return viewNavigator;
    }
}
