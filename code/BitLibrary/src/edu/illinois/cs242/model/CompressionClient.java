package edu.illinois.cs242.model;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 8, 2010
 *
 * <p> <p> Abstract implementation of an image compression client, where concrete implementations would implement specific image
 * compression algorithms.
 *
 * @author Jon Tedesco
 */

public interface CompressionClient {

    /**
      * Method that will encompass the bulk of the compression of an uncompressed image. Takes in an uncompressed image
      * and returns an image  in compressed image format. However, sees images as array of integers at this level still.
      *
      * @param inputImage A 2-D array of integers representing the pixels of the original image
      * @return A 1-D array of the image compressed using the RLE compression algorithm, in the previously described manner
      * @see edu.illinois.cs242.model.RLECompressionClient
      */
    public int[] buildCompressedData(int[][] inputImage) throws Exception;

    /**
     * Method that will encompass the bulk of the decompression of a compressed image. Takes in a compressed image
     * and returns a decompressed image. However, sees images as array of integers at this level still.
     *
     * @param inputData A 1-D array of the image compressed using the RLE compression algorithm, in the previously described manner
     * @return A 2-D array of integers representing the pixels of the original image
     * @see edu.illinois.cs242.model.RLECompressionClient
     */
    public int[][] buildDecompressedData(int[] inputData) throws Exception;

    /**
     * Simple method to get the proper file suffix for the compressed form of the particular compression algorithm
     *
     * @return A String representing the file suffix for the particular CompressionClient implementation
     */
    public String getFileSuffix();
}
