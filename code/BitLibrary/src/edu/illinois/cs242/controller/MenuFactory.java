package edu.illinois.cs242.controller;

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
public class MenuFactory {

    /**
     * <p> Factory method for creating the <code>MenuActionFactory</code>. The factory takes as input a constant
     * string from <code>ButtonConstants</code>, and hashes that value to a specific ActionListener.
     *
     * @param menuConstant A string constant identifying the specific menu item in the GUI that this <code>MenuActionFactory</code> will pertain to
     * @return An <code>MenuActionFactory</code> object that will be the factory for buttons with the menu key <code>menuConstant</code>
     *
     * @see java.awt.event.ActionListener
     * @see MenuConstants
     */
    public static MenuActionFactory buildMenuActionFactory(String menuConstant){
        try{
            return Menus.valueOf(menuConstant).getMenuFactory();
        } catch (Exception e){
            throw new UnsupportedOperationException("Error getting menu factory:\n" + e.getMessage());
        }
    }
}
