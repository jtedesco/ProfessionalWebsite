package filter.graph;

/**
 * User: Jon
 * Date: 4/23/11
 * Time: 4:54 PM
 *
 * Represents a high - level tag, or some arbitrary type. This extends <code>TagGraphVertex</code>
 *  to be used as a type of vertex in the graph structure we build.
 *
 *  This is needed for the graph structure!
 */
public class Tag extends TagGraphVertex {

    /**
     * The value of this tag
     */
    private final String tagValue;

    public Tag(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getTagValue() {
        return tagValue;
    }
}
