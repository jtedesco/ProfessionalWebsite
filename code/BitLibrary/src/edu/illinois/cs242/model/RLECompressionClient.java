package edu.illinois.cs242.model;
import static edu.illinois.cs242.model.BitManipulationConstants.*;

/**
 * <p> Concrete implementation of <code>CompressionClient</code> that uses the RLE, or Run-Length image compression algorithm.
 *
 * <p> For this implementation, we will take in a two-dimensional array of integers, RGB representing pixel values, where
 *         each entry corresponds to a pixel. In the input, each integer is encoded as 0RGB, where each of these four
 *         pieces of data takes up one byte of space. Here, the first byte will be used to denote the length of a given
 *         segment of color.
 * 
 * <p> This class implements the RLE algorithm, so that each integer in the output will represent #RGB, where RGB are the
 *         typical color values, but # represents the number of successive horizontal pixels for which this is the color.
 *         This is horizontal only, because as we flatten the 2-day pixel array into a 1-D representation, traversing the
 *         original image a row at a time.
 *
 * <p> For this particular implementation, the first three entries of the output array will hold special data values:
 *      1) First entry will hold the vertical dimension of the image (number of rows of pixels).
 *      2) Second entry will hold the horizontal dimension of the image (number of columns of pixels).
 *
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 8, 2010
 *
 * @author Jon Tedesco
 * @see edu.illinois.cs242.model.CompressionClient
 */

public class RLECompressionClient implements CompressionClient{

    /**
     * {@inheritDoc}
     *
     * <p> This implementation specifically deals with compression using the RLE image compression algorithm.
     */
     public int[] buildCompressedData(int[][] inputData) throws Exception {

        //First, strip the alpha channel from the image
        inputData = ImageUtilities.stripAlphaChannel(inputData);

        if (inputData != null && inputData.length > 0 && inputData[0] != null && inputData[0].length > 0) {

            //Initialize the compressed data array
            int compressedSize = getCompressedSize(inputData);
            int[] compressedData = new int[compressedSize];
            compressedData[0] = inputData.length;
            compressedData[1] = inputData[0].length;
            int compressedDataIndex = 2;    //Start at the entry after the dimension data

            //For each row in the image...
            for(int row = 0; row < inputData.length; row++){

                //Count how many entries we need to store this row
                int currentPixel = 0;
                while(currentPixel < inputData[row].length){

                    int identicalPixels = 0;
                    try{
                        //Get the number of contiguous identical pixels
                        identicalPixels = countIdenticalPixels(inputData[row], currentPixel);

                        //Enter this group of pixels into our compressed image
                        int rgbData = (inputData[row][currentPixel] << ONE_BYTE) >>> ONE_BYTE;
                        compressedData[((int) compressedDataIndex)] = (identicalPixels << THREE_BYTES) | (rgbData);
                    } catch (Exception e) {
                        throw new Exception("Error writing data to compressed image array!\n" + e.getMessage(), e);
                    }

                    //Update our tallies
                    currentPixel += identicalPixels;
                    compressedDataIndex++;
                }
            }

            return compressedData;

        } else {
            throw new Exception("Invalid input image to buildCompressedData!");
        }

    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation specifically deals with compression using the RLE image compression algorithm.
     */
    public int[][] buildDecompressedData(int[] inputData) throws Exception {

        //Error-check first
        if (inputData != null && inputData.length>2) {

            //Initialize the decompressed image
            int[][] decompressedData = new int[inputData[0]][inputData[1]];
            int inputDataIndex = 2; //Start with the first data entry
            for(int row = 0; row < decompressedData.length; row++){

                int col = 0;
                while(col < decompressedData[0].length){

                    int numPixelsInARow = (inputData[inputDataIndex] >>> THREE_BYTES);
                    int color = ((inputData[inputDataIndex]) << ONE_BYTE) >>> ONE_BYTE;
                    for(int pixel = 0; pixel < numPixelsInARow; pixel++){
                        decompressedData[row][col+pixel] = color;
                    }

                    //Iterate to the next point in each array
                    inputDataIndex++;
                    col += numPixelsInARow;
                }
            }

            return decompressedData;
        } else {
            throw new Exception("Invalid input image to buildDecompressedData!");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation specifically deals with compression using the RLE image compression algorithm.
     */
    public String getFileSuffix() {
        return CompressionConstants.RLE_FILE_SUFFIX;
    }

    /**
     * Find the number of pixels in a row that are of the same color, up to 255 pixels in a row (the maximum that can
     *      be stored in one byte)
     *
     * @param pixelArray The array in which to look for identical contiguous pixels
     * @param startIndex The index from which to begin looking for identical pixels in the given array
     *
     * @return The number of pixels in a row with the same color, including the first pixel
     */
    public int countIdenticalPixels(int[] pixelArray, int startIndex){

        //Initialize counter variables
        int pixelCount = 0, index;
        int color = pixelArray[startIndex];

        //Loop the array until we either hit 255 or find one that's not identical
        for(index = startIndex; index<pixelArray.length && pixelCount < MAX_BYTE && pixelArray[index]==color; index++){
            pixelCount++;
        }

        return pixelCount;
    }

    /**
     * Private helper method to calculate the compressed size of this uncompressed image
     *
     * @param inputImage The uncompressed image
     * @return An integer representing the compressed size of the image
     */
    private int getCompressedSize(int[][] inputImage) {

        //Initialize the size counter
        int size = 2; //One entry for height and one for width

        //For each row in the image...
        for(int row = 0; row < inputImage.length; row++){

            //Count how many entries we need to store this row
            int currentPixel = 0;
            while(currentPixel < inputImage[row].length){

                 //Get the number of contiguous identical pixels
                currentPixel += countIdenticalPixels(inputImage[row], currentPixel);
                size++;
            }
        }

        return size;
    }
}
