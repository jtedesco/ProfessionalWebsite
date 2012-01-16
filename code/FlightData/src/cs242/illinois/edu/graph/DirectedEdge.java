package cs242.illinois.edu.graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> An abstract directed edge for a directed graph structure.
 */
public class DirectedEdge extends Edge{

    /**
     * The target of the edge
     */
    private Vertex target;

    /**
     * The source of the edge
     */
    private Vertex source;

    /**
     * Constructor for a directed edge, builds the corresponding Edge object as well
     *
     * @param source The source of the edge
     * @param target The target of the edge
     */
    public DirectedEdge(Vertex source, Vertex target) {
        super(new Pair(source, target));

        this.target = target;
        this.source = source;
    }

    /**
     * Returns the <code>Vertex</code> object that the <code>DirectedEdge</code> is pointing <i>to</i>
     *
     * @return The <code>Vertex</code> object pointed to by the edge
     */
    public Vertex getTarget() {
        return target;
    }

    /**
     * Returns the <code>Vertex</code> object from which this edge originates
     *
     * @return The <code>Vertex</code> object form which this directed edge originates
     */
    public Vertex getSource() {
        return source;
    }

    /**
     * Returns an unordered pair of the vertices
     *
     * @return The unordered pair of vertices
     */
    @Override
    public Pair<Vertex, Vertex> getVertices() {
        return super.getVertices();
    }

    /**
      * <p> Checks the equality of this <code>DirectedEdge</code> to another <code>DirectedEdge</code>.
      *
      * <p> Equality of two vertices is based on the equality of their pairs of vertices. If the two pairs
      *      are equivalent, then the edges are equivalent.
      *
      * @param otherObject The object with which to compare this object
      * @return True or false, based on the equality of the object to this object
      */
    @Override
    public boolean equals(Object otherObject) {

        //If these two objects are the exact same object, then they are equal
        if (this == otherObject)
            return true;

        //If this is the wrong type, not equal
        if (!(otherObject instanceof DirectedEdge))
            return false;

        //Check to see that their abstract structures are equivalent
        if (!super.equals(otherObject))
            return false;

        //Compare the pairs of vertices
        DirectedEdge that = (DirectedEdge) otherObject;
        if (source != null ? !source.equals(that.source) : that.source != null)
            return false;
        if (target != null ? !target.equals(that.target) : that.target != null)
            return false;

        //If we got here, then they are equal
        return true;
    }

    /**
     * Generates a hash code for this ordered pair
     *
     * @return The generated hash code
     */
    @Override
    public int hashCode() {
        int result = target != null ? target.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }
}
