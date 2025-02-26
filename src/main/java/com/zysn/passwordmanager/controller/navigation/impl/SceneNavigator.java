package com.zysn.passwordmanager.controller.navigation.impl;

import com.zysn.passwordmanager.controller.navigation.api.GenericNavigatorAbstract;
import com.zysn.passwordmanager.model.account.manager.api.AccountManager;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SceneNavigator is a concrete implementation of {@link GenericNavigatorAbstract} for managing scene navigation.
 *
 * @param stage the stage on which scenes are set
 * @param AccountManager the account manager providing data to scenes
 */
public class SceneNavigator extends GenericNavigatorAbstract<Stage, AccountManager> {

    /**
     * Constructor initializing the stage and account manager.
     *
     * @param stage the stage
     * @param AccountManager the account manager
     */
    public SceneNavigator(final Stage stage, final AccountManager AccountManager) {
        super(stage, AccountManager);
    }

    /**
     * Sets the view for the specified scene.
     *
     * @param root the root node of the scene
     * @param title the title of the scene
     */
    @Override
    protected void setView(final Parent root, final String title) {
        super.getView().setScene(new Scene(root));
        super.getView().setTitle(title);
        super.getView().show();
    } 
}

