package edu.illinois.cs242.effects;

import edu.illinois.cs242.effects.image.*;
import edu.illinois.cs242.model.BitManipulationConstants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
/**
 * Created by Jon Tedesco
 * Date: Sep 25, 2010
 *
 * Holds filters for artistic image effects
 */

public class ArtisticEffects {

    /**
     * Applies a chrome effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage chrome(BufferedImage image) {
        BufferedImageOp chromeOperation = new ChromeFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = chromeOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a halftone effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage halftone(BufferedImage image) {
        BufferedImageOp halftoneOperation = new ColorHalftoneFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = halftoneOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a crystallize effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage crystallize(BufferedImage image) {
        BufferedImageOp crystallize = new CrystallizeFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = crystallize.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a emboss effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage emboss(BufferedImage image) {
        BufferedImageOp embossOperation = new EmbossFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = embossOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a feedback effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage feedback(BufferedImage image) {
        BufferedImageOp feedbackOperation = new FeedbackFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = feedbackOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a lights effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage lights(BufferedImage image) {
        BufferedImageOp lightsOperation = new LightFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = lightsOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a noise effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage noise(BufferedImage image) {
        BufferedImageOp noiseOperation = new NoiseFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = noiseOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a pointillize effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage pointillize(BufferedImage image) {
        BufferedImageOp pointillizeOperation = new PointillizeFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = pointillizeOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a rubber stamp effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage rubberStamp(BufferedImage image) {
        BufferedImageOp rubberStampOperation = new StampFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = rubberStampOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a weavestamp effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage weave(BufferedImage image) {
        BufferedImageOp weaveOperation = new WeaveFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = weaveOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies a edge detection effect to a given image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage detectEdges(BufferedImage image) {
        BufferedImageOp detectEdgesOperation = new EdgeFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = detectEdgesOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Posterizes an input image and returns the posterized image as an RGB image
     *
     * @param image The image to be posterized
     * @return The posterized image
     */
    public static BufferedImage posterize(BufferedImage image) {

        //Setup the posterize operation
        short[] posterize = new short[BitManipulationConstants.MAX_BYTE+1];
        for (int i = 0; i < BitManipulationConstants.MAX_BYTE+1; i++)
          posterize[i] = (short)(i - (i % BitManipulationConstants.FOUR_BYTES));
        BufferedImageOp posterizeOperation =
            new LookupOp(new ShortLookupTable(0, posterize), null);

        //Posterize the image
        BufferedImage posterizedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        posterizedImage = posterizeOperation.filter(image, posterizedImage);
        return posterizedImage;
    }
}
