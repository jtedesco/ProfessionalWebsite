package filter.graph;

/**
 * Created by IntelliJ IDEA.
 * User: Jon
 * Date: 4/14/11
 * Time: 10:41 PM
 *
 * Generic interface that represents a node of the graph of the <code>TagGraphFilter</code> class.
 */
public abstract class TagGraphVertex implements Comparable<TagGraphVertex> {

    /**
     * This value is used to find the barycenters of the tag graph. This weight represents the value used for finding
     * the barycenters of the graph. The barycenters of the graph will be computed by finding the nodes with the minimum
     * value bary values.
     */
    private double baryValue;

    public TagGraphVertex() {
        baryValue = 0.0;
    }

    public TagGraphVertex(double baryValue) {
        this.baryValue = baryValue;
    }

    public double getBaryValue() {
        return baryValue;
    }

    public void setBaryValue(double baryValue) {
        this.baryValue = baryValue;
    }

    /**
     * {@inheritDoc}
     *
     * Compares this tag to another. A tag is considered to come 'before' another if its bary weight is less
     *  than another.
     *
     * @param   otherVertex The vertex to which this vertex is being compared
     * @return  0, 1, or -1, based on the bary value of this vertex as compared to another
     */
    public int compareTo(TagGraphVertex otherVertex) {
        if(getBaryValue() == otherVertex.getBaryValue()) {
            return 0;
        } else if(getBaryValue() < otherVertex.getBaryValue()) {
            return -1;
        } else {
            return 1;
        }
    }
}
