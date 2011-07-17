package edu.illinois.cs242.model;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 8, 2010
 *
 * <p> <p> Factory class containing only one, static method: <code> buildCompressionClient </code>. This class is used for
 * producing a <code>CompressionClient</code> object abstractly, using a given <code>CompressionData</code> object.

 * @author Jon Tedesco
 * @see edu.illinois.cs242.model.CompressionClient
 * @see edu.illinois.cs242.model.CompressionData
 */

public class CompressionClientFactory {
    public static CompressionClient buildCompressionClient(CompressionData data){
        try{
            return CompressionTypes.valueOf(data.getCompressionAlgorithm()).buildCompressionClient();
        } catch (Exception e){
            throw new UnsupportedOperationException("Error building CompressionClient:\n" + e.getMessage());
        }
    }
}
