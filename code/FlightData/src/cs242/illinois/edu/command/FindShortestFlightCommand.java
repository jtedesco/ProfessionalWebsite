package cs242.illinois.edu.command;

import cs242.illinois.edu.graph.FlightGraphEdge;
import cs242.illinois.edu.graph.Graph;
import cs242.illinois.edu.utilities.FlightGraphUtilities;
import cs242.illinois.edu.utilities.TextIO;

import java.util.List;

/**
 * <p> Created By: Jon Tedesco
 * <p> Date: Sep 30, 2010
 *
 * <p> <p> A command to be executed on the flight graph
 */
public class FindShortestFlightCommand extends FlightGraphCommand{

    /**
     * {@inheritDoc}
     *
     * Finds the shortest flight in the graph
     */
    public boolean execute(Graph graph) {

        //Error-check
         if(!super.execute(graph))
                 return false;

        //Pull out the list of flights from the graph, and build a list of FlightGraphVertices
        List<FlightGraphEdge> flights = FlightGraphUtilities.getFlightGraphEdges(graph);

         //Find the shortest flight
        FlightGraphEdge shortestFlight = findShortestFlight(flights);

        //Output data
        TextIO.putln(shortestFlight.toString());

        //Success
        return true;
    }

    //Finds the longest flight
    public FlightGraphEdge findShortestFlight(List<FlightGraphEdge> flights) {
        double shortestFlightLength = Double.MAX_VALUE;
        FlightGraphEdge shortestFlight = null;
        for(FlightGraphEdge flightGraphEdge : flights){
            if(flightGraphEdge.getLength() < shortestFlightLength){
                shortestFlightLength = flightGraphEdge.getLength();
                shortestFlight = flightGraphEdge;
            }
        }
        return shortestFlight;
    }
}
