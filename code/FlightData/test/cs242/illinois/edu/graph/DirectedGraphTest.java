package cs242.illinois.edu.graph;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 28, 2010
 *
 * <p> <p> Tests the functionality of the directed graph using flight graph objects
 */
public class DirectedGraphTest extends TestCase {

    /**
     * Variable to hold the directed graph to be tested
     */
    private DirectedGraph directedGraph;

    /**
     * Variable to hold the vertices for the testing graph
     */
    List<Vertex> vertices;

    /**
     * Variable to hold the edges for testing the graph
     */
    List<Edge> edges;

    /**
     * Generates some mock airport data and initializes the <code>directedGraph</code> with a list of mock
     *  <code>FlightGraphVertex</code> objects.
     *
     * <p> Initially, the test graph does not contain any edges
     */
    @Before
    public void setUp() throws GraphException {
        vertices = buildVertexList();
        edges = buildEdgeList();
        directedGraph = spy(new DirectedGraph(vertices, new ArrayList<DirectedEdge>()));
    }

    /**
     * Tests that the add edge function that takes an edge parameter works as expected, specifically in the following cases:
     *  <p> &nbsp a) If the edge is invalid, addEdge() should fail, throwing an exception
     *  <p> &nbsp b) If the vertices included in the edge do not already exist, addEdge() should fail, throwing an exception
     *  <p> &nbsp c) Otherwise, this should succeed, and we should be able find the edge after we add it, both via
     *                  the edge list, and via the connectivity of these two vertices.
     */
    @Test
    public void testAddEdgeWithEdgeParameter() {

        //Test case a, where the edge is invalid
            try {
                directedGraph.addEdge(null);
                fail("Should have thrown an exception when running addEdge(EDGE)");
            } catch (GraphException e) {}


        //Test case b, where we add an edge with vertices that do not already exist in the graph
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);
            Edge newEdge = new FlightGraphEdge(Math.random(), anotherVertex, getRandomVertex());

            //Test
            boolean addedEdgeSuccessfully = false;
            try {
                addedEdgeSuccessfully = directedGraph.addEdge(newEdge);
                fail("Should have thrown an exception when running addEdge(EDGE)");
            } catch (GraphException e) {}

        //Verify
            assertFalse(addedEdgeSuccessfully);

        //Test case c, where we try to add an edge between two existing nodes in the graph
            //Setup
            Vertex source = getRandomVertex();
            Vertex target = getRandomVertex();
            newEdge = new FlightGraphEdge(Math.random(), source, target);
            EdgeVertexBean expectedSourceEdgeVertexBean = new EdgeVertexBean(target, newEdge);

            //Test
            try {
                addedEdgeSuccessfully = directedGraph.addEdge(newEdge);
            } catch (GraphException e) {
                fail("Should not have thrown an exception when running addEdge(EDGE)");
            }

        //Verify
            assertTrue(addedEdgeSuccessfully);
            assertTrue(directedGraph.getEdgeList().contains(newEdge));

            //Further verify that the the vertices have been modified so that they are adjacent to each other
            assertTrue(source.getAdjacentEdgesAndVertices().contains(expectedSourceEdgeVertexBean));
            assertEquals(0, target.getAdjacentEdgesAndVertices().size());
    }

    /**
     * Simple test to test that the addEdge function with the two vertex parameters correctly delegates its work to the
     *  other addEdge() function
     */
    @Test
    public void testAddEdgeWithVertexParameters() {

        //Setup
        Vertex firstVertex = getRandomVertex();
        Vertex secondVertex = getRandomVertex();
        Edge expectedEdgeParameter = new DirectedEdge(firstVertex, secondVertex);

        //Test
        try {
            directedGraph.addEdge(firstVertex, secondVertex);
        } catch (GraphException e) {
            fail("Should not have thrown an exception when calling addEdge(VERTEX, VERTEX)");
        }

        //Verify
        try {
            verify(directedGraph, times(1)).addEdge(eq(expectedEdgeParameter));
        } catch (GraphException e) {
            fail("Should not have thrown an exception when calling addEdge(VERTEX, VERTEX)");
        }
    }

    /**
     * Tests that we add a vertex successfully. Essentially, just a sanity check. On adding a new vertex to a graph,
     *  nothing in the structure should change aside from the list of vertices in the graph. Tests the following cases:
     * <p> &nbsp a) invalid vertex, should throw an exception
     * <p> &nbsp b) valid vertex, should do what it says
     */
    @Test
    public void testAddVertex() {

        //Case a
            try {
                directedGraph.addVertex(null);
                fail("Should have thrown an exception when running eddVertex(NULL)");
            } catch (GraphException e) {}


        //Case b
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test
            boolean successfullyAddedVertex = false;
            try {
                successfullyAddedVertex = directedGraph.addVertex(anotherVertex);
            } catch (GraphException e) {
                fail("Should not have thrown an exception when running addVertex(VERTEX)");
            }

            //Verify
            assertTrue(successfullyAddedVertex);
            assertTrue(directedGraph.getVertexList().contains(anotherVertex));
    }

    /**
     * Test that the 'adjacent' method behaves as expected. Specifically, this tests three cases:
     *  <p> a) If one or both of the vertices do not exist in the graph. In this case, adjacent should throw an exception
     *  <p> b) If both vertices exist but they are not adjacent, return false
     *  <p> c) If both vertices exist and they are adjacent, return true
     */
    @Test
    public void testConnected() {

        //Test case a, where we have one vertex that does not exist in the graph, or is invalid
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test & verify
            try{
                directedGraph.adjacent(anotherVertex, getRandomVertex());
                fail("Should have thrown an exception calling adjacent() on nonexistent vertices");
            } catch (Exception e) {}
            try{
                directedGraph.adjacent(null, getRandomVertex());
                fail("Should have thrown an exception calling adjacent(null, ~~~~)");
            } catch (Exception e) {}

        //Test case b, where both vertices exist but they aren't adjacent
            //Test
            boolean connected = false;
            try {
                connected = directedGraph.adjacent(getRandomVertex(), getRandomVertex());
            } catch (GraphException e) {
                fail("Should not have thrown an exception when calling adjacent(VERTEX, VERTEX)");
            }

            //Verify
            assertFalse(connected);

        //Test case c, where the vertices are actually adjacent
            //Setup
            Vertex source = getRandomVertex();
            Vertex target = getRandomVertex();
            DirectedEdge newEdge = new FlightGraphEdge(Math.random(), source, target);
            try {
                directedGraph.addEdge(newEdge);
            } catch (GraphException e) {
                fail("Should not have thrown an exception when running addEdge(EDGE)");            
            }

            //Test
            try {
                connected = directedGraph.adjacent(source, target);
            } catch (GraphException e) {
                fail("Should not have thrown an exception when calling adjacent(VERTEX, VERTEX)");
            }

            //Verify
            assertTrue(connected);
    }

    /**
     * Tests a the 'containsEdge' method, which returns true or false based on whether or not an equivalent to a
     *   given edge exists in the graph.
     *
     * <p> For this test, we will test two cases, as expected:
     * <p>  a) When the edge parameter is invalid
     * <p>  b) When the edge is not in the graph
     * <p>  c) When the edge is in the graph
     */
    @Test
    public void testContainsEdge() {

        //Test case a
            //Test
            try {
                directedGraph.containsEdge(null);
                fail("Should have thrown an exception when running containsEdge(EDGE)");
            } catch (GraphException e) {}


        //Test case b
            //Setup
            Vertex source = getRandomVertex();
            Vertex target = getRandomVertex();
            DirectedEdge newEdge = new FlightGraphEdge(Math.random(), source, target);

            //Test
            boolean containsEdge = false;
            try {
                containsEdge = directedGraph.containsEdge(newEdge);
            } catch (GraphException e) {
                fail("Should not have thrown an exception when running containsEdge(EDGE)");
            }

        //Verify
            assertFalse(containsEdge);

        //Test case c
            //Setup
            try {
                directedGraph.addEdge(newEdge);
            } catch (GraphException e) {
                fail("Should not have thrown an exception when running addEdge(EDGE)");
            }

            //Test
            try {
                containsEdge = directedGraph.containsEdge(newEdge);
            } catch (GraphException e) {
                fail("Should not have thrown an exception when running containsEdge(EDGE)");
            }

            //Verify
            assertTrue(containsEdge);
    }

    /**
     * Tests the functionality to check whether the graph contains a given vertex or not. Again, we'll just check the two
     *  simple cases: where it does contain the vertex and where it doesn't. Tests:
     * <p>  a) When the parameter is invalid
     * <p>  b) When the vertex exists or doesn't exist
     */
    @Test
    public void testContainsVertex() {

        //Test a
            try{
                directedGraph.containsVertex(null);
                fail("Should have thrown exception");
            } catch(Exception e) {}

        //Test b & c
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test
            boolean containsAnotherVertex = false;
            try {
                containsAnotherVertex = directedGraph.containsVertex(anotherVertex);
            } catch (GraphException e) {
                fail("Should not have thrown an exception when calling containsVertex(VERTEX)");
            }
            boolean containsARandomVertex = false;
            try {
                containsARandomVertex = directedGraph.containsVertex(getRandomVertex());
            } catch (GraphException e) {
                fail("Should not have thrown an exception when calling containsVertex(VERTEX");
            }

            //Verify
            assertFalse(containsAnotherVertex);
            assertTrue(containsARandomVertex);
    }

    /**
     * Tests that we can find the in-degree of a given vertex. This test case tests the following cases:
     * <p> a) The vertex is invalid or does not exist, and in this case, we should throw an exception
     * <p> b) The vertex exists but does not have any edges associated with it, in which case we should get zero
     * <p> c) The vertex exists, and has outgoing edges but no incoming edges, in which case we should still get zero
     * <p> d) The vertex exists, and has a nonzero number of incoming edges
     */
    @Test
    public void testFindInDegree() {

        //Test case a
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test
            try{
                directedGraph.findInDegree(null);
                fail("Should have thrown an exception");
            } catch (Exception e) {}
            try{
                directedGraph.findInDegree(anotherVertex);
                fail("Should have thrown an exception");
            } catch (Exception e) {}

        //Test case b
            //Test
            int zeroInDegree = -1;
            try {
                zeroInDegree = directedGraph.findInDegree(getRandomVertex());
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertEquals(0, zeroInDegree);

        //Test case c
            //Setup
            Vertex vertexOne = getRandomVertex();
            Vertex vertexTwo = getRandomVertex();
            while(vertexTwo.equals(vertexOne))
                vertexTwo = getRandomVertex();
            Edge outgoingEdge = new DirectedEdge(vertexOne, vertexTwo);
            List<Edge> edgeList = new ArrayList<Edge>();
            edgeList.add(outgoingEdge);
            directedGraph.setEdgeList(edgeList);

            //Test
            zeroInDegree = -1;
            try {
                zeroInDegree = directedGraph.findInDegree(vertexOne);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertEquals(0, zeroInDegree);

        //Test case d
            //Setup
            vertexOne = getRandomVertex();
            vertexTwo = getRandomVertex();
            while(vertexTwo.equals(vertexOne))
                vertexTwo = getRandomVertex();
            DirectedEdge incomingEdge = new DirectedEdge(vertexOne, vertexTwo);
            edgeList = new ArrayList<Edge>();
            edgeList.add(incomingEdge);
            directedGraph.setEdgeList(edgeList);

            //Test
            int inDegree = -1;
            try {
                inDegree = directedGraph.findInDegree(vertexTwo);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertEquals(1, inDegree);
    }

    /**
     * Tests that we can find the out-degree of a given vertex. This test case tests the following cases:
     * <p> a) The vertex does not exist, and in this case, we should throw an exception
     * <p> b) The vertex exists but does not have any edges associated with it, in which case we should get zero
     * <p> c) The vertex exists, and has incoming edges but no outgoing edges, in which case we should still get zero
     */
    @Test
    public void testFindOutDegree() {

        //Test case a
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test
            try{
                directedGraph.findOutDegree(null);
                fail("Should have thrown an exception");
            } catch (Exception e) {}
            try{
                directedGraph.findOutDegree(anotherVertex);
                fail("Should have thrown an exception");
            } catch (Exception e) {}

        //Test case b
            //Test
            int zeroOutDegree = -1;
            try {
                zeroOutDegree = directedGraph.findOutDegree(getRandomVertex());
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertEquals(0, zeroOutDegree);

        //Test case c
            //Setup
            Vertex vertexOne = getRandomVertex();
            Vertex vertexTwo = getRandomVertex();
            while(vertexTwo.equals(vertexOne))
                vertexTwo = getRandomVertex();
            Edge incomingEdge = new DirectedEdge(vertexOne, vertexTwo);
            List<Edge> edgeList = new ArrayList<Edge>();
            edgeList.add(incomingEdge);
            directedGraph.setEdgeList(edgeList);
            vertexOne.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexTwo, incomingEdge));

            //Test
            zeroOutDegree = -1;
            try {
                zeroOutDegree = directedGraph.findOutDegree(vertexTwo);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertEquals(0, zeroOutDegree);
    }

    /**
     * Tests that we can find the degree of a given vertex in the graph structure. This test covers fundamental cases
     *  but largely checks to see that we delegate the work to the inDegree and outDegree functions correctly. So this
     *  tests:
     * <p> a) If the given vertex does not exist in the graph, or if the vertex is invalid, throw an exception
     * <p> b) If the given vertex does exist in the graph, delegate the computation to the getAdjacent functions
     */
    @Test
    public void testFindDegree() {

        //Test case a
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test
            try{
                directedGraph.findDegree(null);
                fail("Should have thrown an exception");
            } catch (Exception e) {}
            try{
                directedGraph.findDegree(anotherVertex);
                fail("Should have thrown an exception");
            } catch (Exception e) {}

        //Test case b
            //Setup
            Vertex randomVertex = getRandomVertex();

            //Test
            try {
                directedGraph.findDegree(randomVertex);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            try {
                verify(directedGraph, times(1)).findInDegree(eq(randomVertex));
                verify(directedGraph, times(1)).findOutDegree(eq(randomVertex));
            } catch (GraphException e) {
                fail("Should not have thrown an exception!");
            }
    }

    /**
     * Test that we can get all adjacent edges to a given node.
     * <p> a) Test that if the parameter passed to the function doesn't exist in the graph structure or isn't valid,
     *          this will throw an exception
     * <p> b) Test that this will return an empty list if no adjacent edges are found
     * <p> c) Test the case where we actually find data
     */
    @Test
    public void testGetAdjacentEdges() {

        //Test case a
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test
            try{
                directedGraph.getAdjacentEdges(null);
                fail("Should have thrown an exception");
            } catch (Exception e) {}
            try{
                directedGraph.getAdjacentEdges(anotherVertex);
                fail("Should have thrown an exception");
            } catch (Exception e) {}

        //Test case b
            //Setup
            Vertex randomVertex = getRandomVertex();

            //Test
            List<Edge> adjacentEdges = null;
            try {
                directedGraph.getAdjacentEdges(randomVertex);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertNull(adjacentEdges);

        //Test case c
            //Setup
            Vertex vertexOne = getRandomVertex();
            Vertex vertexTwo = getRandomVertex();
            Vertex vertexThree = getRandomVertex();
            while(vertexTwo.equals(vertexOne))
                vertexTwo = getRandomVertex();
            while(vertexThree.equals(vertexOne) || vertexThree.equals(vertexTwo))
                vertexThree = getRandomVertex();
            Edge edgeOne = new DirectedEdge(vertexOne, vertexTwo);
            Edge edgeTwo = new DirectedEdge(vertexOne, vertexThree);
            Edge edgeThree = new DirectedEdge(vertexTwo, vertexOne);
            Edge edgeFour = new DirectedEdge(vertexThree, vertexOne);
            vertexOne.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexTwo, edgeOne));
            vertexOne.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexThree, edgeTwo));
            vertexTwo.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexOne, edgeThree));
            vertexThree.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexOne, edgeFour));

            //Test
            List<Edge> firstAdjacentEdges = null;
            List<Edge> secondAdjacentEdges = null;
            List<Edge> thirdAdjacentEdges = null;
            try {
                firstAdjacentEdges = directedGraph.getAdjacentEdges(vertexOne);
                secondAdjacentEdges = directedGraph.getAdjacentEdges(vertexTwo);
                thirdAdjacentEdges = directedGraph.getAdjacentEdges(vertexThree);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertNotNull(firstAdjacentEdges);
            assertNotNull(secondAdjacentEdges);
            assertNotNull(thirdAdjacentEdges);
            assertEquals(secondAdjacentEdges.size(), thirdAdjacentEdges.size());
            assertEquals(2, firstAdjacentEdges.size());
            assertEquals(1, secondAdjacentEdges.size());
            assertTrue(firstAdjacentEdges.contains(edgeOne));
            assertTrue(firstAdjacentEdges.contains(edgeTwo));
            assertTrue(secondAdjacentEdges.contains(edgeThree));
            assertTrue(thirdAdjacentEdges.contains(edgeFour));
    }

    /**
     * Test that we can get all adjacent vertices to a given node.
     * <p> a) Test that if the parameter passed to the function doesn't exist in the graph structure or isn't valid,
     *          this will throw an exception
     * <p> b) Test that this will return an empty list if no adjacent vertices are found
     * <p> c) Test the case where we actually find data
     */
    @Test
    public void testGetAdjacentVertices() {

        //Test case a
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test
            try{
                directedGraph.getAdjacentVertices(null);
                fail("Should have thrown an exception");
            } catch (Exception e) {}
            try{
                directedGraph.getAdjacentVertices(anotherVertex);
                fail("Should have thrown an exception");
            } catch (Exception e) {}

        //Test case b
            //Setup
            Vertex randomVertex = getRandomVertex();

            //Test
            List<Vertex> adjacentVertices = null;
            try {
                directedGraph.getAdjacentVertices(randomVertex);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertNull(adjacentVertices);

        //Test case c
            //Setup
            Vertex vertexOne = getRandomVertex();
            Vertex vertexTwo = getRandomVertex();
            Vertex vertexThree = getRandomVertex();
            while(vertexTwo.equals(vertexOne))
                vertexTwo = getRandomVertex();
            while(vertexThree.equals(vertexOne) || vertexThree.equals(vertexTwo))
                vertexThree = getRandomVertex();
            Edge edgeOne = new DirectedEdge(vertexOne, vertexTwo);
            Edge edgeTwo = new DirectedEdge(vertexOne, vertexThree);
            Edge edgeThree = new DirectedEdge(vertexTwo, vertexOne);
            Edge edgeFour = new DirectedEdge(vertexThree, vertexOne);
            vertexOne.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexTwo, edgeOne));
            vertexOne.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexThree, edgeTwo));
            vertexTwo.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexOne, edgeThree));
            vertexThree.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(vertexOne, edgeFour));

            //Test
            List<Vertex> firstAdjacentVertices = null;
            List<Vertex> secondAdjacentVertices = null;
            List<Vertex> thirdAdjacentVertices = null;
            try {
                firstAdjacentVertices = directedGraph.getAdjacentVertices(vertexOne);
                secondAdjacentVertices = directedGraph.getAdjacentVertices(vertexTwo);
                thirdAdjacentVertices = directedGraph.getAdjacentVertices(vertexThree);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Verify
            assertNotNull(firstAdjacentVertices);
            assertNotNull(secondAdjacentVertices);
            assertNotNull(thirdAdjacentVertices);
            assertEquals(secondAdjacentVertices.size(), thirdAdjacentVertices.size());
            assertEquals(2, firstAdjacentVertices.size());
            assertEquals(1, secondAdjacentVertices.size());
            assertTrue(firstAdjacentVertices.contains(vertexTwo));
            assertTrue(firstAdjacentVertices.contains(vertexThree));
            assertTrue(secondAdjacentVertices.contains(vertexOne));
            assertTrue(thirdAdjacentVertices.contains(vertexOne));
    }

    /**
     * Simple test that verifies that we get the edge list correctly
     */
    @Test
    public void testGetEdgeList() {

        //Setup
        directedGraph.setEdgeList(edges);

        //Test
        List<Edge> edgeList = directedGraph.getEdgeList();

        //Verify
        assertNotNull(edgeList);
        assertEquals(edgeList, edges);
    }

    /**
     * Tests that the value from the getter is non-null and matches the expected value
     */
    @Test
    public void testGetVertexList() {
        
        //Test
        List<Vertex> vertexList = directedGraph.getVertexList();
        
        //Verify
        assertNotNull(vertexList);
        assertEquals(vertexList, buildVertexList());
    }

    /**
     * Test that we can correctly figure out the source vertex of a directed edge using our library. Tests:
     * <p> a) That an exception is thrown when we try to find the edge source of an edge that does not exist in the graph
     *          structure
     * <p> b) That the correct source vertex is returned when the data is already in the graph structure
     */
    @Test
    public void testGetEdgeSource() {

        //Setup
        Vertex firstVertex = getRandomVertex();
        Vertex secondVertex = getRandomVertex();
        DirectedEdge edge = new DirectedEdge(firstVertex, secondVertex);

        //Test & verify
        try {
            directedGraph.getEdgeSource(null);
            fail("Should have thrown exception trying to get the source for null edge");
        } catch (GraphException e) {}
        try {
            directedGraph.getEdgeSource(edge);
            fail("Should have thrown exception trying to get the source for an edge that doesn't exist in the graph structure");
        } catch (GraphException e) {}
        
        //Setup
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(edge);
        directedGraph.setEdgeList(edges);

        //Test
        Vertex sourceVertex = null;
        try {
            sourceVertex = directedGraph.getEdgeSource(edge);
        } catch (GraphException e) {
            fail("Should not have thrown exception trying to get the source for an edge that exists in the graph structure");
        }
        
        //Verify
        assertNotNull(sourceVertex);
        assertEquals(firstVertex, sourceVertex);
    }

    /**
     * Test that we can correctly figure out the target vertex of a directed edge using our library. Tests two cases:
     * <p> a) That an exception is thrown when we try to find the target of an edge that does not exist in the graph
     *          structure
     * <p> b) That the correct target vertex is returned when the data is already in the graph structure
     */
    @Test
    public void testGetEdgeTarget() {

        //Setup
        Vertex firstVertex = getRandomVertex();
        Vertex secondVertex = getRandomVertex();
        Edge edge = new DirectedEdge(firstVertex, secondVertex);

        //Test & verify
        try {
            directedGraph.getEdgeTarget(edge);
            fail("Should have thrown exception trying to get the target for an edge that doesn't exist in the graph structure");
        } catch (GraphException e) {}

        //Setup
        List<Edge> edges = new ArrayList<Edge>();
        edges.add(edge);
        directedGraph.setEdgeList(edges);

        //Test
        Vertex sourceVertex = null;
        try {
            sourceVertex = directedGraph.getEdgeTarget(edge);
        } catch (GraphException e) {
            fail("Should not have thrown exception trying to get the target for an edge that exists in the graph structure");
        }

        //Verify
        assertNotNull(sourceVertex);
        assertEquals(secondVertex, sourceVertex);
    }

    /**
     * Test that we can remove an edge from the graph structure given the edge to remove. Tests:
     * <p> a) That the library will throw an exception if the edge does not already exist in the graph structure
     * <p> b) That the edge no longer exists in the graph structure after this method is called
     */
    @Test
    public void testRemoveEdgeGivenEdge() {

        //Setup
        Vertex firstVertex = getRandomVertex();
        Vertex secondVertex = getRandomVertex();
        Edge edge = new DirectedEdge(firstVertex, secondVertex);

        //Test & verify
        try {
            directedGraph.removeEdge(edge);
            fail("Should have thrown exception trying to remove an edge that doesn't exist in the graph structure");
        } catch (GraphException e) {}

        //Setup
        try {
            directedGraph.addEdge(edge);
        } catch (GraphException e) {
            fail("Should not have thrown an exception when adding an edge to the graph structure");
        }

        //Test
        boolean removeResult = false;
        assertTrue(directedGraph.getEdgeList().contains(edge));
        try {
            removeResult = directedGraph.removeEdge(edge);
        } catch (GraphException e) {
            fail("Should not have thrown exception trying to remove an edge that exists in the graph structure");
        }

        //Verify
        assertTrue(removeResult);
        assertFalse(directedGraph.getEdgeList().contains(edge));
        assertEquals(0, firstVertex.getAdjacentEdgesAndVertices().size());
        assertEquals(0, secondVertex.getAdjacentEdgesAndVertices().size());
     }

    /**
     * Test that we can remove an edge from the graph structure given the endpoints of the edge.  Tests:
     * <p> a) That the library will throw an exception if one or both of the vertices does not already exist in the graph structure
     * <p> b) That the edge no longer exists in the graph structure after this method is called
     */
    @Test
    public void testRemoveEdgeGivenVertices() {

        //Setup
        Vertex firstVertex = getRandomVertex();
        Vertex secondVertex = getRandomVertex();
        Edge edge = new DirectedEdge(firstVertex, secondVertex);

        //Test & verify
        try {
            directedGraph.removeEdge(firstVertex, secondVertex);
            fail("Should have thrown exception trying to remove an edge that doesn't exist in the graph structure");
        } catch (GraphException e) {}

        //Setup
        try {
            directedGraph.addEdge(edge);
        } catch (GraphException e) {
            fail("Should not have thrown an exception when adding an edge to the graph structure");
        }

        //Test
        boolean removeResult = false;
        assertTrue(directedGraph.getEdgeList().contains(edge));
        try {
            removeResult = directedGraph.removeEdge(firstVertex, secondVertex);
        } catch (GraphException e) {
            fail("Should not have thrown exception trying to remove an edge that exists in the graph structure");
        }

        //Verify
        assertTrue(removeResult);
        assertFalse(directedGraph.getEdgeList().contains(edge));
        assertEquals(0, firstVertex.getAdjacentEdgesAndVertices().size());
        assertEquals(0, secondVertex.getAdjacentEdgesAndVertices().size());
    }

    /**
     * Test that we can remove a vertex from the graph structure correctly. Tests the following cases:
     *  <p> a) Invalid inputs to remove vertex, should throw an exception, including null or nonexistent values
     *  <p> b) Vertex that is attached to an edge, in which case the library should remove this vertex and all connected edges
     *  <p> c) Vertex that is not attached to any edge, which should succeed
     */
    @Test
    public void testRemoveVertex() {
        
        //Test a
            //Setup
            Vertex anotherVertex = new FlightGraphVertex("LAX", "Los Angeles", "US", "North America", -8, new Pair('N', 34), new Pair('W', 118), 17900000, 1);

            //Test & verify
            try {
                directedGraph.removeVertex(null);
                fail("Should have thrown an exception trying to remove a null existent vertex from the graph structure");
            } catch (GraphException e) {}
            try {
                directedGraph.removeVertex(anotherVertex);
                fail("Should have thrown an exception trying to remove a non-existent vertex from the graph structure");
            } catch (GraphException e) {}

        //Test b
            //Setup
            Vertex firstVertex = getRandomVertex();
            Vertex secondVertex = getRandomVertex();
            Edge edge = new DirectedEdge(firstVertex, secondVertex);
            try {
                directedGraph.addEdge(edge);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Test
            try {
                directedGraph.removeVertex(firstVertex);
            } catch (GraphException e) {
                fail("Should not have thrown an exception trying to remove a null existent vertex from the graph structure");
            }
            try {
                directedGraph.removeVertex(secondVertex);
            } catch (GraphException e) {
                fail("Should not have thrown an exception trying to remove a null existent vertex from the graph structure");
            }

            //Verify
            assertFalse(directedGraph.getEdgeList().contains(edge));

        //Test c
            //Setup
            try {
                directedGraph.addVertex(firstVertex);
                directedGraph.addVertex(secondVertex);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }

            //Test
            boolean result = false;
            Vertex randomVertex = getRandomVertex();
            assertTrue(directedGraph.getVertexList().contains(randomVertex));
            try {
                directedGraph.addEdge(edge);
            } catch (GraphException e) {
                fail("Should not have thrown an exception");
            }
            try{
                result = directedGraph.removeVertex(randomVertex);
            } catch (GraphException e){
                fail("Should not have thrown an exception when removing an existing vertex from the graph structure");
            }

            //Verify
            assertTrue(result);
            assertFalse(directedGraph.getVertexList().contains(randomVertex));
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
    private List<Edge> buildEdgeList() {

        List<Edge> edgeList = new ArrayList<Edge>();

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
     * Simple helper function to grab a random vertex from the vertices list
     *
     * @return A random vertex from our list
     */
    private Vertex getRandomVertex() {
        return vertices.get((int) (Math.random() * vertices.size()));
    }

    /**
     * Private helper function to generate some random positive short
     */
    private short randomShort() {
        Random random = new Random();
        int data = random.nextInt();
        return (short) Math.abs(data);
    }
}
