package edu.illinois.cs242.model;

/**
 * Created By: Jon Tedesco
 * Date: Sep 8, 2010
 *
 * This class holds the data to be passed to both the <code> CompressionClientFactory </code> method and the
 * <code> CompressionClient </code> methods. Essentially, this class is used for encapsulating the implementation details
 * of the image compression utility by creating a state context variable, <code>CompressionData</code>, to hold the relevant
 * informations
 *
 * <p> The <code>compressionAlgorithm</code> field will be an constant from <code>CompressionConstants</code>, and
 * will be used by the <code>CompressionClientFactory</code> to determine what type of <code>CompressionClient</code> should
 * be constructed.
 *
 * @author Jon Tedesco
 * @see edu.illinois.cs242.model.CompressionClient
 * @see CompressionConstants
 * @see edu.illinois.cs242.model.CompressionClientFactory
 */

public class CompressionData {

    /**
     * Field that will be an constant from <code>CompressionConstants</code>, and will be used by the
     * <code>CompressionClientFactory</code> to determine what type of <code>CompressionClient</code> should
     * be constructed.
     */
    private String compressionAlgorithm;

    /**
     * Simple getter for the compressionAlgorithm variable
     *
     * @return compressionAlgorithm Variable representing the type of compression to be used in <code>CompressionClient</code>
     */
    public String getCompressionAlgorithm() {
        return compressionAlgorithm;
    }

    /**
     * A simple setter for the <code>compressionAlgorithm</code> variable
     *
     * @param compressionAlgorithm Variable representing the type of compression to be used in <code>CompressionClient</code>
     */
    public void setCompressionAlgorithm(String compressionAlgorithm) {
        this.compressionAlgorithm = compressionAlgorithm;
    }
}
