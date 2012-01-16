package edu.illinois.cs242.model;

import edu.illinois.cs242.model.inputstream.BZipInputStream;
import edu.illinois.cs242.model.outputstream.BZipOutputStream;
import edu.illinois.cs242.view.ImageWindow;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 9, 2010
 *
 * A class containing two template methods for handling an image, specifically, compressing and decompressing an image
 * or file, independent of the compression algorithm being used. Makes use of the <code>CompressionClient</code> interface
 * to call methods on the particular kind of <code>CompressionClient</code> object.
 *
 * @author Jon Tedesco
 *
 * @see edu.illinois.cs242.model.CompressionClient
 * @see edu.illinois.cs242.model.CompressionClientFactory
 */
public class ImageCompressor {

    /**
     * Template method that uses <code>CompressionClient</code> objects, which outlines the steps of an image being compressed
     * by a <code>CompressionClient</code>.
     *
     * @param inputImage An image object representing the original, decompressed image.
     * @param compressionClient An abstract <code>CompressionClient</code> object that will perform the compression using
     *                          a particular compression algorithm.
     *
     * @param outputImage The File object to write to directly
     * @return A file object, representing a compressed image. This file is not necessarily written to disk at this point.
     *
     * @see java.io.File
     * @see java.awt.image.BufferedImage
     * @see edu.illinois.cs242.model.CompressionClient
     */
    public static void compressImage(BufferedImage inputImage, CompressionClient compressionClient, File outputImage) throws Exception {

        //Error handle first
        if(inputImage != null && compressionClient != null){

            //Convert the BufferedImage into an array of pixels in RGB form
            int[][] image = ImageUtilities.getRGBPixels(inputImage);

            //Delegate the work to the CompressionClient
            int[] compressedImageData;
            try {
                compressedImageData = compressionClient.buildCompressedData(image);
            } catch (Exception e) {
                throw new Exception("Error building compressed data!\n" + e.getMessage(), e);
            }

            //Prepare an output stream (zipped depending on the GUI options) to write the file
            OutputStream outputStream = null;
            String extraCompressionAlgorithm = ImageWindow.getExtraCompressionAlgorithm();
            if(extraCompressionAlgorithm.equals(CompressionConstants.NONE)){
                outputStream = new FileOutputStream(outputImage);
            } else if(extraCompressionAlgorithm.equals(CompressionConstants.ZIP)){
                outputStream = new ZipOutputStream(new FileOutputStream(outputImage));
            } else if(extraCompressionAlgorithm.equals(CompressionConstants.GZIP)) {
                outputStream = new GZIPOutputStream(new FileOutputStream(outputImage));
            } else if(extraCompressionAlgorithm.equals(CompressionConstants.BZIP)) {
                outputStream = new BZipOutputStream(new FileOutputStream(outputImage));
            }

            //Write data to the given file file
            BitLibrary bitLibrary = new BitLibrary(null, outputStream);
            for(int chunk : compressedImageData){
                bitLibrary.writeInt(chunk);
            }

            //Finish up and return
            bitLibrary.close();

        } else {
            throw new Exception("Invalid arguments to compressImage()!");
        }
    }

    /**
     * A template method, similar to <code>compressImage</code> in purpose, which decompresses a compressed image, given as
     * a <code>File</code> object.
     *
     * @param inputImage A File object representing the compressed image object, regardless of the compression algorithm used.
     * @param compressionClient An abstract <code>CompressionClient</code> object that will perform the decompression using
     *                          a particular compression algorithm.

     * @return A <code>BufferedImage</code> object representing the final, decompressed image.
     *
     * @see java.io.File
     * @see java.awt.image.BufferedImage
     * @see edu.illinois.cs242.model.CompressionClient
     */
    public static BufferedImage decompressImage(File inputImage, CompressionClient compressionClient) throws Exception {

        //Error handle first
        if(inputImage != null && compressionClient != null) {

            //Setup to read from file into array of integers
            long byteChunksToBeRead = (inputImage.length() / 4);
            int[] compressedImageData = new int[(int) byteChunksToBeRead];

            //Create an input stream and the bit library to read the data (zipped based on the GUI options)
            InputStream inputStream = null;
            String extraCompressionAlgorithm = ImageWindow.getExtraCompressionAlgorithm();
            if(extraCompressionAlgorithm.equals(CompressionConstants.NONE)){
                inputStream = new FileInputStream(inputImage);
            } else if(extraCompressionAlgorithm.equals(CompressionConstants.ZIP)){
                inputStream = new ZipInputStream(new FileInputStream(inputImage));
            } else if(extraCompressionAlgorithm.equals(CompressionConstants.GZIP)) {
                inputStream = new GZIPInputStream(new FileInputStream(inputImage));
            } else if(extraCompressionAlgorithm.equals(CompressionConstants.BZIP)) {
                inputStream = new BZipInputStream(new FileInputStream(inputImage));
            }

            //Extract data from the compressed image
            BitLibrary bitLibrary = new BitLibrary(inputStream, null);
            for(int chunk = 0; chunk < byteChunksToBeRead; chunk++){
                try {
                    compressedImageData[chunk] = bitLibrary.readInt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Decompress the image
            int[][] decompressedImageData = null;
            try {
                decompressedImageData = compressionClient.buildDecompressedData(compressedImageData);
            } catch (Exception e) {
                throw new Exception("Error building decompressed data!\n" + e.getMessage(), e);
            }

            //Convert the resultant array back into a BufferedImage object
            BufferedImage decompressedImage = new BufferedImage(decompressedImageData.length, decompressedImageData[0].length, BufferedImage.TYPE_INT_RGB);
            ImageUtilities.setRGBPixels(decompressedImage, decompressedImageData);

            //Finish up and return
            bitLibrary.close();
            return decompressedImage;            

        } else {
            throw new Exception("Invalid arguments to decompressImage()!");
        }
    }

}
