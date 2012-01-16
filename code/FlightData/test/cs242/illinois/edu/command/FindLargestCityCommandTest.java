package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.*;
import cs242.illinois.edu.utilities.FlightGraphUtilities;
import junit.framework.TestCase;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> Tests the cs242.illinois.edu.command to find the largest city
 */
public class FindLargestCityCommandTest extends TestCase {

    private Graph graph;
    private List<Vertex> vertices;
    private List<DirectedEdge> edges;

    /**
     * Builds the sample graph to parse
     */
    @Before
    public void setUp() throws GraphException {
        vertices = buildVertexList();
        edges = buildEdgeList();
        graph = new DirectedGraph(vertices, edges);
    }

    /**
     * Tests that the largest city we find is correct, based on the sample input data
     */
    public void testFindLargestCityCommand() {

        //Setup
        FindLargestCityCommand command = new FindLargestCityCommand();
        List<FlightGraphEdge> castEdges = new ArrayList();
        for(int i = 0; i<edges.size(); i++){
            castEdges.add((FlightGraphEdge) edges.get(i));
        }
        FlightGraphVertex expectedValue = (new FlightGraphVertex("MEX", "Bogota", "MX", "North America", -6, new Pair('N', 19), new Pair('W', 99), 23400000, 1));

        //Test
        List<FlightGraphVertex> cities = FlightGraphUtilities.getFlightGraphVertices(graph);
        FlightGraphVertex actualValue = command.findLargestCity(cities);

        //Verify
        assertEquals(expectedValue, actualValue);
    }

    /**
     * Private helper function to build a list of vertices containing sample airport data to be used in testing
     *
     * @return A list of vertices representing airports for testing
     */
    private List<Vertex> buildVertexList() {

        List<Vertex> vertexList = new ArrayList<Vertex>();

        //Add some sample airport data to a vertex list
        vertexList.add(new FlightGraphVertex("BOG", "Bogota", "CO", "South America", -5, new Pair('N', 5), new Pair('W', 74), 8600000, 1));
        vertexList.add(new FlightGraphVertex("SCL", "Santiago", "CL", "South America", -4, new Pair('S', 33), new Pair('W', 71), 6000000, 1));
        vertexList.add(new FlightGraphVertex("LIM", "Lima", "PE", "South America", -5, new Pair('S', 12), new Pair('W', 77), 9050000, 1));
        vertexList.add(new FlightGraphVertex("MEX", "Bogota", "MX", "North America", -6, new Pair('N', 19), new Pair('W', 99), 23400000, 1));
        vertexList.add(new FlightGraphVertex("CAI", "Cairo", "EG", "Africa", 2, new Pair('N', 30), new Pair('E', 31), 15200000, 1));
        vertexList.add(new FlightGraphVertex("PAR", "Paris", "FR", "Europe", 1, new Pair('N', 49), new Pair('E', 2), 10400000, 3));
        vertexList.add(new FlightGraphVertex("HKG", "Hong Kong", "CH", "Asia", 8, new Pair('N', 22), new Pair('E', 114), 7050000, 4));
        vertexList.add(new FlightGraphVertex("SYD", "Sydney", "AU", "Asia", 10, new Pair('S', 34), new Pair('E', 151), 4475000, 4));

        return vertexList;
    }

    /**
     * Private helper function to build a list of vertices containing sample airport data to be used in testing
     *
     * @return A list of vertices representing airports for testing
     */
    private List<DirectedEdge> buildEdgeList() {

        List<DirectedEdge> edgeList = new ArrayList<DirectedEdge>();

        //Add some sample airport data to a vertex list
        edgeList.add(new FlightGraphEdge(randomShort(), getRandomVertex(), getRandomVertex()));
        edgeList.add(new FlightGraphEdge(randomShort(), getRandomVertex(), getRandomVertex()));
        edgeList.add(new FlightGraphEdge(randomShort(), getRandomVertex(), getRandomVertex()));
        edgeList.add(new FlightGraphEdge(randomShort(), getRandomVertex(), getRandomVertex()));
        edgeList.add(new FlightGraphEdge(randomShort(), getRandomVertex(), getRandomVertex()));
        edgeList.add(new FlightGraphEdge(randomShort(), getRandomVertex(), getRandomVertex()));

        return edgeList;
    }

    /**
     * Private helper function to generate some random positive short
     */
    private short randomShort() {
        Random random = new Random();
        int data = random.nextInt();
        return (short) Math.abs(data);
    }

    /**
     * Simple helper function to grab a random vertex from the vertices list
     *
     * @return A random vertex from our list
     */
    private Vertex getRandomVertex() {
        return vertices.get((int) (Math.random() * vertices.size()));
    }
}
