package com.zysn.passwordmanager.controller.navigation.impl;

import com.zysn.passwordmanager.controller.navigation.api.GenericNavigatorAbstract;

import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * StepNavigator is a concrete implementation of {@link GenericNavigatorAbstract} for managing pane-based navigation.
 *
 * @param <S> the type of the data to be managed
 */
public class StepNavigator<S> extends GenericNavigatorAbstract<Pane, S> {

    /**
     * Constructor initializing the pane and data.
     *
     * @param pane the pane
     * @param data the data
     */
    public StepNavigator(Pane pane, S data) {
        super(pane, data);
    }
    
    /**
     * Sets the view for the specified scene.
     *
     * @param root the root node of the scene
     * @param title the title of the scene
     */
    @Override
    protected void setView(Parent root, String title) {
        super.getView().getChildren().clear();
        super.getView().getChildren().add(root);
    }   
}
