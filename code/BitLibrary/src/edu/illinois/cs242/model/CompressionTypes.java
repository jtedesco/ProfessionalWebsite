package edu.illinois.cs242.model;

/**
 * Created By: Jon Tedesco
 * Date: Sep 8, 2010
 *
 * <p> Class used as a hash of constants from <code>CompressionConstants</code> to concrete implementations of
 * <code>CompressionClient</code>. This is used by the <code>CompressionClientFactory</code> to produce specific subtypes
 * of <code>CompressionClient</code> without using any conditionals.
 *
 * @author Jon Tedesco
 *
 * @see edu.illinois.cs242.model.CompressionClientFactory for usage of this class
 * @see CompressionConstants for constants usable with this enum
 *
 */

public enum CompressionTypes {

    RLE {
        @Override
        CompressionClient buildCompressionClient() {
            return new RLECompressionClient();
        }
    },

    indexedColor {
        @Override
        CompressionClient buildCompressionClient() {
            return new IndexedColorCompressionClient();
        }
    };

    /**
     * Abstract method overloaded by each enum value used to return a concrete implementation of <code>CompressionClient</code>
     *
     * @return CompressionClient a <code>CompressionClient</code> representing a concrete implementation based on enum values
     */
    abstract CompressionClient buildCompressionClient();
}
