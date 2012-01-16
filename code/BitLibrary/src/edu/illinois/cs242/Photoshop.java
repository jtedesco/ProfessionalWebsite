package edu.illinois.cs242;

import edu.illinois.cs242.controller.ImageWindowKeyListener;
import edu.illinois.cs242.model.ImageUtilities;
import edu.illinois.cs242.view.ImageWindow;
import edu.illinois.cs242.view.LabelConstants;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 26, 2010
 *
 * <p> <p> The main entry point for the ImageWindow GUI
 */
public class Photoshop {

    /**
     * Runs the application starting with the ImageWindow
     */
   public static void main(String[] args) {

        //Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {/*Ignore, just defaults to 'metal' look*/}

        //Build the new frame, its basic attributes, and menus
        JFrame frame = new JFrame();
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(LabelConstants.TITLE);
        ImageWindow.addMenus(frame);

        //Add the key press listener
        frame.addKeyListener(new ImageWindowKeyListener());

        //Record the frame in the ImageWindow class
        ImageWindow.setFrame(frame);

        //Load the splash image
        BufferedImage splash = ImageUtilities.loadImage("images/logo.jpg");
        ImageWindow.setOriginalImage(splash);
        ImageWindow.setImage(splash);
        ImageWindow.setImageForFrame(frame, splash, true);

        //Show the window
        frame.setVisible(true);
        frame.setResizable(false);
   }
}
