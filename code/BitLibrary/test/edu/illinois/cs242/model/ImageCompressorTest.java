package edu.illinois.cs242.model;

import junit.framework.TestCase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.mockito.Mockito.*;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 11, 2010
 * 
 * <p> <p> Tests that:
 *          1) <code>ImageCompressor</code> calls the correct <code>CompressionClient</code> methods in order for each action
 *          2) <code>ImageCompressor</code> correctly translates between the input and output data types to its methods
 */
public class ImageCompressorTest extends TestCase {

    /**
     * Randomly generates a compressed image and returns a <code>File</code> object pointing to that data
     *
     * @return A file object representing a compressed image
     */
    private File getRandomCompressedImageFile() throws IOException {

        //Build a randomized int array
        short randomLength = (short) Math.abs(randomShort());
        int[] compressedFile = new int[randomLength];
        for(int chunk = 0; chunk < randomLength; chunk++){
            compressedFile[chunk] = randomInt();
        }

        //Write that to a temporary file and return it
        File tempFile = File.createTempFile("tempTestFile", ".tmp");
        tempFile.deleteOnExit();
        BitLibrary bitLibrary = new BitLibrary(null, tempFile);
        for(int chunk: compressedFile){
            bitLibrary.writeInt(chunk);
        }
        bitLibrary.close();

        return tempFile;
    }

    /**
     * Randomly selects an image from the 'images' directory and returns it as a <code>BufferedImage</code>
     *
     * @return A random image from the 'images' directory
     */
    private BufferedImage getRandomImage() {

        //Open the 'images' directory and get the file list
        File imagesDir = new File("images\\");
        File[] imageOptions = imagesDir.listFiles();

        //Pick an image at random
        int choiceIndex = (Math.abs(randomShort())) % imageOptions.length;
        File choice = imageOptions[choiceIndex];
        while(choice.getName().indexOf(".svn")>=0){
            choiceIndex = (Math.abs(randomShort())) % imageOptions.length;
            choice = imageOptions[choiceIndex];
        }
        //Open it as an image and return it
        BufferedImage image = ImageUtilities.loadImage(choice.getAbsolutePath());
        return image;
    }

    /**
     * Test that our library delegates its work to the compression clients as we expect
     */
    public void testCompressImageCallsCompressionClient(){

        //Setup
        CompressionClient mockCompressionClient = mock(CompressionClient.class);
        when(mockCompressionClient.getFileSuffix()).thenReturn(CompressionConstants.RLE_FILE_SUFFIX);
        try {
            when(mockCompressionClient.buildCompressedData((int[][]) any())).thenReturn(new int[0]);
        } catch (Exception e) {
            fail("Should not have thrown an exception...????!");
        }
        BufferedImage image = getRandomImage();
        int[][] expectedImage = ImageUtilities.getRGBPixels(image);
    }

    /**
     * Test that our library delegates its work to the compression clients as we expect
     */
    public void testDecompressImageCallsCompressionClient() {

        //Setup a temporary file
        CompressionClient mockCompressionClient = mock(CompressionClient.class);
        when(mockCompressionClient.getFileSuffix()).thenReturn(CompressionConstants.INDEXED_COLOR_FILE_SUFFIX);
        try {
            when(mockCompressionClient.buildDecompressedData((int[]) any())).thenReturn(new int[1][1]);
        } catch (Exception e) {
            fail("Should not have and could not have thrown an exception here!");
        }
        File compressedFile = null;
        try {
            compressedFile = spy(getRandomCompressedImageFile());
        } catch (IOException e) {
            fail("Failed to creating a randomized compressed image file for testing!");
        }

        //Make the file seem like it has some free space
        int freeSpace = (int) Math.abs(randomShort()*0.1);
        when(compressedFile.length()).thenReturn((long) freeSpace);

        //Setup expected parameters to CompressionClient
        long sizeOfFile = (compressedFile.length() / 4);
        int[] compressedFileData = new int[(int) sizeOfFile];
        BitLibrary bitLibrary = null;
        try {
            bitLibrary = new BitLibrary(compressedFile, null);
        } catch (IOException e) {
            fail("Should not have thrown an exception while initialize BitLibrary");
        }
        try {
            bitLibrary = new BitLibrary(compressedFile, null);
        } catch (IOException e) {
            fail("Failed to open randomized compressed image file for testing!");
        }
        for(int chunk = 0; chunk<freeSpace/4; chunk++){
            try {
                compressedFileData[chunk] = bitLibrary.readInt();
            } catch (IOException e) {
                fail("Error while reading file data into array for testing, on chunk = " + chunk + "!");
            }
        }
    }

    //Utility method for generating and returning a random int
    private int randomInt() {
        Random random = new Random();
        return random.nextInt();
    }

    //Utility method for generating and returning a random short
    private short randomShort() {
        Random random = new Random();
        return (short) random.nextInt();
    }

}
