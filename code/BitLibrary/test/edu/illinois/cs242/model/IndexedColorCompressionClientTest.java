package edu.illinois.cs242.model;

import junit.framework.TestCase;

import java.util.Random;

import static edu.illinois.cs242.model.BitManipulationConstants.*;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 15, 2010
 *
 * <p> <p> Test basic functionality of the IndexedColor compression algorithm
 */
public class IndexedColorCompressionClientTest extends TestCase {

    /**
     * Test that our library builds the color palette as we expect
     */
    public void testBuildPalette() {

        //Setup
        int[][] expectedColorPalette = new int[MAX_BYTE+1][MAX_BYTE+1];
        //Initialize the top-left quadrant of the color palette
        for(int row = 0; row <= HALF_MAX_BYTE; row++){
            for(int col = 0; col <= HALF_MAX_BYTE; col++){
                int red = (HALF_MAX_BYTE - col) << 1;            //Blue is constant here, and blue=0 for this quadrant
                int green = (HALF_MAX_BYTE - row) << 1;
                expectedColorPalette[row][col] = (red << TWO_BYTES) | (green << ONE_BYTE);
            }
        }
        //Initialize the top-right quadrant of the color palette
        for(int row = 0; row <= HALF_MAX_BYTE; row++){
            for(int col = HALF_MAX_BYTE; col <= MAX_BYTE; col++){
                int blue = (col - HALF_MAX_BYTE) << 1;           //Red is constant here, and red=0 for this quadrant
                int green = (HALF_MAX_BYTE - row) << 1;
                expectedColorPalette[row][col] = (green << ONE_BYTE) | blue;
            }
        }
        //Initialize the bottom-right quadrant of the color palette
        for(int row = HALF_MAX_BYTE; row <= MAX_BYTE; row++){
            for(int col = HALF_MAX_BYTE; col <= MAX_BYTE; col++){
                int blue = (col) << 1;                //Green is constant here, and green=0 for this quadrant
                int red = (row - HALF_MAX_BYTE) << 1;
                expectedColorPalette[row][col] = (red << TWO_BYTES) | blue;
            }
        }
        //Initialize the bottom-left quadrant of the color palette
        for(int row = HALF_MAX_BYTE; row <= MAX_BYTE; row++){
            for(int col = 0; col <= HALF_MAX_BYTE; col++){

                int white = (MAX_BYTE << THREE_BYTES) | (MAX_BYTE << TWO_BYTES) | (MAX_BYTE);
                expectedColorPalette[row][col] = white;     //Everything here should be white
            }
        }

        //Test
        IndexedColorCompressionClient client = new IndexedColorCompressionClient();
        int[][] actualColorPalette = client.buildPalette();

        //Verify
        assertNotNull(actualColorPalette);
        assertEquals(MAX_BYTE+1, actualColorPalette.length);
        assertEquals(MAX_BYTE+1, actualColorPalette[0].length);
        for(int row = 0; row <= MAX_BYTE; row++){
            for(int col = 0; col <= MAX_BYTE; col++){
                assertEquals(expectedColorPalette[row][col], actualColorPalette[row][col]);
            }
        }
    }

    /**
     * Test that our library looks up a color from an index as we expect
     */
    public void testLookupColorFromIndex() {

        //Setup
        IndexedColorCompressionClient client = new IndexedColorCompressionClient();
        int[][] colorPalette = client.buildPalette();
        int colorPaletteIndex = ((int)(Math.random()* MAX_BYTE) << THREE_BYTES) | ((int)(Math.random()* MAX_BYTE) << TWO_BYTES);
        int row = colorPaletteIndex >>> THREE_BYTES;
        int col = ((colorPaletteIndex << ONE_BYTE) >>> THREE_BYTES);

        //Test
        client.setColorPalette(colorPalette);
        int indexedColor = client.lookupColorFromIndex(colorPaletteIndex);

        //Verify
        assertEquals(colorPalette[row][col], indexedColor);
    }
    
    /**
     * Test that our library gets an index correctly from a color as we expect
     */
    public void testLookupIndexFromColor() {

        //Setup
        IndexedColorCompressionClient client = new IndexedColorCompressionClient();
        int rgbColorValue = randomInt();
        int red = (RGBUtilities.getRed(rgbColorValue) >>> ONE_BIT);
        int blue = (RGBUtilities.getBlue(rgbColorValue) >>> ONE_BIT);
        int green = (RGBUtilities.getGreen(rgbColorValue) >>> ONE_BIT);
        while(!(isValid(red) && isValid(green) && isValid(blue))){
            rgbColorValue = randomInt();
        }
        int row, col;
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
        int expectedIndex = ((row << THREE_BYTES) | (col << TWO_BYTES));

        //Test
        int actualIndex = 0;
        try {
            actualIndex = client.lookupIndexFromColor(rgbColorValue);
        } catch (Exception e) {
            fail("Should not have thrown an exception!");
        }

        //Verify
        assertEquals(expectedIndex, actualIndex);
    }

    /**
     * Test that our library decompresses data as we expect
     */
    public void testDecompressData() {

        //Setup
        IndexedColorCompressionClient client = new IndexedColorCompressionClient();
        int[][] colorPalette = client.buildPalette();
        client.setColorPalette(colorPalette);
        int[] compressedData = new int[MAX_BYTE*MAX_BYTE+2];
        int width = MAX_BYTE;
        int height = MAX_BYTE;
        compressedData[0] = height;
        compressedData[1] = width;
        int[][] expectedDecompressedData = null;

        //Build expected result
        boolean validData = compressedData != null && compressedData.length > 0;
        boolean validPalette = colorPalette != null && colorPalette.length == MAX_BYTE+1 && colorPalette[0].length == MAX_BYTE+1;
        if(validData && validPalette){
            int rows = compressedData[0];
            int cols = compressedData[1];
            expectedDecompressedData = new int[rows][cols];
            for(int row = 0; row < rows; row++){
                for(int col = 0; col < cols; col++){
                    int compressedDataIndex = (2 + (((row+1)*(col+1)-1))/2);
                    int colorPaletteIndex = compressedData[compressedDataIndex];
                    if(((row+1)*(col+1)-1) % 2 == 0){
                        colorPaletteIndex = (colorPaletteIndex >>> TWO_BYTES) << TWO_BYTES;
                    } else {
                        colorPaletteIndex = colorPaletteIndex << TWO_BYTES;
                    }
                    expectedDecompressedData[row][col] = client.lookupColorFromIndex(colorPaletteIndex);
                }
            }
        }

        //Test
        int[][] actualDecompressedData = null;
        try {
            actualDecompressedData = client.buildDecompressedData(compressedData);
        } catch (Exception e) {
            fail("Should not have thrown an exception!");
        }

        //Verify
        assertNotNull(expectedDecompressedData);
        assertNotNull(actualDecompressedData);
        assertEquals(actualDecompressedData.length, expectedDecompressedData.length);
        assertEquals(actualDecompressedData[0].length, expectedDecompressedData[0].length);
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                assertEquals(expectedDecompressedData[row][col], actualDecompressedData[row][col]);
            }
        }
    }

    /**
     * Test that our library compresses data as we expect
     */
    public void testCompressData() {

        //Setup
        IndexedColorCompressionClient client = new IndexedColorCompressionClient();
        int[][] colorPalette = client.buildPalette();
        client.setColorPalette(colorPalette);
        int width = MAX_BYTE;
        int height = MAX_BYTE;
        int[][] decompressedData = new int[height][width];
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                decompressedData[row][col] = RGBUtilities.getRGB((int)(Math.random()*MAX_BYTE), (int)(Math.random()*MAX_BYTE), (int)(Math.random()*MAX_BYTE));
            }
        }

        //Build expected compressed data
        int[] expectedCompressedData = new int[(width*height)/2 + 1 + 2];
        expectedCompressedData[0] = height;
        expectedCompressedData[1] = width;
         for(int row = 0; row < decompressedData.length; row++){
             for (int col = 0; col < decompressedData[0].length; col++) {
                 int compressedDataIndex = (2 + (((row+1)*(col+1)-1))/2);
                 int colorPaletteIndex = 0;
                 try {
                     colorPaletteIndex = client.lookupIndexFromColor(decompressedData[row][col]);
                 } catch (Exception e) {
                     fail("Should not have thrown an exception!");
                 }

                 //If this pixel belongs in the left half an int entry in the compressed image, put it in the left half
                 if(((row+1)*(col+1)-1) % 2 == 0){
                     expectedCompressedData[compressedDataIndex] = colorPaletteIndex;
                 } else {
                     expectedCompressedData[compressedDataIndex] = expectedCompressedData[compressedDataIndex] | (colorPaletteIndex >>> TWO_BYTES);
                 }
             }
         }

        //Test
        int[] actualCompressedData = null;
        try {
            actualCompressedData = client.buildCompressedData(decompressedData);
        } catch (Exception e) {
            fail("Should not have thrown an exception!");
        }

        //Verify
        assertNotNull(actualCompressedData);
        assertEquals(expectedCompressedData.length, actualCompressedData.length);
        for(int entry = 0; entry<expectedCompressedData.length; entry++){
            assertEquals(expectedCompressedData[entry], actualCompressedData[entry]);
        }
    }


    /**
     * Simple method that checks for valid R,G, and B values
     *
     * @param value R, G, or B value to test
     * @return Whether or not the data is valid
     */
    private boolean isValid(int value) {
        return (value <= HALF_MAX_BYTE && value >= 0);
    }

    //Utility method for generating and returning a random short
    private short randomShort() {
        Random random = new Random();
        return (short) Math.abs(random.nextInt());
    }

    //Utility method for generating and returning a random short
    private int randomInt() {
        Random random = new Random();
        return random.nextInt();
    }
}
