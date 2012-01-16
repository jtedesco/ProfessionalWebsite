package cs242.illinois.edu.utilities;

import cs242.illinois.edu.graph.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Oct 8, 2010
 *
 * <p> <p> Implements common utilities performed on the flight graph
 */
public class FlightGraphUtilities {

    /**
     * Grabs the vertices of a graph as flight graph vertices
     *
     * @param graph The graph from which to grab the vertices
     * @return The list of vertices
     */
    public static List<FlightGraphVertex> getFlightGraphVertices(Graph graph) {
        List<Vertex> vertices = graph.getVertexList();
        List<FlightGraphVertex> cities = new ArrayList<FlightGraphVertex>();
        for(Vertex vertex : vertices){
            cities.add((FlightGraphVertex) vertex);
        }
        return cities;
    }

    /**
     * Grabs the edges from a graph as flight graph edges
     *
     * @param graph The graph from which to grab the edges
     * @return The list of edges
     */
    public static List<FlightGraphEdge> getFlightGraphEdges(Graph graph) {
        List<FlightGraphEdge> flights = new ArrayList<FlightGraphEdge>();
        List<Edge> edges = graph.getEdgeList();
        for(Edge edge : edges){
            flights.add((FlightGraphEdge) edge);
        }
        return flights;
    }
}