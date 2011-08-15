package edu.illinois.cs242.effects;

import edu.illinois.cs242.effects.image.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * Created by Jon Tedesco
 * Date: Sep 25, 2010
 *
 * Holds filters for distortion effects
 */

public class DistortionEffects {

    /**
     * Applies a marblingeffect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage marble(BufferedImage image) {
        BufferedImageOp marblingOperation = new MarbleFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = marblingOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a kaleidoscope effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage kaleidoscope(BufferedImage image) {
        BufferedImageOp kaleidoscopeOperation = new GlowFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = kaleidoscopeOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a ripple effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage ripple(BufferedImage image) {
        BufferedImageOp rippleOperation = new RippleFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = rippleOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a lens effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage lens(BufferedImage image) {
        BufferedImageOp lensOperation = new SphereFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = lensOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies an underwater effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage swim(BufferedImage image) {
        BufferedImageOp swimOperation = new SwimFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = swimOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a twirl effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage twirl(BufferedImage image) {
        BufferedImageOp twirlOperation = new TwirlFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = twirlOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a warp effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage warp(BufferedImage image) {
        BufferedImageOp warpOperation = new WarpFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = warpOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a diffuse effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage diffuse(BufferedImage image) {
        BufferedImageOp diffuseOperation = new DiffuseFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = diffuseOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a water effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage water(BufferedImage image) {
        BufferedImageOp waterOperation = new WaterFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = waterOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }
}
