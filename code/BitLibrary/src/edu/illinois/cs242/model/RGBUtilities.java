package edu.illinois.cs242.model;

import static edu.illinois.cs242.model.BitManipulationConstants.ONE_BYTE;
import static edu.illinois.cs242.model.BitManipulationConstants.TWO_BYTES;

/**
 * Manipulates RGB values, translating between RGB values separately and RGB values compressed into one integer color value.
 *               _______________________________________________
 *              |                                               |
 *              |  NOTE: This is not my code, but was taken     |
 *              |   from CS125, and modified by me heavily      |
 *              |_______________________________________________|
 *
 * @author Jon Tedesco
 */

public class RGBUtilities {

    /**
     * Takes as input the encoded color data as an integer and returns the second byte of data, which refers to the RED value.
     *
     * @param colorData The encoded integer, four bytes, each of which represents a different piece of data.
     * @return The RED value of the input color
     */
	public static int getRed(int colorData) {
		return (colorData >>> TWO_BYTES) & 0xFF;
	}

    /**
     * Takes as input the encoded color data as an integer and returns the third byte of data, which refers to the GREEN value.
     *
     * @param colorData The encoded integer, four bytes, each of which represents a different piece of data.
     * @return The GREEN value of the input color
     */
	public static int getGreen(int colorData) {
		return (colorData >>> ONE_BYTE) & 0xFF;
	}

    /**
     * Takes as input the encoded color data as an integer and returns the fourth byte of data, which refers to the BLUE value.
     *
     * @param colorData The encoded integer, four bytes, each of which represents a different piece of data
     * @return The BLUE value of the input color
     */
	public static int getBlue(int colorData) {
		return colorData & 0xFF;
	}

    /**
     * Encodes red, green, and blue values into one integer to represent a pixel.
     *
     * @param red   The red value of the color
     * @param green The green value of the color
     * @param blue  The blue value of the color
     * @return      An encoded integer containing the RGB data of one pixel
     */
	public static int getRGB(int red, int green, int blue) {
		return (red << TWO_BYTES) | (green << ONE_BYTE) | blue;
	}
}
