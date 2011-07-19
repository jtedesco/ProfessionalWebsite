package edu.illinois.cs242.controller;

import edu.illinois.cs242.view.ImageWindow;

import java.awt.*;
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
 * @see ButtonConstants
 *
 */

public enum ZoomMenuActions {

    //Zoom buttons
    //Action of zooming in 10% on the given image
    zoomInButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image currently in the frame
                    BufferedImage image = ImageWindow.getImage();
                    double currentScale = ImageWindow.getImageScale();

                    //Zoom in 10%
                    currentScale += 0.15;
                    ImageWindow.setImageScale(currentScale);
                    int scaledWidth = (int)(currentScale*image.getWidth());
                    int scaledHeight = (int)(currentScale*image.getHeight());

                    // Paint scaled version of image to new image
                    BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics2D = scaledImage.createGraphics();
                    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    graphics2D.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
                    graphics2D.dispose();

                    //Replace the current image with this new scaled one
                    ImageWindow.openImage(scaledImage, null, false);
                 }
            };
        }
    },

   //Action of zooming out 10% on the current iamge
    zoomOutButton {
        @Override
        ActionListener getActionListener() {
            return new ActionListener() {
                public void actionPerformed(ActionEvent ae) {

                    //Get the image currently in the frame
                    BufferedImage image = ImageWindow.getImage();
                    double currentScale = ImageWindow.getImageScale();

                    //Zoom out 10%
                    currentScale -= 0.15;
                    ImageWindow.setImageScale(currentScale);
                    int scaledWidth = (int)(currentScale*image.getWidth());
                    int scaledHeight = (int)(currentScale*image.getHeight());

                    // Paint scaled version of image to new image
                    BufferedImage scaledImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics2D = scaledImage.createGraphics();
                    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    graphics2D.drawImage(image, 0, 0, scaledWidth, scaledHeight, null);
                    graphics2D.dispose();

                    //Replace the current image with this new scaled one
                    ImageWindow.openImage(scaledImage, null, false);
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
