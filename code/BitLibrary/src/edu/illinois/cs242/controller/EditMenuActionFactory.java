package edu.illinois.cs242.controller;

import java.awt.event.ActionListener;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 23, 2010
 *
 * <p> <p> Factory for creating a <code>MenuActionFactory</code> based on the corresponding JMenu. The factory takes as input a constant
 * string from <code></code>, and hashes that value to a specific factory type.
 *
 * @see java.awt.event.ActionListener
 * @see edu.illinois.cs242.controller.MenuConstants
 */
public class EditMenuActionFactory implements MenuActionFactory{

    /**
     * {@inheritDoc}
     *
     * This implementation deals with the edit menu specifically
     */
    public ActionListener buildMenuAction(String buttonConstant){
        try{
            return EditMenuActions.valueOf(buttonConstant).getActionListener();
        } catch (Exception e){
            throw new UnsupportedOperationException("Error getting edit menu actions:\n" + e.getMessage());
        }
    }
}
