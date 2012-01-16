package edu.illinois.cs242.controller;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created Jon Tedesco
 * Date: Sep 26, 2010
 *
 */
public class ImageWindowKeyListener implements KeyListener {

    /**
     * Function called when a key is 'typed', not just pressed or released
     *
     * @param event The key event
     */
    public void keyTyped(KeyEvent event) {

        //Grab the key pressed
        char keyPressed = event.getKeyChar();

        //Get action factories for the file and edit menu
        MenuActionFactory fileMenuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.FILE_MENU);
        MenuActionFactory editMenuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.EDIT_MENU);
        MenuActionFactory zoomMenuActionFactory = MenuFactory.buildMenuActionFactory(MenuConstants.ZOOM_MENU);
        ActionListener action = null;

        //Grab the correct action listener from the factory
        if(keyPressed=='c' || keyPressed=='C'){

            //Create the 'copy' action
            action = fileMenuActionFactory.buildMenuAction(ButtonConstants.COPY_BUTTON);

        } else if(keyPressed=='r' || keyPressed=='R') {

            //Create the 'revert' action
            action = editMenuActionFactory.buildMenuAction(ButtonConstants.REVERT_BUTTON);

        } else  if(keyPressed=='q' || keyPressed=='Q' ||keyPressed==KeyEvent.VK_ESCAPE){

            //Create the 'exit' action
            action = fileMenuActionFactory.buildMenuAction(ButtonConstants.EXIT_BUTTON);

        } else if(keyPressed=='o' || keyPressed=='O') {

            //Create the 'open image' action
            action = fileMenuActionFactory.buildMenuAction(ButtonConstants.OPEN_DECOMPRESSED_IMAGE_BUTTON);

        } else if(keyPressed=='s' || keyPressed=='S') {

            //Create the 'save as image' action
            action = fileMenuActionFactory.buildMenuAction(ButtonConstants.DECOMPRESS_IMAGE_BUTTON);

        } else if(keyPressed==KeyEvent.VK_PRINTSCREEN || keyPressed=='p' || keyPressed=='P') {

            //Create the 'capture screen' action
            action = editMenuActionFactory.buildMenuAction(ButtonConstants.CAPTURE_SCREEN_BUTTON);

        } else if(keyPressed=='v' || keyPressed=='V') {

            //Create the 'paste' action
            action = editMenuActionFactory.buildMenuAction(ButtonConstants.PASTE_BUTTON);
        }  else if(keyPressed=='+' || keyPressed=='=') {

            //Create the 'zoom in' action
            action = zoomMenuActionFactory.buildMenuAction(ButtonConstants.ZOOM_IN_BUTTON);
        }  else if(keyPressed=='-' || keyPressed=='_') {

            //Create the 'zoom out' action
            action = zoomMenuActionFactory.buildMenuAction(ButtonConstants.ZOOM_OUT_BUTTON);
        }

        //Trigger the action
        if (action != null) {
            action.actionPerformed(null);
        }
    }

        /**
     * Called when a key is pushed down
     *
     * @param event The event triggerred
     */
    public void keyPressed(KeyEvent event) {}

    /**
     * Called when a key is released
     *
     * @param event The event that got triggerd
     */
    public void keyReleased(KeyEvent event) {}
}
