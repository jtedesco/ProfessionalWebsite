package edu.illinois.cs242.model;

import junit.framework.TestCase;

import java.util.Random;

import static edu.illinois.cs242.model.BitManipulationConstants.*;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 15, 2010
 *
 * <p> <p> Test basic functionality of the RLE compression algorithm
 */
public class RLECompressionClientTest extends TestCase {

    /**
     * Tests that our library correctly compresses a simple image
     */
    public void testCompressSimpleImage() {

        //Setup
        //Build the image to be compressed with one color
        int numberOfRows = Math.abs(randomShort());
        byte numberOfColumns = (byte) Math.abs(randomByte());
        byte red =  (byte) Math.abs(randomByte());
        byte green =  (byte) Math.abs(randomByte());
        byte blue = (byte) Math.abs(randomByte());
        int[][] simpleImage = new int[numberOfRows][numberOfColumns];
        for(int row = 0; row < simpleImage.length; row++){
            for(int col = 0; col < simpleImage[0].length; col++){
                int rgb = (blue) | (green << ONE_BYTE) | (red << TWO_BYTES);
                simpleImage[row][col] = rgb;
            }
        }

        //Build the expected output
        int[] expectedCompressedSimpleImage = new int[numberOfRows+2];
        expectedCompressedSimpleImage[0] = numberOfRows;
        expectedCompressedSimpleImage[1] = numberOfColumns;
        for(int entry = 2; entry<numberOfRows+2; entry++)
            expectedCompressedSimpleImage[entry] = ((blue) | (green << ONE_BYTE) | (red << TWO_BYTES | (numberOfColumns << THREE_BYTES)));

        //Test
        //Get the actual compressed image
        RLECompressionClient client = new RLECompressionClient();
        int[] actualCompressedSimpleImage = null;
        try {
            actualCompressedSimpleImage = client.buildCompressedData(simpleImage);
        } catch (Exception e) {
            fail("Should not have thrown an exception!");
        }

        //Verify
        assertNotNull(actualCompressedSimpleImage);
        assertEquals(numberOfRows+2, actualCompressedSimpleImage.length);
        for(int entry = 0; entry < simpleImage.length; entry++){
            assertEquals(expectedCompressedSimpleImage[entry], actualCompressedSimpleImage[entry]);
        }
    }

    /**
     * Tests that our library correctly decompresses a simple image
     */
    public void testDeompressSimpleImage() {

        //Setup
        //Build the image to be compressed (a block with r=40, b=40, c=40)
        int numberOfRows = Math.abs(randomShort());
        byte numberOfColumns = (byte) Math.abs(randomByte());
        byte red =  (byte) Math.abs(randomByte());
        byte green =  (byte) Math.abs(randomByte());
        byte blue = (byte) Math.abs(randomByte());
        int[][] simpleImage = new int[numberOfRows][numberOfColumns];
        for(int row = 0; row < simpleImage.length; row++){
            for(int col = 0; col < simpleImage[0].length; col++){
                int rgb = (blue) | (green << ONE_BYTE) | (red << TWO_BYTES);
                simpleImage[row][col] = rgb;
            }
        }

        //Build the expected output
        int[] compressedSimpleImage = new int[numberOfRows+2];
        compressedSimpleImage[0] = numberOfRows;
        compressedSimpleImage[1] = numberOfColumns;
        for(int entry = 2; entry<numberOfRows+2; entry++)
            compressedSimpleImage[entry] = ((blue) | (green << ONE_BYTE) | (red << TWO_BYTES | (numberOfColumns << THREE_BYTES)));

        //Test
        //Get the actual decompressed image
        RLECompressionClient client = new RLECompressionClient();
        int[][] actualDecompressedSimpleImage = null;
        try {
            actualDecompressedSimpleImage = client.buildDecompressedData(compressedSimpleImage);
        } catch (Exception e) {
            fail("Should not have thrown an exception!");
        }

        //Verify
        assertNotNull(actualDecompressedSimpleImage);
        assertEquals(simpleImage.length, actualDecompressedSimpleImage.length);
        assertEquals(simpleImage[0].length, actualDecompressedSimpleImage[0].length);
        for(int row = 0; row < simpleImage.length; row++){
            for(int col = 0; col < simpleImage[0].length; col++){
                assertEquals(simpleImage[row][col], actualDecompressedSimpleImage[row][col]);
            }
        }
    }

    //Utility method for generating and returning a random byte
    private byte randomByte() {
        Random random = new Random();
        byte[] aByte = new byte[1];
        random.nextBytes(aByte);
        return aByte[0];
    }

    //Utility method for generating and returning a random short
    private short randomShort() {
        Random random = new Random();
        return (short) random.nextInt();
    }
}
