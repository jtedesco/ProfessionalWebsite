package cs242.illinois.edu.graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> An abstract edge for a simple graph structure
 */
public class Edge {

    /**
     * The unordered pair of vertices adjacent by this edge.
     */
    Pair<Vertex, Vertex> vertices;

    /**
     * Constructor for an abstract edge, taking an unordered pair of vertices
     *
     * @param vertices The pair of vertices that this edge connects
     */
    public Edge(Pair<Vertex, Vertex> vertices) {
        this.vertices = vertices;
    }

    /**
     * Gets the unordered pair of vertices adjacent by this edge
     *
     * @return The unordered pair of vertices
     */
    public Pair<Vertex, Vertex> getVertices() {
        return vertices;
    }

    /**
      * <p> Checks the equality of this <code>Edge</code> to another <code>Edge</code>.
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
        if (!(otherObject instanceof Edge) || otherObject==null)
            return false;

        //Compare the pairs of vertices
        Edge other = (Edge) otherObject;
        if (vertices != null ? !vertices.equals(other.vertices) : other.vertices != null)
            return false;

        //If we got here, they are equal
        return true;
    }

}
