package filter;

/**
 * User: Jon
 * Date: 4/21/11
 * Time: 9:05 PM
 *
 * Represents the tag type for an edge, i.e. associated with an artist, song, and/or album.
 */
public enum TagType {
    artist (5.0),
    album  (2.0),
    song   (1.0);

    /**
     * The weight of this tag, to be used for finding shortest paths in a graph
     */
    private double weight;

    /**
     * Builds a tag given a particular weight for the tag
     * @param weight    The weight of the tag
     */
    private TagType(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
