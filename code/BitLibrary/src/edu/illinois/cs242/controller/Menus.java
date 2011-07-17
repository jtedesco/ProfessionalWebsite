package edu.illinois.cs242.controller;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 23, 2010
 *
 * <p> <p> Enum that hashes button constants to concrete factories to implement the abstract factory
 */
public enum Menus {

    fileMenu {
        @Override
        MenuActionFactory getMenuFactory() {
            return new FileMenuActionFactory();
        }
     },

    editMenu {
        @Override
        MenuActionFactory getMenuFactory() {
            return new EditMenuActionFactory();
        }
     },

    effectsMenu {
        @Override
        MenuActionFactory getMenuFactory() {
            return new EffectsMenuActionFactory();
        }
     },

    zoomMenu {
        @Override
        MenuActionFactory getMenuFactory() {
            return new ZoomMenuActionFactory();
        }
     },

    algorithmsMenu {
        @Override
        MenuActionFactory getMenuFactory() {
            return new AlgorithmsMenuActionFactory();
        }
    };

    /**
     * Abstract method that we overload in each enum to create the actual <code>ActionListener</code> object.
     * @return An action listener that will be applied to a menu item.
     */
    abstract MenuActionFactory getMenuFactory();

}
