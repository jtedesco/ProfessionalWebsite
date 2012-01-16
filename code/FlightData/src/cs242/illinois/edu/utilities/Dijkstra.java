package cs242.illinois.edu.utilities;

import cs242.illinois.edu.graph.EdgeVertexBean;
import cs242.illinois.edu.graph.FlightGraphEdge;
import cs242.illinois.edu.graph.FlightGraphVertex;
import cs242.illinois.edu.graph.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 7, 2010
 *
 * <p> <p> Implements Dijkstra's algorithm to find the shortest path from any node in the graph to any other node in the
 *          graph
 */
public class Dijkstra
{
    /**
     * Finds the shortest path as an ordered list of vertices between two verties in the graph
     *
     * @param from The vertex from which to start
     * @param to The vertex at which to end
     * @param graph
     * @return The list of vertices representing the shortest path
     */
    public static List<FlightGraphVertex> findShortestPath(FlightGraphVertex from, FlightGraphVertex to, Graph graph){

        //First, label all the vertices in the graph with the minimum distance from the starting node using Dijkstra's
        computePaths(from);

        //Find and return the shortest path to the given vertex
        List<FlightGraphVertex> shortestPath = getShortestPathTo(to);

        //Reset the graph
        resetGraphDistances(graph);

        //Return the shortest path
        return shortestPath;
    }

    /**
     * Helper function to label the distances of each vertex in the graph from the given
     *      Note: I found this code online  (http://en.literateprograms.org/Dijkstra%27s_algorithm_%28Java%29)
     *
     * @param source The vertex from which we'll start
     */
    private static void computePaths(FlightGraphVertex source){
        source.setMinDistance(0);
        PriorityQueue<FlightGraphVertex> vertexQueue = new PriorityQueue<FlightGraphVertex>();
	    vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            FlightGraphVertex u = vertexQueue.poll();

            //Grab the list of adjacent edges
            List<FlightGraphEdge> edges = new ArrayList<FlightGraphEdge>();
            List<EdgeVertexBean> edgeVertexBeans = u.getAdjacentEdgesAndVertices();
            for(EdgeVertexBean edgeVertexBean : edgeVertexBeans){
                edges.add((FlightGraphEdge) edgeVertexBean.getEdge());
            }

            // Visit each edge exiting u
            for (FlightGraphEdge edge : edges){

                FlightGraphVertex v = (FlightGraphVertex) edge.getTarget();
                double weight = edge.getLength();
                double distanceThroughU = u.getMinDistance() + weight;

                if (distanceThroughU < v.getMinDistance()) {
                    vertexQueue.remove(v);

                    v.setMinDistance(distanceThroughU);
                    v.setPrevious(u);
                    vertexQueue.add(v);

                }
            }
        }
    }

    /**
     * Gets the shortest path in the graph to the given vertex
     *
     * @param target The destination vertex
     * @return The list of vertices, representing the shortest path
     */
    private static List<FlightGraphVertex> getShortestPathTo(FlightGraphVertex target)
    {
        List<FlightGraphVertex> path = new ArrayList<FlightGraphVertex>();
        for (FlightGraphVertex vertex = target; vertex != null; vertex = vertex.getPrevious())
            path.add(vertex);

        Collections.reverse(path);
        return path;
    }

    /**
     * Resets the Dijkstra's statistics in the graph
     *
     * @param graph The graph to reset
     */
    private static void resetGraphDistances(Graph graph){

        //Grab the vertices from the graph
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);
        for(FlightGraphVertex city : cities){
            city.setMinDistance(Double.POSITIVE_INFINITY);
            city.setPrevious(null);
        }
    }
}