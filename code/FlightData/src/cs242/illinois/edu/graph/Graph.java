package cs242.illinois.edu.graph;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> An abstract graph implementation using abstract vertices and edges
 */
public interface Graph {

    /**
     * Add an <code>Edge</code>
     *
     * @param newEdge The new <code>Edge</code> to be added to the graph
     * @return True on success, false otherwise
     * @throws GraphException If the parameter is null or otherwise invalid
     */
    public boolean addEdge(Edge newEdge) throws GraphException;

    /**
     * Add an <code>Edge</code> between two <code>Vertex</code> objects
     *
     * @param source The source <code>Vertex</code>
     * @param target The target <code>Vertex</code>
     * @return True on success, false otherwise
     * @throws GraphException If the function cannot find the parameter in the graph function or the parameters are invalid
     */
    public boolean addEdge(Vertex source, Vertex target) throws GraphException;

    /**
     * Add a <code>Vertex</code> to the graph
     *
     * @param newVertex The <code>Vertex</code> to be added
     * @return True on success, false otherwise
     * @throws GraphException If the parameters are invalid
     */
    public boolean addVertex(Vertex newVertex) throws GraphException;

    /**
     * Tests to see if two <code>Vertex</code> objects are adjacent in the graph.
     *
     * <p> If one or more of the vertices do not exist in the graph structure, this method will throw an exception
     *
     * @param source The source <code>Vertex</code>
     * @param target The target <code>Vertex</code>
     * @return True if these two <code>Vertex</code> objects are adjacent, false otherwise
     * @throws GraphException If the function cannot find the parameter in the graph function
     */
    public boolean adjacent(Vertex source, Vertex target) throws GraphException;

    /**
     * Checks to see whether a particular <code>Edge</code> is in the graph
     *
     * @param edge The <code>Edge</code> to find in the graph structure
     * @return True if the graph contains this <code>Edge</code>, false otherwise
     * @throws GraphException If the parameter is invalid
     */
    public boolean containsEdge(Edge edge) throws GraphException;

    /**
     * Checks to see whether a particular <code>Vertex</code> exists in the graph structure
     *
     * @param vertex The <code>Vertex</code>
     * @return True if the <code>Vertex</code> exists, false otherwise
     * @throws GraphException If the parameter is invalid
     */
    public boolean containsVertex(Vertex vertex) throws GraphException;

    /**
     * Returns the total degree of a given <code>Vertex</code> object, or the total number of incoming and outgoing
     * <code>Edge</code> objects from/to the given <code>Vertex</code>
     *
     * @param vertex The <code>Vertex</code> object to calculate
     * @return The total degree of the <code>Vertex</code>
     * @throws GraphException If the function cannot find the parameter in the graph function or if the parameter is invalid
     */
    public int findDegree(Vertex vertex) throws GraphException;

    /**
     * Get the list of <code>Edge</code> objects attached to the given <code>Vertex</code>
     *
     * @param vertex The <code>Vertex</code> to check
     * @return The list of <code>Edge</code> objects representing the edges attached to the given <code>Vertex</code>
     * @throws GraphException If the function cannot find the parameter in the graph function or if the parameter is invalid
     */
    public List<Edge> getAdjacentEdges(Vertex vertex) throws GraphException;

    /**
     * Get the list of <code>Vertex</code> objects adjacent to the given <code>Vertex</code>
     *
     * @param vertex The <code>Vertex</code> to check
     * @return The list of <code>Vertex</code> objects adjacent to the given <code>Vertex</code> object
     * @throws GraphException If the function cannot find the parameter in the graph function or the parameter is invalid
     */
    public List<Vertex> getAdjacentVertices(Vertex vertex) throws GraphException;

    /**
     * Returns the list of all <code>Edge</code> objects in the <code>FlightGraph</code>
     *
     * @return The list of edges
     */
    public List<Edge> getEdgeList();

    /**
     * Returns the list of <code>Vertex</code> objects in the <code>FlightGraph</code>
     *
     * @return The list of vertices
     */
    public List<Vertex> getVertexList();

    /**
     * Get the <code>Edge</code> adjacent the two given <code>Vertex</code> objects
     *
     * @param source The source <code>Vertex</code>
     * @param target The target <code>Vertex</code>
     * @return The <code>Edge</code> connecting the two given vertices
     * @throws GraphException If the function cannot find the parameter in the graph function or the parameters are invalid
     */
    public Edge getEdge(Vertex source, Vertex target) throws GraphException;

    /**
     * Finds the unordered pair of vertices associated with an edge
     *
     * @param edge The edge from which to find the endpoints
     * @return The endpoints of the edge, represented as a <code>Pair</code> of vertices
     * @throws GraphException If the function cannot find the parameter in the graph function or the parameter is invalid
     */
    public Pair<Vertex, Vertex> getVertices(Edge edge) throws GraphException;

    /**
     * Removes the given <code>Edge</code> from the <code>FlightGraph</code>
     *
     * @param edge The <code>Edge</code> to remove from the <code>FlightGraph</code>
     * @return True on success, false otherwise
     * @throws GraphException If the function cannot find the parameter in the graph function or the parameter is invalid
     */
    public boolean removeEdge(Edge edge) throws GraphException;

    /**
     * Removes the edge connecting the two given <code>Vertex</code> objects
     *
     * @param source The source of the <code>Edge</code>
     * @param target The target of the <code>Edge</code>
     * @return True on success, false otherwise
     * @throws GraphException If the function cannot find the parameter in the graph function or the parameters are invalid
     */
    public boolean removeEdge(Vertex source, Vertex target) throws GraphException;

    /**
     * Remove the given <code>Vertex</code> from the <code>FlightGraph</code>
     *
     * @param vertex The <code>Vertex</code> to be removed
     * @return True on success, false otherwise
     * @throws GraphException If the function cannot find the parameter in the graph function or the parameter is invalid
     */
    public boolean removeVertex(Vertex vertex) throws GraphException;

    /**
     * Sets the list of edges in the graph implementation to the given parameter
     *
     * @param edges The new list of edges for the graph
     */
    public void setEdgeList(List<Edge> edges);

    /**
     * Sets the list of vertices in the graph to the given list of vertices
     *
     * @param vertices The new list of vertices for the graph
     */
    public void setVertexList(List<Vertex> vertices);
}
