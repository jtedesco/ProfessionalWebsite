package cs242.illinois.edu.graph;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> Simple container for an <code>Edge</code>, <code>Vertex</code> pair. Useful for lists of adjacent vertices and edges.
 */
public class EdgeVertexBean {

    /**
     * The vertex
     */
    Vertex vertex;

    /**
     * The edge adjacent to the vertex
     */
    Edge edge;

    public EdgeVertexBean(Vertex vertex, Edge edge) {
        this.vertex = vertex;
        this.edge = edge;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    /**
     * Checks the equality of these two beans
     *
     * @param otherObject The object with which to compare this object
     * @return Whether the objects are equal
     */
    @Override
    public boolean equals(Object otherObject) {

        //If these are the exact same objects
        if (this == otherObject)
            return true;

        //If this is the wrong type, not equal
        if (!(otherObject instanceof EdgeVertexBean))
            return false;

        //Compare the fields
        EdgeVertexBean that = (EdgeVertexBean) otherObject;
        if (edge != null ? !edge.equals(that.edge) : that.edge != null)
            return false;
        if (vertex != null ? !vertex.equals(that.vertex) : that.vertex != null)
            return false;

        //If we got here, they are equal
        return true;
    }

    /**
     * Generates a hash code for this ordered pair
     *
     * @return The generated hash code
     */
    @Override
    public int hashCode() {
        int result = vertex != null ? vertex.hashCode() : 0;
        result = 31 * result + (edge != null ? edge.hashCode() : 0);
        return result;
    }
}
