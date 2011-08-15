package edu.illinois.cs242.model;

import junit.framework.TestCase;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 11, 2010
 *
 * <p> <p> Tests that the factory for producing <code>CompressionClient</code> builds correct <code>CompressionClient</code>
 *          based on the given <code>CompressionData</code> object.
 */
public class CompressionClientFactoryTest extends TestCase {

    /**
     * Constructs the <code>CompressionData</code> object given a string representing the compression algorithm
     */
    private CompressionData buildCompressionData(String compressionAlgorithm){
        CompressionData compressionData = new CompressionData();
        compressionData.setCompressionAlgorithm(compressionAlgorithm);
        return compressionData;
    }

    /**
     * Constructs a <code>RLECompressionClient</code> using the factory and verifies its correctness
     */
    public void testBuildRLECompressionClient() {

        //Setup
        CompressionData compressionData = buildCompressionData(CompressionConstants.RLE);

        //Test & Verify (Will fail if we can't cast back to what we expect)
        try {
            RLECompressionClient compressionClient = (RLECompressionClient) CompressionClientFactory.buildCompressionClient(compressionData);
        } catch (Exception e) {
            fail("Should not have thrown an exception when casting CompressionClient to RLECompressionClient!");
        }
    }

    /**
     * Constructs a <code>IndexedColorCompressionClient</code> using the factory and verifies its correctness
     */
    public void testBuildIndexedCompressionClient() {

        //Setup
        CompressionData compressionData = buildCompressionData(CompressionConstants.INDEXED_COLOR);

        //Test & Verify (Will fail if we can't cast back to what we expect)
        try {
            IndexedColorCompressionClient compressionClient = (IndexedColorCompressionClient) CompressionClientFactory.buildCompressionClient(compressionData);
        } catch (Exception e) {
            fail("Should not have thrown an exception when casting CompressionClient to IndexedColorCompressionClient!");
        }

    }
}
