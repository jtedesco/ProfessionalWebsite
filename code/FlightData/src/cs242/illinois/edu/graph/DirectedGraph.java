package cs242.illinois.edu.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p>
 */
public class DirectedGraph implements Graph{

    /**
     * A list of <code>Vertex</code> objects representing the vertices of the graph.
     */
    List<Vertex> vertices;

    /**
     * A list of <code>Edge</code> objects representing the edges of the graph
     */
    List<DirectedEdge> edges;

    /**
     * Constructed for the <code>DirectedGraph</code> class that initializes the edge and vertex lists to empty lists
     */
    public DirectedGraph() {
        vertices = new ArrayList<Vertex>();
        edges = new ArrayList<DirectedEdge>();
    }

    /**
     * Constructor for the graph structure that takes as parameters the list of vertices and edges to initialize the graph
     *
     * @param vertices The list of vertices in the graph
     * @param edges The list of edges in the graph
     * @throws GraphException If one or more of the input parameters is null
     */
    public DirectedGraph(List<Vertex> vertices, List<DirectedEdge> edges) throws GraphException {
        this();
        if (vertices!=null && edges!=null) {
            this.vertices = vertices;
            this.edges = edges;
        } else {
            throw new GraphException("Cannot constructed DirectedGraph with null parameters!");
        }
    }

    /**
     * Returns the in degree of the <code>Vertex</code>, or the number of incoming edges to a given vertex
     *
     * <p> If the given vertex does not exist in the graph, this function will throw an exception
     *
     * @param vertex The <code>Vertex</code> to check
     * @return The in degree of the given <code>Vertex</code> object
     * @throws GraphException If the function cannot find the parameter in the graph function
     */
    public int findInDegree(Vertex vertex) throws GraphException{

        //Error check
        if(isValid(vertex)){

            //Find each edge that has this vertex as a target
            int inDegree = 0;
            for(DirectedEdge edge : edges){
                if(edge.getTarget()!=null && edge.getTarget().equals(vertex)){
                    inDegree++;
                }
            }
            return inDegree;
            
        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * Returns the out degree of a given <code>Vertex</code>, or the number of edges directed from the given vertex
     * to other <code>Vertex</code> objects in the graph
     *
     * <p> If the given vertex does not exist in the graph, this function will throw an exception
     *
     * @param vertex The <code>Vertex</code> to check
     * @return The out degree of the <code>Vertex</code> object
     * @throws GraphException If the function cannot find the parameter in the graph function
     */
    public int findOutDegree(Vertex vertex) throws GraphException{

        //Error check
        if(isValid(vertex)){

            return vertex.getAdjacentEdgesAndVertices().size();

        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * Find the origin <code>Vertex</code> of the given <code>Edge</code>
     *
     * @param edge The <code>Edge</code> to parse
     * @return The origin of the given <code>Edge</code>
     * @throws GraphException If the function cannot find the parameter in the graph function
     */
    public Vertex getEdgeSource(Edge edge) throws GraphException{

        //Error check
        if(isValid(edge)){

            DirectedEdge directedEdge = (DirectedEdge) edge;
            return directedEdge.getSource();

        } else {
            throw new GraphException("Invalid edge parameter");
        }
    }

    /**
     * Find the target <code>Vertex</code> of the given <code>Edge</code>.
     *
     * <p> If the edge is not a directed edge, or if the edge is null, this function will return null.
     *
     * @param edge The <code>Edge</code> to parse
     * @return The target <code>Vertex</code> of the given <code>Edge</code>
     * @throws GraphException If the function cannot find the parameter in the graph function
     */
    public Vertex getEdgeTarget(Edge edge) throws GraphException{

        //Error check
        if(isValid(edge)){

            DirectedEdge directedEdge = (DirectedEdge) edge;
            return directedEdge.getTarget();

        } else {
            throw new GraphException("Invalid edge parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges.

     * <p> Specifically, this function assumes that the given source and target vertex already exist in the graph structure. If
     *      either or both of these vertices do not exist in the graph structure when this method is called, this method
     *      will return false and will not add an edge between the two vertices, and will not create these two vertices.
     *
     * <p> If the edge being added already exists in the graph, this function will fail
     */
    public boolean addEdge(Edge edge) throws GraphException{

        //Error check
        if(edge!=null){

            DirectedEdge directedEdge = (DirectedEdge) edge;

            //Check to see if vertices already exist
            if(vertices.contains(directedEdge.getSource()) && vertices.contains(directedEdge.getTarget())){

                //Add the edge to the edge list
                edges.add(directedEdge);

                //Update the source vertex with adjacency information
                Vertex source = directedEdge.getSource();
                Vertex target = directedEdge.getTarget();
                source.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(target, edge));

                //Success
                return true;

            } else {
                throw new GraphException("Vertices do not already exist in the graph structure");
            }
        } else {
            throw new GraphException("Invalid edge parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges.
     *
     * <p> Specifically, this function assumes that the given source and target vertex already exist in the graph structure. If
     *      either or both of these vertices do not exist in the graph structure when this method is called, this method
     *      will return false and will not add an edge between the two vertices, and will not create these two vertices.
     *
     * <p> If the edge being added already exists in the graph, this function will fail
     *
     * <p> This function delegates its work to the alternate addEdge() function
     */
    public boolean addEdge(Vertex source, Vertex target) throws GraphException{

        //Error-check
        if(isValid(source) && isValid(target)){

            //Create the new edge
            DirectedEdge newEdge = new DirectedEdge(source, target);

            if(edges.contains(newEdge)){

                return false;
            } else {

                //Add the edge
                return addEdge(newEdge);
            }

        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public boolean addVertex(Vertex vertex) throws GraphException{

        //Error-check
        if(vertex!=null){

            //Add it!
            vertices.add(vertex);
            return true;

        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public boolean adjacent(Vertex source, Vertex target) throws GraphException{

        //Error-check
        if(isValid(source) && isValid(target)){

            //Check to see if the adjacent vertices list of the first contains the second
            for(EdgeVertexBean edgeVertexPair : source.getAdjacentEdgesAndVertices()){
                if(edgeVertexPair.getVertex().equals(target)){
                    return true;
                }
            }

            //Otherwise, we haven't found it
            return false;

        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public boolean containsEdge(Edge edge) throws GraphException{

        //Error check
        if(edge!=null){

            //Contains this edge?
            return edges.contains(edge);

        } else {
            throw new GraphException("Invalid edge parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public boolean containsVertex(Vertex vertex) throws GraphException{

        //Error-check
        if(vertex!=null){

            //Contains this vertex?
            return vertices.contains(vertex);

        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     * <p> This particular implementation will delegate the majority of its work the findInDegree and findInDegree functions
     */
    public int findDegree(Vertex vertex) throws GraphException{
        return findInDegree(vertex) + findOutDegree(vertex);
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public List<Edge> getAdjacentEdges(Vertex vertex) throws GraphException{

        //Error-check
        if(isValid(vertex)){

            //Get list and return it
            List<Edge> edges = new ArrayList<Edge>();
            for(EdgeVertexBean edgeVertexBean : vertex.getAdjacentEdgesAndVertices()){
                edges.add(edgeVertexBean.getEdge());
            }
            return edges;

        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public List<Vertex> getAdjacentVertices(Vertex vertex) throws GraphException{

        //Error-check
        if(isValid(vertex)){

            //Get list and return it
            List<Vertex> vertices = new ArrayList<Vertex>();
            for(EdgeVertexBean edgeVertexBean : vertex.getAdjacentEdgesAndVertices()){
                vertices.add(edgeVertexBean.getVertex());
            }
            return vertices;

        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public List<Edge> getEdgeList() {
        List<Edge> returnList = new ArrayList<Edge>();
        returnList.addAll(edges);
        return returnList;
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public List<Vertex> getVertexList() {
        return vertices;
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public Edge getEdge(Vertex source, Vertex target) throws GraphException{

        //Error-check
         if(isValid(source) && isValid(target)){

             //Check to see if they are adjacent
             boolean adjacent = adjacent(source, target);

             //If they're not connected, return null, otherwise, return the edge connecting them
             if(adjacent){

                 List<EdgeVertexBean> edgeVertexBeans = source.getAdjacentEdgesAndVertices();
                 for(EdgeVertexBean edgeVertexBean : edgeVertexBeans){
                     if(edgeVertexBean.getVertex().equals(target)){
                         return edgeVertexBean.getEdge();
                     }
                 }
                 return null;

             } else {
                 return null;
             }

         } else {
             throw new GraphException("Invalid vertex parameter");
         }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     *
     * <p> This will return an unordered pair of vertices belonging to the given edge. If the edge is null, null will be returned.
     */
    public Pair<Vertex, Vertex> getVertices(Edge edge) throws GraphException{
        if(edge!=null){
            return edge.getVertices();
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public boolean removeEdge(Edge edge) throws GraphException{

        //Error-check
        if(isValid(edge)){

            //If the edge is not in the edge list, throw an exception
            if(edges.contains(edge)){

                //Remove the edge from the edge list
                edges.remove(edge);

                //Remove the adjacency in the vertices
                DirectedEdge directedEdge = (DirectedEdge) edge;
                Vertex source = directedEdge.getSource();
                Vertex target = directedEdge.getTarget();
                source.removeEdgeAndVertexFromAdjacentEdgesAndVertices(new EdgeVertexBean(target, edge));

                //Success!
                return true;
                
            } else {
                throw new GraphException("Cannot remove edge, it does not exist");
            }

        } else {
            throw new GraphException("Invalid edge parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public boolean removeEdge(Vertex source, Vertex target) throws GraphException{
        //Error-check
         if(isValid(source) && isValid(target)){

             //Remove the edge
             DirectedEdge expectedEdge = new DirectedEdge(source, target);
             return removeEdge(expectedEdge);

        } else {
             throw new GraphException("Invalid vertex parameter");
         }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public boolean removeVertex(Vertex vertex) throws GraphException{

        //Error-check
        if(isValid(vertex)){

            //Create a local copy of the edge list to avoid multithreaded problems
            List<DirectedEdge> localEdges = new ArrayList<DirectedEdge>();
            localEdges.addAll(edges);

            //If the vertex is attached to an edge, remove each edge connected to this vertex
            for(DirectedEdge edge : localEdges){
                if(edge.getSource().equals(vertex) || edge.getTarget().equals(vertex)){
                    removeEdge(edge);
                }
            }

            //If we found no problems, remove it
            vertices.remove(vertex);
            return true;

        } else {
            throw new GraphException("Invalid vertex parameter");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     *
     * <p> If the List does not contain <code>DirectedEdge</code> variables, this method will do nothing
     */
    public void setEdgeList(List<Edge> edges) {
        if (edges!=null && edges.size()>0 && (edges.get(0) instanceof DirectedEdge)) {
            this.edges = new ArrayList<DirectedEdge>();
            for(Edge edge : edges)
                this.edges.add((DirectedEdge) edge);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p> This implementation of the graph structure deals with a directed graph, using standard vertices but directed edges
     */
    public void setVertexList(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    /**
     * Compares the two objects, and returns a boolean based in their equality. Specifically, this will compare the edge
     *  and vertex lists. If the given object and this object are the exact same objects, then this will be true, but if
     *  their edge and vertex lists are equivalent, these graphs are also equivalent.
     *
     * @param otherObject The object with which to compare this object
     * @return True or false, based on the equality of the objects
     */
    @Override
    public boolean equals(Object otherObject) {

        //Check to see if these two graphs are the exact same objects
        if (this == otherObject)
            return true;

        //Check that the object is of the right type
        if (!(otherObject instanceof DirectedGraph))
            return false;

        //Compare the fields
        DirectedGraph other = (DirectedGraph) otherObject;
        if (edges != null ? !edges.equals(other.edges) : other.edges != null)
            return false;
        if (vertices != null ? !vertices.equals(other.vertices) : other.vertices != null)
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
        int result = vertices != null ? vertices.hashCode() : 0;
        result = 31 * result + (edges != null ? edges.hashCode() : 0);
        return result;
    }

    /**
     * Private helper function to test to see if a vertex is valid
     *
     * @param vertex The vertex to check
     * @return True or false, whether or not the vertex is valid
     */
    private boolean isValid(Vertex vertex) {
        return vertex!=null && vertices.contains(vertex);
    }

    /**
     * Private helper function to test to see if an edge is valid
     *
     * @param edge The edge to check
     * @return True or false, whether or not the edge is valid
     */
    private boolean isValid(Edge edge) {
        return (edge!=null && edges.contains(edge));
    }
}
