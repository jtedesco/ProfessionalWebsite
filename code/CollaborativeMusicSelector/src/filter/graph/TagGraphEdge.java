package filter.graph;

import filter.TagType;
import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * User: Jon
 * Date: 4/23/11
 * Time: 5:18 PM
 *
 * Necessary to create edge objects to be used in the graph
 */
public class TagGraphEdge extends DefaultWeightedEdge {

    private final TagType tagType;

    public TagGraphEdge(TagType type) {
        tagType = type;
    }

    public TagType getTagType() {
        return tagType;
    }
}
