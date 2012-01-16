package edu.illinois.cs242.effects;

import edu.illinois.cs242.effects.image.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Created by Jon Tedesco
 * Date: Sep 25, 2010
 *
 * Holds blur and sharpen image effects
 */

public class BlurAndSharpenEffects {
    
    /**
     * Applies the simple blur effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage simpleBlur(BufferedImage image) {

        //Setup the blur operation
        float ninth = 1.0f / 9.0f;
        float[] blurKernel = {
            ninth, ninth, ninth,
            ninth, ninth, ninth,
            ninth, ninth, ninth
        };
        BufferedImageOp blurOperation = new ConvolveOp(new Kernel(3, 3, blurKernel));

        //Perform the blur into an image with transparency
        BufferedImage blurredImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        blurredImageWithTransparency = blurOperation.filter(image, blurredImageWithTransparency);

        //Remove the transparency
        BufferedImage blurredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = blurredImage.getGraphics();
        graphics.drawImage(blurredImageWithTransparency, 0, 0, null);
        return blurredImage;
    }

    /**
     * Sharpens a given input image, and returns the sharpened image as an RGB image
     *
     * @param image The image to be sharpened
     * @return The sharpened image
     */
    public static BufferedImage sharpen(BufferedImage image) {

        BufferedImageOp sharpenOperation = new SharpenFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = sharpenOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the box blur effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage boxBlur(BufferedImage image) {
        BufferedImageOp boxBlurOperation = new BoxBlurFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = boxBlurOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the gaussian blur effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage gaussianBlur(BufferedImage image) {
        BufferedImageOp gaussianBlurOperation = new GaussianFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = gaussianBlurOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the high pass filter effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage highPass(BufferedImage image) {
        BufferedImageOp highPassOperation = new HighPassFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = highPassOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the lens blur effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage lensBlur(BufferedImage image) {
        BufferedImageOp lensBlurOperation = new LensBlurFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = lensBlurOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the erosion effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage erode(BufferedImage image) {
        BufferedImageOp erodeOperation = new MinimumFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = erodeOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the dilation effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage dilate(BufferedImage image) {
        BufferedImageOp dilateOperation = new MaximumFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = dilateOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the motion blur effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage motionBlur(BufferedImage image) {
        BufferedImageOp motionBlurOperation = new MotionBlurFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = motionBlurOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the oil effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage oil(BufferedImage image) {
        BufferedImageOp oilOperation = new OilFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = oilOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the sparkle effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage sparkle(BufferedImage image) {
        BufferedImageOp sparkleOperation = new SparkleFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = sparkleOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }


    /**
     * Applies the smear effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage smear(BufferedImage image) {
        BufferedImageOp smearOperation = new SmearFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = smearOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies the unsharpen effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage unsharpen(BufferedImage image) {
        BufferedImageOp unsharpenOperation = new UnsharpFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = unsharpenOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }


    /**
     * Applies the rays effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage rays(BufferedImage image) {
        BufferedImageOp raysOperation = new RaysFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = raysOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }
}
