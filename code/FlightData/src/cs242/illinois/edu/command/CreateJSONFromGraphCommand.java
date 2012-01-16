package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.*;
import cs242.illinois.edu.utilities.TextIO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class CreateJSONFromGraphCommand extends FlightGraphCommand{

    /**
     * {@inheritDoc}
     *
     * Exports data to a JSON object from a graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        try {
            parseJSON(graph);
        } catch (JSONException e) {
            TextIO.putln("Failed to parse JSON file!");
        }

        TextIO.writeFile(null);
        TextIO.putln("Successfully loaded graph to JSON file!");

        //Success
        return true;
    }

    private void parseJSON(Graph graph) throws JSONException {

        //Read input file through TextIO
        File outputFile = new File("output_map_data.json");
        if(outputFile.exists()){
            try {
                outputFile.delete();
                outputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TextIO.writeFile("output_map_data.json");

        //An array of the properties of a flight graph vertices and edges
        String[] flightProperties = FlightGraphVertex.getProperties();
        String[] cityProperties = FlightGraphEdge.getProperties();

        //Grab the edge list from the graph
        List<Edge> edges = graph.getEdgeList();
        List<FlightGraphEdge> flights = new ArrayList<FlightGraphEdge>();
        for(Edge edge : edges){
            flights.add((FlightGraphEdge) edge);
        }

        //Grab the vertex list from the graph
        List<Vertex> vertices = graph.getVertexList();
        List<FlightGraphVertex> cities = new ArrayList<FlightGraphVertex>();
        for(Vertex vertex : vertices){
            cities.add((FlightGraphVertex) vertex);
        }

        //Translate the list of flights from the graph into an array of JSON objects
        JSONArray edgeArray = new JSONArray();
        for(FlightGraphEdge flight : flights){

            //Create a new JSON object for this flight
            JSONObject jsonFlight = new JSONObject();

            //Grab the ports of this flight, and turn them into a string array
            FlightGraphVertex source = (FlightGraphVertex) flight.getSource();
            FlightGraphVertex target = (FlightGraphVertex) flight.getTarget();
            String[] ports = new String[]{source.getAirportCode(), target.getAirportCode()};

            //Add each individual property to the object
            jsonFlight.put("length", Double.toString(flight.getLength()));
            jsonFlight.put("ports", ports);

            //Add this flight to the edge array
            edgeArray.put(jsonFlight);
        }

        //Translates the list of vertices into an array of JSON objects
        JSONArray vertexArray = new JSONArray();
        for(FlightGraphVertex city : cities){

            //Create a new JSON object for this city
            JSONObject jsonCity = new JSONObject();

            //Grab the coordinates of this city
            Pair<Character, Integer> longitude = city.getLongitude();
            Pair<Character, Integer> latitude = city.getLatitude();

            //Create a JSON object out of these coordinates
            JSONObject coordinates = new JSONObject();
            coordinates.put(longitude.getFirst().toString(), longitude.getSecond());
            coordinates.put(latitude.getFirst().toString(), latitude.getSecond());

            //Add each property of a city to this JSON object
            jsonCity.put("code", city.getAirportCode());
            jsonCity.put("name", city.getName());
            jsonCity.put("country", city.getCountry());
            jsonCity.put("continent", city.getContinent());
            jsonCity.put("timezone", city.getTimezone());
            jsonCity.put("coordinates", coordinates);
            jsonCity.put("population", city.getPopulation());
            jsonCity.put("region", city.getRegion());

            //Append this object to the array of vertices
            vertexArray.put(jsonCity);
        }

        //Grab the data sources
        String[] dataSources = new String[]{"http://www.gcmap.com/","http://www.theodora.com/country_digraphs.html","http://www.citypopulation.de/world/Agglomerations.html","http://www.mongabay.com/cities_urban_01.htm","http://en.wikipedia.org/wiki/Urban_agglomeration","http://www.worldtimezone.com/standard.html"};

        //Add these three objects to one big object
        JSONObject jsonGraph = new JSONObject();
        jsonGraph.put("data sources", dataSources);
        jsonGraph.put("metros", vertexArray);
        jsonGraph.put("routes", edgeArray);

        //Write this JSON to disk
        TextIO.putln(jsonGraph.toString(4)); //Write with some arbitrary number of spaces used for indentation
    }
}
