package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.*;
import cs242.illinois.edu.utilities.LabelConstants;
import cs242.illinois.edu.utilities.TextIO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class CreateGraphFromJSONCommand extends FlightGraphCommand{

    /**
     * {@inheritDoc}
     *
     * Imports data from a JSON object and parses it as a graph
     */
    public boolean execute(Graph graph) {

        //Error Check
        if (graph == null) {
            TextIO.putln(LabelConstants.UNINITIZIALIZED_GRAPH_ERROR_MESSAGE);
            return false;
        }

        try {
            parseJSON(graph);
        } catch (JSONException e) {
            TextIO.putln("Failed to parse JSON file!");
        }

        TextIO.readFile(null);
        TextIO.writeFile(null);
        TextIO.putln("Successfully loaded JSON file data to graph!");

        //Success
        return true;
    }

    private void parseJSON(Graph graph) throws JSONException {
        
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
            TextIO.putln("Error!\n" + e.getMessage());
            graph = new DirectedGraph();
            return;
        }

        //Prepare the lists
        List<Edge> castFlights = new ArrayList();
        castFlights.addAll(flights);
        List<Vertex> castAirports = new ArrayList();
        castAirports.addAll(airports);

        //Update the actual graph object
        graph.setEdgeList(castFlights);
        graph.setVertexList(castAirports);
    }
}
