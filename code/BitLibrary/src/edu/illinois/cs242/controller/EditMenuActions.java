package edu.illinois.cs242.controller;

import edu.illinois.cs242.model.ImageUtilities;
import edu.illinois.cs242.view.ImageWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 23, 2010

 * <p> <p> Enum that allows us to hash a key, to a specific <code>ActionListener</code>.
 *
 * NOTE: I took some of the effects code from CS125, some from http://www.javaworld.com/javaworld/jw-09-1998/jw-09-media.html?page=3
 *
 * @see java.awt.event.ActionListener
 * @see edu.illinois.cs242.controller.ButtonConstants
 *
 */

public enum EditMenuActions {

    //Edit menu
    //Action of copying the current, full-sized image to clipboard
    copyButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    BufferedImage image = ImageWindow.getImage();
                    ImageUtilities.setClipboardImage(image);
                }
            };
        }
    },

    //Action of pasting the current clipboard to the frame
    pasteButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    BufferedImage image = ImageUtilities.getClipboardImage();
                    if (image != null){
                        ImageWindow.openImage(image, "Clipboard", true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Clipboard was empty!");
                    }
                }
            };
        }
    },


    //The action of reverting the current image back to the last-opened state
    revertButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    BufferedImage originalImage = ImageWindow.getOriginalImage();
                    ImageWindow.openImage(originalImage, null, true);
                }
            };
        }
    },

    //Action of placing a picture of the current screen into the window
    captureScreenButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    BufferedImage img = ImageUtilities.captureScreen();
                    if (img != null)
                        ImageWindow.openImage(img, "Screenshot", true);
                }
            };
        }
    };

    /**
     * Abstract method that we overload in each enum to create the actual <code>ActionListener</code> object.
     * @return An action listener that will be applied to a menu item.
     */
    abstract ActionListener getActionListener();

}
