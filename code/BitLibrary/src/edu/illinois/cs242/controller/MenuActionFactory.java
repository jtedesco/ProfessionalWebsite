package edu.illinois.cs242.controller;

import java.awt.event.ActionListener;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 23, 2010
 *
 * <p> <p> Factory for creating the <code>ActionListener</code> for a menu item. The factory takes as input a constant
 * string from <code>ButtonConstants</code>, and hashes that value to a specific ActionListener.
 *
 * @see java.awt.event.ActionListener
 * @see edu.illinois.cs242.controller.ButtonConstants
 */
public interface MenuActionFactory {

    /**
     * <p> Factory method for creating the <code>ActionListener</code> for a menu item. The factory takes as input a constant
     * string from <code>ButtonConstants</code>, and hashes that value to a specific ActionListener.
     *
     * @param buttonConstant A string constant identifying the specific menu item in the GUI that this <code>ActionListener</code> will pertain to
     * @return An <code>ActionListener</code> object that will become the action listener for the menu button with key <code>buttonConstant</code>
     *
     * @see java.awt.event.ActionListener
     * @see edu.illinois.cs242.controller.ButtonConstants
     */
    public ActionListener buildMenuAction(String buttonConstant);
}
