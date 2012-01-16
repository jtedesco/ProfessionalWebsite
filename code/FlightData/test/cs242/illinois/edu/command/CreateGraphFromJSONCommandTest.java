package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.*;
import cs242.illinois.edu.utilities.TextIO;
import junit.framework.TestCase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> Tests that we can create a graph from the JSON input
 */
public class CreateGraphFromJSONCommandTest extends TestCase {

    /**
     * Tests if we can build a graph from the input JSON
     */
    @Test
    public void testCreateGraphFromJSONCommand() throws JSONException {

        //Read input file through TextIO
        TextIO.readFile("map_data.json");
        String json = "";
        while(!TextIO.eof()){
            json += TextIO.getln();
        }

        //Parse these two lists from the JSON
        List<Vertex> airports = new ArrayList<Vertex>();
        List<DirectedEdge> flights = new ArrayList<DirectedEdge>();
        try {

            //Pull out the nested objects we care about
            JSONObject jsonData = new JSONObject(json);
            JSONArray metros = jsonData.getJSONArray("metros");
            JSONArray routes = jsonData.getJSONArray("routes");

            //Parse the airports from the JSON
            for(int index = 0; index<metros.length(); index++){
                JSONObject metro = metros.getJSONObject(index);
                JSONObject coordinates = metro.getJSONObject("coordinates");

                //Parse the longitude as a pair
                Pair<Character, Integer> longitude;
                try{
                    longitude = new Pair('N',coordinates.getInt("N"));
                } catch (Exception e) {
                    longitude = new Pair('S',coordinates.getInt("S"));
                }

                //Parse the longitude as a pair
                Pair<Character, Integer> latitude;
                try{
                    latitude = new Pair('E',coordinates.getInt("E"));
                } catch (Exception e) {
                    latitude = new Pair('W',coordinates.getInt("W"));
                }
                airports.add(new FlightGraphVertex(metro.getString("code"), metro.getString("name"), metro.getString("country"), metro.getString("continent"), metro.getInt("timezone"), longitude, latitude, metro.getInt("population"), metro.getInt("region")));
            }

            //Parse the flights from the JSON
            for(int index = 0; index<routes.length(); index++){
                JSONObject route = routes.getJSONObject(index);
                JSONArray associatedAirports = route.getJSONArray("ports");

                //Find the associated airports
                FlightGraphVertex origin = (FlightGraphVertex) airports.get(airports.indexOf(new FlightGraphVertex(associatedAirports.getString(0), "", "", "", 0, null, null, 0, 0)));
                FlightGraphVertex destination = (FlightGraphVertex) airports.get(airports.indexOf(new FlightGraphVertex(associatedAirports.getString(1), "", "", "", 0, null, null, 0, 0)));

                //Add the edge
                FlightGraphEdge outgoingEdge = new FlightGraphEdge(route.getDouble("distance"), origin, destination);
                FlightGraphEdge returningEdge = new FlightGraphEdge(route.getDouble("distance"), destination, origin);
                flights.add(outgoingEdge);
                flights.add(returningEdge);

                //Update the vertices with adjacency info
                origin.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(destination, outgoingEdge));
                destination.addEdgeAndVertexToAdjacentEdgesAndVertices(new EdgeVertexBean(origin, returningEdge));
            }
        } catch (JSONException e) {
            fail("Should not have thrown an exception while parsing json file");
        }

        //Setup
        Graph expectedGraph = null;
        try {
            expectedGraph = new DirectedGraph(airports, flights);
        } catch (GraphException e) {
            e.printStackTrace();
        }
        Graph actualGraph = null;
        try {
            actualGraph = new DirectedGraph(new ArrayList(), new ArrayList());
        } catch (GraphException e) {
            fail("Should not have thrown an exception created a new empty graph!");
        }
        CreateGraphFromJSONCommand createGraphCommand = new CreateGraphFromJSONCommand();

        //Test
        createGraphCommand.execute(actualGraph);

        //Verify
        assertTrue(expectedGraph.equals(actualGraph));
        assertTrue(actualGraph.getEdgeList().containsAll(expectedGraph.getEdgeList()));
        assertTrue(actualGraph.getVertexList().containsAll(expectedGraph.getVertexList()));
        assertEquals(expectedGraph.getEdgeList().size(), actualGraph.getEdgeList().size());
        assertEquals(expectedGraph.getVertexList().size(), actualGraph.getVertexList().size());
        for(Edge edge : expectedGraph.getEdgeList()){
            FlightGraphEdge castEdge = (FlightGraphEdge) edge;
            assertNotNull(actualGraph.getEdgeList().get(actualGraph.getEdgeList().indexOf(castEdge)));
        }
        for(Vertex vertex : expectedGraph.getVertexList()){
            FlightGraphVertex castVertex = (FlightGraphVertex) vertex;
            assertTrue(castVertex.fullEquals(actualGraph.getVertexList().get(actualGraph.getVertexList().indexOf(castVertex))));
        }
    }
}
