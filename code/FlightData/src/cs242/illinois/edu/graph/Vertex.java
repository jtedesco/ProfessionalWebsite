package cs242.illinois.edu.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> Abstract implementation of a graph vertex. Simply holds the skeleton data for an adjacency list implementation of a graph.
 */
public class Vertex {

    /**
     * A list of <code>Edge</code>, <code>Vertex</code> pairs representing adjacent vertices and the connecting edges
     */
    List<EdgeVertexBean> adjacentEdgesAndVertices;

    /**
     * Default constructor for an abstract <code>Vertex</code>, that initializes the list to an empty list of
     *  <code>EdgeVertexBean</code> objects
     */
    public Vertex() {
        adjacentEdgesAndVertices = new ArrayList<EdgeVertexBean>();
    }

    /**
     * Constructor for the <code>Vertex</code> that takes a list of adjacent <code>Vertex</code> objects and the
     *  corresponding <code>Edge</code> objects
     *
     * @param adjacentEdgesAndVertices The list with which to build the <code>Vertex</code>
     */
    public Vertex(List<EdgeVertexBean> adjacentEdgesAndVertices) {
        this.adjacentEdgesAndVertices = adjacentEdgesAndVertices;
    }

    /**
     * Gets the list of <code>EdgeVertexBean</code> objects associated with this vertex
     *
     * @return The list of <code>EdgeVertexBean</code> objects associated with this vertex
     */
    public List<EdgeVertexBean> getAdjacentEdgesAndVertices() {
        return adjacentEdgesAndVertices;
    }

    /**
     * Add an edge & vertex to the list of <code>EdgeVertexBean</code> objects associated with this vertex
     *
     * @return The updated list of <code>EdgeVertexBean</code> objects associated with this vertex
     */
    public List<EdgeVertexBean> addEdgeAndVertexToAdjacentEdgesAndVertices(EdgeVertexBean adjacentPair) {
        adjacentEdgesAndVertices.add(adjacentPair);
        return adjacentEdgesAndVertices;
    }

    /**
     * Remove an edge & vertex to the list of <code>EdgeVertexBean</code> objects associated with this vertex
     *
     * @return The updated list of <code>EdgeVertexBean</code> objects associated with this vertex
     */
     public List<EdgeVertexBean> removeEdgeAndVertexFromAdjacentEdgesAndVertices(EdgeVertexBean adjacentPair) {
         adjacentEdgesAndVertices.remove(adjacentPair);
         return adjacentEdgesAndVertices;
     }

    /**
     * <p> Checks the equality of this <code>Vertex</code> to another <code>Vertex</code>.
     *
     * <p> Equality of two vertices is based on the equality of their lists of adjacent edges/nodes. If the two lists
     *      are equivalent, then the vertices are equivalent.
     *
     * @param otherObject The object with which to compare this object
     * @return True or false, based on the equality of the object to this object
     */
    @Override
    public boolean equals(Object otherObject) {

        //Check to see if the other object is not null and is of the correct type first
        if(otherObject!=null && otherObject instanceof Vertex){

            //Are these the exact same objects?
            if(this == otherObject)
                return true;

            //Cast the object to a vertex
            Vertex other = (Vertex) otherObject;

            //Return true if the list of adjacent edges/vertices are equal
            if(other.getAdjacentEdgesAndVertices()==null && getAdjacentEdgesAndVertices()==null){
                return true;
            } else if(other.getAdjacentEdgesAndVertices().equals(getAdjacentEdgesAndVertices())){
                return true;
            }
        }

        //Not equal otherwise
        return false;
    }

    /**
     * Generates a hash code for this ordered pair
     *
     * @return The generated hash code
     */
    @Override
    public int hashCode() {
        return adjacentEdgesAndVertices != null ? adjacentEdgesAndVertices.hashCode() : 0;
    }
}
