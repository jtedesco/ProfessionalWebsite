package edu.illinois.cs242.model;
import static edu.illinois.cs242.model.BitManipulationConstants.*;

/**
 * <p> Concrete implementation of <code>CompressionClient</code> that uses the Indexed Color image compression algorithm.
 *
 * <p> For this implementation, we will take in a two-dimensional array of integers, RGB representing pixel values, where
 *         each entry corresponds to a pixel. In the input, each integer is encoded as 0RGB, where each of these four
 *         pieces of data takes up one byte of space.
 *
 * <p> This implementation uses a color palette, which is a two-dimensional array used for looking up color values to
 *      reduce file size.
 *
 * <p> In the compressed format, each integer value will hold two pixels. The first two bytes of the integer will hold the
 *      first and second index values to our color palette array. Likewise, the last two bytes will hold the first and second
 *      index values to the color of another pix in our color palette. So, an integer in compressed for will look like:
 *      ROW | COL | ROW | COL, where ROW and COL index the color palette.
 *
 * <p> For this particular implementation, the first three entries of the output array will hold special data values:
 *      1) First entry will hold the vertical dimension of the image (number of rows of pixels).
 *      2) Second entry will hold the horizontal dimension of the image (number of columns of pixels).
 *
 * <p> Each entry
 *                                                               green
 * <p> So, the color palette looks like this:                 (0, 127, 0)
 *                                                  _________________________________
 *    ________________________          yellow     |               |                 |        cyan
 *   |                        |     (127, 127, 0)  |               |                 |   (0, 127, 127)
 *   |  Where each tuple is:  |                    |               |                 |
 *   |   (Red, Green, Blue)   |                    |               |                 |
 *   |________________________|          red       |               |                 |
 *                                   (127, 0, 0)   |_______________|_________________|        blue
 *                                                         black   |                 |     (0, 0, 127)
 *        ________________________                       (0, 0, 0) |                 |
 *       |                        |                                |                 |
 *       | Notice that the bottom |                                |                 |
 *       |   left portion of the  |                                |                 |        magenta
 *       |   palette doesn't map  |                                |_________________|     (127, 0, 127)
 *       |   to any colors!       |                              red
 *       |________________________|                          (127, 0, 0)
 *                                                                                                             
 *
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 8, 2010
 *
 * @author Jon Tedesco
 * @see edu.illinois.cs242.model.IndexedColorCompressionClient
 */

public class IndexedColorCompressionClient implements CompressionClient{

    /**
     * A 256x256 color palette for use with the indexed color compression algorithm
     */
    private int[][] colorPalette = buildPalette();

    /**
     * {@inheritDoc}
     *
     * <p> This implementation specifically deals with compression using the indexed color image compression algorithm.
     */
    public int[] buildCompressedData(int[][] decompressedData) throws Exception {

        //Error check first
        boolean validData = decompressedData != null && decompressedData.length > 0 && decompressedData[0].length > 0;
        boolean validPalette = colorPalette != null && colorPalette.length == MAX_BYTE+1 && colorPalette[0].length == MAX_BYTE+1;
        if(validData && validPalette){

            //Calculate the size of the compressed image
            int compressedSize = (decompressedData.length * decompressedData[0].length)/2 + 2;
            if((decompressedData.length * decompressedData[0].length) % 2 != 0){
                compressedSize++;
            }
            int[] compressedData = new int[compressedSize]; //Allocate room for all the data, plus one entry for to store the dimensions of the image

            //Write the dimensions of the image into the compressed form
            compressedData[0] = decompressedData.length;
            compressedData[1] = decompressedData[0].length;

            //Compress pixels
            for(int row = 0; row < decompressedData.length; row++){
                for (int col = 0; col < decompressedData[0].length; col++) {

                    //Prepare data for compression
                    int compressedDataIndex = (2 + (((row+1)*(col+1)-1))/2);
                    int colorPaletteIndex = lookupIndexFromColor(decompressedData[row][col]);

                    //If this pixel belongs in the left half an int entry in the compressed image, put it in the left half
                    if(((row+1)*(col+1)-1) % 2 == 0){
                        compressedData[compressedDataIndex] = colorPaletteIndex;
                    } else {
                        compressedData[compressedDataIndex] = compressedData[compressedDataIndex] | (colorPaletteIndex >>> TWO_BYTES);
                    }
                }
            }

            return compressedData;

        } else {
            throw new Exception("Invalid input image to buildCompressedData()!");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation specifically deals with compression using the indexed color image compression algorithm.
     */
    public int[][] buildDecompressedData(int[] compressedData) throws Exception {

        //Error check first
        boolean validData = compressedData != null && compressedData.length > 0;
        boolean validPalette = colorPalette != null && colorPalette.length == MAX_BYTE+1 && colorPalette[0].length == MAX_BYTE+1;
        if(validData && validPalette){

            //Initialize the return array
            int rows = compressedData[0];
            int cols = compressedData[1];
            int[][] decompressedData = new int[rows][cols];

            for(int row = 0; row < rows; row++){
                for(int col = 0; col < cols; col++){

                    //Prepare data for decompression
                    int compressedDataIndex = (2 + (((row+1)*(col+1)-1))/2);
                    int colorPaletteIndex = compressedData[compressedDataIndex];

                    //If this pixel was in the left half of the entry or the right half of the entry, pull it out accordingly
                    if(((row+1)*(col+1)-1) % 2 == 0){
                        colorPaletteIndex = (colorPaletteIndex >>> TWO_BYTES) << TWO_BYTES;
                    } else {
                        colorPaletteIndex = colorPaletteIndex << TWO_BYTES;
                    }

                    //Write the color back to the decompressedData
                    decompressedData[row][col] = lookupColorFromIndex(colorPaletteIndex);
                }
            }
            return decompressedData;

        } else {
            throw new Exception("Invalid input image to buildDecompressedData()!");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation specifically deals with compression using the indexed color image compression algorithm.
     */
    public String getFileSuffix() {
        return CompressionConstants.INDEXED_COLOR_FILE_SUFFIX;
    }

    /**
     * Private helper method to initialize the color palette
     *
     * @return A color palette represented as a 2D array of integers, where each integer is the RGB, 24-bit representation
     *          of a color, without the LSB for each R,G, and B component
     */
    public int[][] buildPalette() {

        //Create our data structures
        int[][] colorPalette = new int[MAX_BYTE+1][MAX_BYTE+1];

        //Initialize the top-left quadrant of the color palette
        for(int row = 0; row <= HALF_MAX_BYTE; row++){
            for(int col = 0; col <= HALF_MAX_BYTE; col++){
                int red = (HALF_MAX_BYTE - col) << 1;            //Blue is constant here, and blue=0 for this quadrant
                int green = (HALF_MAX_BYTE - row) << 1;
                colorPalette[row][col] = (red << TWO_BYTES) | (green << ONE_BYTE);
            }
        }

        //Initialize the top-right quadrant of the color palette
        for(int row = 0; row <= HALF_MAX_BYTE; row++){
            for(int col = HALF_MAX_BYTE; col <= MAX_BYTE; col++){
                int blue = (col - HALF_MAX_BYTE) << 1;           //Red is constant here, and red=0 for this quadrant
                int green = (HALF_MAX_BYTE - row) << 1;
                colorPalette[row][col] = (green << ONE_BYTE) | blue;
            }
        }

        //Initialize the bottom-right quadrant of the color palette
        for(int row = HALF_MAX_BYTE; row <= MAX_BYTE; row++){
            for(int col = HALF_MAX_BYTE; col <= MAX_BYTE; col++){
                int blue = (col) << 1;                //Green is constant here, and green=0 for this quadrant
                int red = (row - HALF_MAX_BYTE) << 1;
                colorPalette[row][col] = (red << TWO_BYTES) | blue;
            }
        }

        //Initialize the bottom-left quadrant of the color palette
        for(int row = HALF_MAX_BYTE; row <= MAX_BYTE; row++){
            for(int col = 0; col <= HALF_MAX_BYTE; col++){

                int white = (MAX_BYTE << THREE_BYTES) | (MAX_BYTE << TWO_BYTES) | (MAX_BYTE);
                colorPalette[row][col] = white;     //Everything here should be white
            }
        }

        //Return the palette
        return colorPalette;
    }

    /**
     * A private helper method to lookup a color value in the palette and translate it to its index. Since the palette is
     * only 256 x 256, we cannot store all 24 bits of color data. Instead, we approximate the color using 21 bits, 7 bits
     * for each of R, G, and B, and drop off the LSB of each value to approximate it with the palette.
     *
     * @param rgbColorValue The RGB representation of a color, represented as 0RGB in an integer value, where 0RGB is
     *                          four bytes long (0, R, G, and B each take up one byte)
     * @return The index of the color in the color palette, represented as ROW|COL|0|0, where '|' denotes the boundaries
     *          between bytes of data in the integer value
     */
    public int lookupIndexFromColor(int rgbColorValue) throws Exception {

        //Extract the red, green, and blue components from the color and make it on the interval [0, 127]
        int red = (RGBUtilities.getRed(rgbColorValue) >>> ONE_BIT);
        int blue = (RGBUtilities.getBlue(rgbColorValue) >>> ONE_BIT);
        int green = (RGBUtilities.getGreen(rgbColorValue) >>> ONE_BIT);

        //Error - check
        if(isValid(red) && isValid(green) && isValid(blue)){
            int row, col;

            //Check each quadrant for this color
            if(blue == 0){ //Top left quadrant

                row = HALF_MAX_BYTE - green;
                col = HALF_MAX_BYTE - red;

            } else if(red == 0) { //Top right quadrant

                row = HALF_MAX_BYTE - green;
                col = (blue << 1);

            } else if(green == 0) { //Bottom right quadrant

                row = (red << 1);
                col = (blue << 1);

            } else { //Bottom left quadrant -- white

                row = MAX_BYTE;
                col = 0;
            }

            //Encode the row & col into an int and return it
            return ((row << THREE_BYTES) | (col << TWO_BYTES));

        } else {
            throw new Exception("Invalid RGB values:\nred: " + red + ", blue: " + blue + ", green: " + green);
        }
    }

    /**
     * A private helper method to lookup a color value in the palette and translate it to its index representation
     *
     * @param paletteIndex The index of the color in the color palette, represented as ROW|COL|0|0, where '|'
     *          denotes the boundaries between bytes of data in the integer value
     * @return The RGB representation of a color, represented as 0RGB in an integer value, where 0RGB is
     *                          four bytes long (0, R, G, and B each take up one byte)
     */
    public int lookupColorFromIndex(int paletteIndex) {

        //Extract the row and column from the parameter
        int row = paletteIndex >>> THREE_BYTES;
        int col = ((paletteIndex << ONE_BYTE) >>> THREE_BYTES);

        //Get the RGB from the palette and return it
        return colorPalette[row][col];
    }

    /**
     * Simple helper method that will be used to check to see if the given value is a valid color value
     *
     * @param value The value to be checked for validity
     * @return Whether the given value is on the interval [0, 127]
     */
    private boolean isValid(int value) {
        return (value <= HALF_MAX_BYTE && value >= 0);
    }

    /**
     * Simple setter for the color palette object of the class
     *
     * @param colorPalette The new color palette
     */
    public void setColorPalette(int[][] colorPalette) {
        this.colorPalette = colorPalette;
    }
}
