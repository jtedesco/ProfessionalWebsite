package edu.illinois.cs242.effects;

import edu.illinois.cs242.effects.image.*;
import edu.illinois.cs242.model.BitManipulationConstants;
import edu.illinois.cs242.model.RGBUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;

/**
 * Created by Jon Tedesco
 * Date: Sep 25, 2010
 *
 * This class stores image effects dealing with color manipulation specifically
 */

public class ColorEffects {

    /**
     * Take an input image and turn it into grayscale
     *
     * @param source The image to modify
     * @return The modified image
     */
	public static int[][] toGrayscale(int[][] source) {
		int[][] result = new int[source.length][source[0].length];
		for(int row = 0; row<source.length; row++){
			for(int col = 0; col<source[0].length; col++){
				int rgb = source[row][col];
				int red = RGBUtilities.getRed(rgb);
				int green = RGBUtilities.getGreen(rgb);
				int blue = RGBUtilities.getBlue(rgb);
				int avg = (int)((red+blue+green)/3.0);
				rgb = RGBUtilities.getRGB(avg, avg, avg);
				result[row][col] = rgb;
			}
		}
		return result;
	}

    /**
     * Remove the redeye from a photo
     *
     * @param source The image to modify
     * @return The modified image
     */
	public static int[][] redeye(int[][] source) {

		int width = source.length, height = source[0].length;
		int[][] result = new int[width][height];
		for (int i = 1; i < width; i++)
			for (int j = 1; j < height; j++) {
				int rgb = source[i][j];
				int red = RGBUtilities.getRed(rgb);
				int green = RGBUtilities.getGreen(rgb);
				int blue = RGBUtilities.getBlue(rgb);
				if(red > 4*Math.max(green,blue) && red>64) red = green = blue = 0;
				result[i][j] = RGBUtilities.getRGB(red, green, blue);
			}
		return result;
	}

    /**
     * Inverts the colors of an input image and returns the inverted image as an RGB image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage invert(BufferedImage image) {
        //Setup the invert colors operation
        short[] invert = new short[BitManipulationConstants.MAX_BYTE+1];
        for (int i = 0; i < BitManipulationConstants.MAX_BYTE+1; i++)
          invert[i] = (short)(BitManipulationConstants.MAX_BYTE - i);
        BufferedImageOp invertOperation = new LookupOp(
            new ShortLookupTable(0, invert), null);

        //Invert the colors
        BufferedImage invertedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        invertedImage = invertOperation.filter(image, invertedImage);
        return invertedImage;
    }

    /**
     * Mixes the RGB channels of the image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage mixChannels(BufferedImage image) {
        BufferedImageOp mixChannelsOperation = new ChannelMixFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = mixChannelsOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }


    /**
     * Grays out the image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage gray(BufferedImage image) {
        BufferedImageOp grayOperation = new GrayFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = grayOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Quantizes out the image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage quantize(BufferedImage image) {
        BufferedImageOp quantizeOperation = new QuantizeFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = quantizeOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Dithers the image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage dither(BufferedImage image) {
        BufferedImageOp ditherOperation = new DitherFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = ditherOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Creates a tri-tone version of the image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage tritone(BufferedImage image) {
        BufferedImageOp tritoneOperation = new TritoneFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = tritoneOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Solarizes the image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage solarize(BufferedImage image) {
        BufferedImageOp solarizeOperation = new SolarizeFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = solarizeOperation.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

    /**
     * Applies adjustment curves to the image
     *
     * @param image The image to modify
     * @return The modified image
     */
    public static BufferedImage adjustmentCurves(BufferedImage image) {
        BufferedImageOp adjustmentCurves = new CurvesFilter();

        //Perform the sharpen operation into an image with transparency
        BufferedImage modifiedImageWithTransparency = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        modifiedImageWithTransparency = adjustmentCurves.filter(image, modifiedImageWithTransparency);

        //Remove the transparency in the image
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = modifiedImage.getGraphics();
        graphics.drawImage(modifiedImageWithTransparency, 0, 0, null);
        return modifiedImage;
    }

}
